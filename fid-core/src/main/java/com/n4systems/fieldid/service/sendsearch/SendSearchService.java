package com.n4systems.fieldid.service.sendsearch;

import com.n4systems.ejb.PageHolder;
import com.n4systems.exceptions.EmptyReportException;
import com.n4systems.fieldid.context.ThreadLocalInteractionContext;
import com.n4systems.fieldid.service.FieldIdPersistenceService;
import com.n4systems.fieldid.service.asset.AssetExcelExportService;
import com.n4systems.fieldid.service.download.StringRowPopulator;
import com.n4systems.fieldid.service.download.TableGenerationContext;
import com.n4systems.fieldid.service.download.TableGenerationContextImpl;
import com.n4systems.fieldid.service.event.EventExcelExportService;
import com.n4systems.fieldid.service.event.util.ResultTransformerFactory;
import com.n4systems.fieldid.service.search.AssetSearchService;
import com.n4systems.fieldid.service.search.ReportService;
import com.n4systems.fieldid.service.search.SavedAssetSearchService;
import com.n4systems.fieldid.service.search.SavedReportService;
import com.n4systems.mail.SMTPMailManager;
import com.n4systems.model.ExtendedFeature;
import com.n4systems.model.SendSavedItemSchedule;
import com.n4systems.model.api.Archivable;
import com.n4systems.model.common.ReportFormat;
import com.n4systems.model.saveditem.SavedItem;
import com.n4systems.model.saveditem.SavedReportItem;
import com.n4systems.model.saveditem.SavedSearchItem;
import com.n4systems.model.search.AssetSearchCriteria;
import com.n4systems.model.search.ColumnMappingView;
import com.n4systems.model.search.EventReportCriteria;
import com.n4systems.model.search.SearchCriteria;
import com.n4systems.model.security.OpenSecurityFilter;
import com.n4systems.model.security.TenantOnlySecurityFilter;
import com.n4systems.model.security.UserSecurityFilter;
import com.n4systems.model.user.User;
import com.n4systems.util.DateHelper;
import com.n4systems.util.LogUtils;
import com.n4systems.util.mail.TemplateMailMessage;
import com.n4systems.util.persistence.QueryBuilder;
import com.n4systems.util.persistence.search.ResultTransformer;
import com.n4systems.util.views.RowView;
import com.n4systems.util.views.TableView;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import javax.mail.MessagingException;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

public class SendSearchService extends FieldIdPersistenceService {
    
    private static final Logger logger = Logger.getLogger(SendSearchService.class);
    private static final int MAX_RESULTS_FOR_SENT_SEARCH = 500;
    private static volatile Properties localizationProperties = null;

    @Autowired private AssetSearchService searchService;
    @Autowired private ReportService reportService;
    @Autowired private SavedAssetSearchService savedAssetSearchService;
    @Autowired private SavedReportService savedReportService;
    
    @Autowired private AssetExcelExportService assetExcelExportService;
    @Autowired private EventExcelExportService eventExcelExportService;

    @Transactional
    public void sendAllDueItems() {
        QueryBuilder<SendSavedItemSchedule> query = new QueryBuilder<SendSavedItemSchedule>(SendSavedItemSchedule.class, new OpenSecurityFilter());
        query.addSimpleWhere("user.state", Archivable.EntityState.ACTIVE);
        query.addSimpleWhere("tenant.disabled", false);

        List<SendSavedItemSchedule> schedules = persistenceService.findAll(query);
        for (SendSavedItemSchedule schedule : schedules) {
            if(schedule.getUser().getOwner().getPrimaryOrg().getExtendedFeatures().contains(ExtendedFeature.EmailAlerts)) {
                try {
                    Date utcDateToTheHour = DateHelper.truncate(new Date(), DateHelper.HOUR);
                    if (schedule.shouldRunNow(utcDateToTheHour)) {
                        sendSavedItemInsideUserContext(schedule.getSavedItem().getId(), schedule);
                    }
                } catch (Exception e) {
                    logger.error("Error sending notification: " + ToStringBuilder.reflectionToString(schedule), e);
                }
            }
        }
    }

    private void sendSavedItemInsideUserContext(Long savedItemId, SendSavedItemSchedule schedule) throws MessagingException {
        Locale previousLanguage = ThreadLocalInteractionContext.getInstance().getUserThreadLanguage();
        try {
            ThreadLocalInteractionContext.getInstance().setUserThreadLanguage(schedule.getUser().getLanguage());
            securityContext.setUserSecurityFilter(new UserSecurityFilter(schedule.getUser()));
            securityContext.setTenantSecurityFilter(new TenantOnlySecurityFilter(schedule.getTenant()));
            sendSavedItem(savedItemId, schedule);
        } finally {
            // Must clear the session to nuke any translated entities
            persistenceService.clearSession();
            securityContext.reset();
            ThreadLocalInteractionContext.getInstance().setUserThreadLanguage(previousLanguage);
        }
    }

    @Transactional
    public void sendSavedItem(Long savedItemId, SendSavedItemSchedule schedule) throws MessagingException {
        SearchCriteria searchCriteria = getSearchCriteriaForSavedItem(savedItemId);
        sendSearch(searchCriteria, schedule);
    }

    private SearchCriteria getSearchCriteriaForSavedItem(Long savedItemId) {
        SavedItem savedItem = persistenceService.find(SavedItem.class, savedItemId);
        if (savedItem instanceof SavedSearchItem) {
            return savedAssetSearchService.getConvertedReport(SavedSearchItem.class, savedItem.getId()).getSearchCriteria();
        } else if (savedItem instanceof SavedReportItem) {
            return savedReportService.getConvertedReport(SavedReportItem.class, savedItem.getId()).getSearchCriteria();
        }
        return savedItem.getSearchCriteria();
    }

    @Transactional
    public void sendSearch(SearchCriteria searchCriteria, SendSavedItemSchedule schedule) throws MessagingException {
        localizeColumnNames(searchCriteria.getSortedStaticAndDynamicColumns());
            if (schedule.getReportFormat() == ReportFormat.HTML) {
                sendSearchHtml(searchCriteria, schedule);
            } else if (schedule.getReportFormat() == ReportFormat.EXCEL) {
                sendSearchExcel(searchCriteria, schedule);
            }
    }

    public Long countSavedItemSchedules(SavedItem savedItem) {
        QueryBuilder<SendSavedItemSchedule> query = createUserSecurityBuilder(SendSavedItemSchedule.class);
        query.addSimpleWhere("savedItem", savedItem);
        return persistenceService.count(query);
    }

    private void sendSearchExcel(SearchCriteria criteria, SendSavedItemSchedule schedule) throws MessagingException {
        ByteArrayOutputStream attachmentData = new ByteArrayOutputStream();
        boolean exceptionOnEmptyReport = !schedule.isSendBlankReport();

        try {
            try {
                if (criteria instanceof AssetSearchCriteria) {
                    assetExcelExportService.generateFile((AssetSearchCriteria)criteria, attachmentData, false, 0, MAX_RESULTS_FOR_SENT_SEARCH, exceptionOnEmptyReport);
                } else if (criteria instanceof EventReportCriteria) {
                    eventExcelExportService.generateFile((EventReportCriteria)criteria, attachmentData, false, 0, MAX_RESULTS_FOR_SENT_SEARCH, exceptionOnEmptyReport);
                }
            } catch(EmptyReportException ere) {
                return;
            }

            TemplateMailMessage message = createBasicHtmlMessage("sendSavedItemExcel", criteria, schedule);

            message.getAttachments().put(getReportFileName(schedule), attachmentData.toByteArray());

            logger.info(LogUtils.prepare("Sending Notification Message to [$0] recipients", message.getToAddresses().size()));

            new SMTPMailManager().sendMessage(message);

        } catch (Exception e) {
            logger.error("error creating excel export", e);
        }
    }

    private String getReportFileName(SendSavedItemSchedule schedule) {
        StringBuilder fileName = new StringBuilder();
        if (schedule.getSavedItem() != null) {
            fileName.append(schedule.getSavedItem().getName());
        } else {
            fileName.append("notification");
        }
        fileName.append(" - ");
        fileName.append(new SimpleDateFormat(getCurrentUser().getOwner().getPrimaryOrg().getDateFormat()).format(new Date()));
        fileName.append(".xlsx");
        return fileName.toString();
    }

    private void sendSearchHtml(SearchCriteria criteria, SendSavedItemSchedule schedule) throws MessagingException {
        ResultTransformer<TableView> transformer = new ResultTransformerFactory().createResultTransformer(criteria);
        PageHolder<TableView> pageHolder = null;
        if (criteria instanceof AssetSearchCriteria) {
            pageHolder = searchService.performSearch((AssetSearchCriteria)criteria, transformer, 0, MAX_RESULTS_FOR_SENT_SEARCH);
        } else if (criteria instanceof EventReportCriteria) {
            pageHolder = reportService.performSearch((EventReportCriteria)criteria, transformer, 0, MAX_RESULTS_FOR_SENT_SEARCH);
        }

        List<RowView> results = pageHolder.getPageResults().getRows();

        int rowCount = 0;
        rowCount = results.size();

        if ((rowCount > 0) || (rowCount <= 0 && schedule.isSendBlankReport())) {
            sendHtmlMessage(criteria, results, schedule);
        }
    }

    private void sendHtmlMessage(SearchCriteria criteria, List<RowView> results, SendSavedItemSchedule schedule) throws MessagingException {
        User user = schedule.getUser();
        TableGenerationContext exportContextProvider = new TableGenerationContextImpl(user.getTimeZone(), user.getOwner().getPrimaryOrg().getDateFormat(), user.getOwner().getPrimaryOrg().getDateFormat() + " h:mm a", user.getOwner());
        StringRowPopulator.populateRowsWithConvertedStrings(results, criteria, exportContextProvider);

        // no we need to build the message body with the html event report table
        TemplateMailMessage message = createBasicHtmlMessage("sendSavedItem", criteria, schedule);

        message.getTemplateMap().put("columns", criteria.getSortedStaticAndDynamicColumns());
        message.getTemplateMap().put("rows", results);
        message.getTemplateMap().put("numResults", results.size());

        logger.info(LogUtils.prepare("Sending Notification Message to [$0] recipients", message.getToAddresses().size()));

        new SMTPMailManager().sendMessage(message);
    }
    
    private TemplateMailMessage createBasicHtmlMessage(String templateName, SearchCriteria criteria, SendSavedItemSchedule schedule) {
        Set<String> addressesToDeliverTo = schedule.getAddressesToDeliverTo();
        String title = getSanitizedTitle(schedule, criteria);

        TemplateMailMessage message = new TemplateMailMessage(schedule.getSubject(), templateName);

        message.getTemplateMap().put("title", title);
        message.getTemplateMap().put("message", sanitize(schedule.getMessage()));
        message.getTemplateMap().put("maxResults", MAX_RESULTS_FOR_SENT_SEARCH);
        message.setToAddresses(addressesToDeliverTo);

        int preTruncateResultCount = countResults(criteria);
        if (preTruncateResultCount > MAX_RESULTS_FOR_SENT_SEARCH) {
            message.getTemplateMap().put("resultCountBeforeTruncation", preTruncateResultCount);
        }

        return message;
    }

    private String getSanitizedTitle(SendSavedItemSchedule schedule, SearchCriteria criteria) {
        if (schedule.getSavedItem() != null) {
            return sanitize(schedule.getSavedItem().getName());
        }
        return criteria instanceof AssetSearchCriteria ? "Search" : "Report";
    }

    private String sanitize(String message) {
        if (message == null) {
            return null;
        }
        return message.replaceAll("<", "&lt;").replaceAll(">", "&gt;").replaceAll("\n", "<p>&nbsp;");
    }

    // Localization is done in the web for now.. this is a hack to get at Strut's localization file
    // since we're in core, and we can't see struts classes
    private void localizeColumnNames(List<ColumnMappingView> columns) {
        User user = getCurrentUser();

        for (ColumnMappingView column : columns) {
			Properties lang = getLang();
            if (lang.get(column.getLabel()) != null) {
                column.setLocalizedLabel(lang.get(column.getLabel()).toString());
            } else {
                if ("label.eusername".equals(column.getLabel())) {
                    column.setLocalizedLabel(getCustomerLabel(user));
                } else {
                    column.setLocalizedLabel(column.getLabel());
                }
            }
        }
    }
    
    private int countResults(SearchCriteria criteria) {
        if (criteria instanceof AssetSearchCriteria) {
            return searchService.countPages((AssetSearchCriteria)criteria, 1L);
        } else if (criteria instanceof  EventReportCriteria) {
            return reportService.countPages((EventReportCriteria)criteria, 1L);
        }
        return 0;
    }

    private String getCustomerLabel(User user) {
        boolean jobSiteUser = user.getOwner().getPrimaryOrg().getExtendedFeatures().contains(ExtendedFeature.JobSites);
        if (jobSiteUser) {
            return "Job Site Name";
        } else {
            return "Customer Name";
        }
    }

    private static Properties getLang() {
		if (localizationProperties == null) {
			synchronized (SendSearchService.class) {
				if (localizationProperties == null) {
					try {
						localizationProperties = new Properties();
						localizationProperties.load(SendSearchService.class.getClassLoader().getResourceAsStream("com/n4systems/fieldid/actions/package.properties"));
					} catch (IOException e) {
						logger.error("Failed loading Struts package.properties", e);
						localizationProperties = null;
					}
				}
			}
		}
        return localizationProperties;
    }

}
