package com.n4systems.fieldid.service.sendsearch;

import com.n4systems.ejb.PageHolder;
import com.n4systems.fieldid.service.FieldIdPersistenceService;
import com.n4systems.fieldid.service.download.StringRowPopulator;
import com.n4systems.fieldid.service.download.TableGenerationContext;
import com.n4systems.fieldid.service.download.TableGenerationContextImpl;
import com.n4systems.fieldid.service.event.util.ResultTransformerFactory;
import com.n4systems.fieldid.service.search.AssetSearchService;
import com.n4systems.fieldid.service.search.ReportService;
import com.n4systems.fieldid.service.search.SavedAssetSearchService;
import com.n4systems.fieldid.service.search.SavedReportService;
import com.n4systems.mail.SMTPMailManager;
import com.n4systems.model.ExtendedFeature;
import com.n4systems.model.SendSavedItemSchedule;
import com.n4systems.model.saveditem.SavedSearchItem;
import com.n4systems.model.search.AssetSearchCriteria;
import com.n4systems.model.search.ColumnMappingView;
import com.n4systems.model.search.EventReportCriteria;
import com.n4systems.model.search.SearchCriteria;
import com.n4systems.model.user.User;
import com.n4systems.util.LogUtils;
import com.n4systems.util.mail.TemplateMailMessage;
import com.n4systems.util.persistence.search.ResultTransformer;
import com.n4systems.util.views.RowView;
import com.n4systems.util.views.TableView;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import javax.mail.MessagingException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Set;

public class SendSearchService extends FieldIdPersistenceService {
    
    private static final Logger logger = Logger.getLogger(SendSearchService.class);
    private static final int MAX_RESULTS_FOR_SENT_SEARCH = 100;
    private static final Properties localizationProperties = loadProperties();

    @Autowired
    private AssetSearchService searchService;

    @Autowired
    private ReportService reportService;

    @Autowired
    private SavedAssetSearchService savedAssetSearchService;

    @Autowired
    private SavedReportService savedReportService;

    @Transactional
    public void sendAll() {
        List<SendSavedItemSchedule> schedules = persistenceService.findAll(SendSavedItemSchedule.class);
        for (SendSavedItemSchedule schedule : schedules) {
            try {
                sendSavedItem(schedule.getSavedItem().getId(), schedule);
            } catch(MessagingException e) {
                logger.error("Error sending notification: " + schedule);
            }
        }
    }
    
    @Transactional
    public void sendSavedItem(Long savedItemId, SendSavedItemSchedule schedule) throws MessagingException {
        SavedSearchItem convertedReport = savedAssetSearchService.getConvertedReport(SavedSearchItem.class, savedItemId);
        AssetSearchCriteria searchCriteria = convertedReport.getSearchCriteria();
        sendSearch(searchCriteria, schedule);
    }

    @Transactional
    public void sendSearch(SearchCriteria searchCriteria, SendSavedItemSchedule schedule) throws MessagingException {
        ResultTransformer<TableView> transformer = new ResultTransformerFactory().createResultTransformer(searchCriteria);
        PageHolder<TableView> pageHolder = null;
        if (searchCriteria instanceof AssetSearchCriteria) {
            pageHolder = searchService.performSearch((AssetSearchCriteria)searchCriteria, transformer, 0, MAX_RESULTS_FOR_SENT_SEARCH);
        } else if (searchCriteria instanceof EventReportCriteria) {
            pageHolder = reportService.performSearch((EventReportCriteria)searchCriteria, transformer, 0, MAX_RESULTS_FOR_SENT_SEARCH);
        }

        List<RowView> results = pageHolder.getPageResults().getRows();
        sendMessage(searchCriteria, results, schedule);
    }

    private void sendMessage(SearchCriteria criteria, List<RowView> results, SendSavedItemSchedule schedule) throws MessagingException {
        String messageSubject = "Search Results";

        Set<String> addressesToDeliverTo = schedule.getAddressesToDeliverTo();

        User user = schedule.getUser();
        TableGenerationContext exportContextProvider = new TableGenerationContextImpl(user.getTimeZone(), user.getOwner().getPrimaryOrg().getDateFormat(), user.getOwner().getPrimaryOrg().getDateFormat() + " h:mm a", user.getOwner());
        StringRowPopulator.populateRowsWithConvertedStrings(results, criteria, exportContextProvider);

        // no we need to build the message body with the html event report table
        TemplateMailMessage message = new TemplateMailMessage(messageSubject, "sendSavedItem");

        List<ColumnMappingView> columns = criteria.getSortedStaticAndDynamicColumns();
        message.getTemplateMap().put("message", sanitize(schedule.getMessage()));
        message.getTemplateMap().put("columns", localizeColumnNames(columns, user));
        message.getTemplateMap().put("rows", results);
        message.getTemplateMap().put("numResults", results.size());
        message.setToAddresses(addressesToDeliverTo);

        logger.info(LogUtils.prepare("Sending Notification Message to [$0] recipients", addressesToDeliverTo.size()));

        new SMTPMailManager().sendMessage(message);
    }

    private String sanitize(String message) {
        if (message == null) {
            return null;
        }
        return message.replaceAll("<", "&lt;").replaceAll(">", "&gt;").replaceAll("\n", "<p>&nbsp;");
    }

    // Localization is done in the web for now.. this is a hack to get at Strut's localization file
    // since we're in core, and we can't see struts classes
    private List<String> localizeColumnNames(List<ColumnMappingView> columns, User user) {
        List<String> localizedNames = new ArrayList<String>(columns.size());

        for (ColumnMappingView column : columns) {
            if (localizationProperties.get(column.getLabel()) != null) {
                localizedNames.add(localizationProperties.get(column.getLabel()).toString());
            } else {
                if ("label.eusername".equals(column.getLabel())) {
                    addCustomerLabel(user, localizedNames);
                } else {
                    localizedNames.add(column.getLabel());
                }
            }
        }
        return localizedNames;
    }

    private void addCustomerLabel(User user, List<String> localizedNames) {
        boolean jobSiteUser = user.getOwner().getPrimaryOrg().getExtendedFeatures().contains(ExtendedFeature.JobSites);
        if (jobSiteUser) {
            localizedNames.add("Job Site Name");
        } else {
            localizedNames.add("Customer Name");
        }
    }

    private static Properties loadProperties() {
        Properties allProperties = new Properties();
        try {
            allProperties.load(SendSearchService.class.getClassLoader().getResourceAsStream("com/n4systems/fieldid/actions/package.properties"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return allProperties;
    }
    
}
