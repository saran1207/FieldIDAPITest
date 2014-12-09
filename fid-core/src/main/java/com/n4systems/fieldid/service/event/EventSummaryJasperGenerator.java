package com.n4systems.fieldid.service.event;

import com.n4systems.exceptions.ReportException;
import com.n4systems.fieldid.context.ThreadLocalInteractionContext;
import com.n4systems.fieldid.service.FieldIdPersistenceService;
import com.n4systems.fieldid.service.amazon.S3Service;
import com.n4systems.fieldid.service.org.OrgService;
import com.n4systems.model.*;
import com.n4systems.model.orgs.InternalOrg;
import com.n4systems.model.orgs.PrimaryOrg;
import com.n4systems.model.search.EventReportCriteria;
import com.n4systems.model.utils.DateTimeDefiner;
import com.n4systems.reporting.EventReportMapProducer;
import com.n4systems.reporting.PathHandler;
import com.n4systems.reporting.SubEventReportMapProducer;
import com.n4systems.services.date.DateService;
import com.n4systems.util.StringListingPair;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.util.JRLoader;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import rfid.ejb.entity.InfoOptionBean;

import java.io.*;
import java.util.*;

public class EventSummaryJasperGenerator extends FieldIdPersistenceService {

    private static final String n4LogoFileName = "n4_logo.gif";
    private Logger logger = Logger.getLogger(EventSummaryJasperGenerator.class);

    private @Autowired EventService eventService;
    private @Autowired OrgService orgService;
    private @Autowired DateService dateService;
    private @Autowired LastEventDateService lastEventDateService;


    @Autowired
    private S3Service s3service;

    @Transactional
    public JasperPrint generate(EventReportCriteria criteria, List<Long> sortedIdList) throws ReportException  {
        Locale locale = ThreadLocalInteractionContext.getInstance().getUserThreadLanguage();
        File jasperFile = PathHandler.getCompiledSummaryReportFile(getCurrentTenant(), locale);

        //We will no longer compile the jaspers through FieldiD.  The assumption is that the jasper file will exist on S3.
        /*
        try {
            new ReportCompiler().compileReports(jasperFile.getParentFile());
        } catch (JRException e) {
            throw new ReportException(e);
        }
        */

        // check to see if the report exists
        if (!jasperFile.canRead()) {
            throw new ReportException("Could not access Jasper File " + jasperFile);
        }

        Map<String, Object> reportMap = criteriaMap(criteria, getCurrentUser().getOwner().getPrimaryOrg(), jasperFile);
        List<Map<String, Object>> collection = new ArrayList<Map<String, Object>>();

        addImageStreams(reportMap, getCurrentUser().getOwner().getInternalOrg());

        DateTimeDefiner dateDefiner = new DateTimeDefiner(getCurrentUser());

        try {
            Integer totalNumEvents = 0;
            Integer totalPassedEvents = 0;
            Integer totalFailedEvents = 0;
            Integer totalNAEvents = 0;

            ThingEvent event;
            for (Long eventScheduleId : sortedIdList) {

                event = persistenceService.find(ThingEvent.class, eventScheduleId);

                Map<String, Object> eventMap = new HashMap<String, Object>();
                eventMap.put("date", event.getDate());
                eventMap.put("referenceNumber", event.getAsset().getCustomerRefNumber());
                eventMap.put("productType", event.getAsset().getType().getName());
                eventMap.put("serialNumber", event.getAsset().getIdentifier());
                eventMap.put("description", event.getAsset().getDescription());
                eventMap.put("organization", event.getOwner().getInternalOrg().getName());
                eventMap.put("inspectionType", event.getType().getName());
                eventMap.put("dateFormat", new DateTimeDefiner(getCurrentUser()).getDateFormat());
                eventMap.put("performedBy", event.getPerformedBy().getUserLabel());
                eventMap.put("result", event.getEventResult().getDisplayName());
                eventMap.put("division", (event.getOwner().isDivision()) ? event.getOwner().getName() : null);

                PrimaryOrg tenant = orgService.getPrimaryOrgForTenant(getCurrentTenant().getId());

                eventMap.put("tenantAddress", tenant.getAddressInfo() !=null? tenant.getAddressInfo().getDisplay() : "");

                eventMap.put("productTypeGroup", event.getAsset().getType().getGroup() != null ? event.getAsset().getType().getGroup().getName() : "");
                eventMap.put("assetComments", event.getAsset().getComments());
                eventMap.put("customer", event.getOwner().getCustomerOrg() != null? event.getOwner().getCustomerOrg().getName() :"");

                eventMap.put("orderNumber", getOrderNumber(event));
                eventMap.put("PONumber", event.getAsset().getPurchaseOrder());
                eventMap.put("attributes", produceInfoOptionLP(event.getAsset().getOrderedInfoOptionList()));
                eventMap.put("productStatus", event.getAssetStatus() !=null ? event.getAssetStatus().getName() : "");

                eventMap.put("eventTypeGroup", event.getType().getGroup() !=null ? event.getType().getGroup().getName() : "");
                eventMap.put("location", event.getAdvancedLocation() != null ? event.getAdvancedLocation().getFullName() : "");
                eventMap.put("eventComments", event.getComments());
                eventMap.put("assignedTo", event.getAssignedTo() != null && event.getAssignedTo().getAssignedUser() != null
                        ? event.getAssignedTo().getAssignedUser().getDisplayName() : "");


                Map<String, Object> eventReportMap = new EventReportMapProducer(event, dateDefiner, s3service, eventService, lastEventDateService).produceMap();
                eventMap.put("mainInspection", eventReportMap);
                eventMap.put("product", eventReportMap.get("product"));

                List<Map<String, Object>> inspectionResultMaps = new ArrayList<Map<String, Object>>();
                inspectionResultMaps.add(eventReportMap);



                for (SubEvent subEvent : event.getSubEvents()) {
                    inspectionResultMaps.add(new SubEventReportMapProducer(subEvent, event, dateDefiner, s3service, lastEventDateService).produceMap());
                }

                eventMap.put("allInspections", inspectionResultMaps);

                ThingEventProofTest thingEventProofTest = event.getProofTestInfo();
                if (thingEventProofTest != null) {
                    eventMap.put("peakLoad", thingEventProofTest.getPeakLoad());
                }

                collection.add(eventMap);

                totalNumEvents++;
                if (event.getEventResult().equals(EventResult.PASS)) {
                    totalPassedEvents++;
                }
                if (event.getEventResult().equals(EventResult.FAIL)) {
                    totalFailedEvents++;
                }
                if (event.getEventResult().equals(EventResult.NA)) {
                    totalNAEvents++;
                }
            }

            reportMap.put("totalNumEvents", totalNumEvents);
            reportMap.put("totalPassedEvents", totalPassedEvents);
            reportMap.put("totalFailedEvents", totalFailedEvents);
            reportMap.put("totalNAEvents", totalNAEvents);

        } catch (Exception e) {
            throw new ReportException("Failed to load the criteria", e);
        }

        JasperPrint jasperPrint = null;
        try {
            JRBeanCollectionDataSource jrDataSource = new JRBeanCollectionDataSource(collection);
            JasperReport jasperReport = (JasperReport) JRLoader.loadObject(PathHandler.getCompiledSummaryReportFile(getCurrentTenant(), locale));
            jasperPrint = JasperFillManager.fillReport(jasperReport, reportMap, jrDataSource);

        } catch (JRException e) {
            throw new ReportException("Failed to generate report", e);
        }

        return jasperPrint;
    }

    private String getOrderNumber(ThingEvent event) {
        if (event.getAsset().getShopOrder() != null) {
            return event.getAsset().getShopOrder().getOrder().getOrderNumber();
        } else if(event.getAsset().getNonIntergrationOrderNumber() != null) {
            return event.getAsset().getNonIntergrationOrderNumber();
        } else {
            return "";
        }
    }

    // XXX - document me
    private Map<String, Object> criteriaMap(EventReportCriteria criteria, PrimaryOrg primaryOrg, File jasperFile) {
        Map<String, Object> reportMap = new HashMap<String, Object>();

        reportMap.put("SUBREPORT_DIR", jasperFile.getParent() + "/");
        reportMap.put("serialNumber", criteria.getIdentifier());
        reportMap.put("rfidNumber", criteria.getRfidNumber());
        reportMap.put("orderNumber", criteria.getOrderNumber());
        reportMap.put("purchaseOrder", criteria.getPurchaseOrder());
        reportMap.put("toDate", dateService.calculateFromDate(criteria.getDateRange()));
        reportMap.put("fromDate", dateService.calculateToDate(criteria.getDateRange()));
        reportMap.put("hasIntegration", primaryOrg.hasExtendedFeature(ExtendedFeature.Integration));

        if (criteria.getAssetType() != null) {
            reportMap.put("assetType", criteria.getAssetType().getName());
        }

        if (criteria.getEventBook() != null) {
            if (criteria.getEventBook().getId() != 0L) {
                reportMap.put("inspectionBook", criteria.getEventBook().getName());
            } else {
                reportMap.put("inspectionBook", "no event book");
            }
        }

        if (criteria.getEventTypeGroup() != null) {
            reportMap.put("inspectionTypeGroup", criteria.getEventTypeGroup().getName());
        }

        if (criteria.getPerformedBy() != null) {
            reportMap.put("performedBy", criteria.getPerformedBy().getUserLabel());
        }

        if (criteria.getOwner() != null) {
            reportMap.put("customer", (criteria.getOwner().getCustomerOrg() != null) ? criteria.getOwner().getCustomerOrg().getName() : "");
            reportMap.put("division", (criteria.getOwner().getDivisionOrg() != null) ? criteria.getOwner().getDivisionOrg().getName() : "");
        }

        return reportMap;
    }

    private void addImageStreams(Map<String, Object> params, InternalOrg org) throws ReportException {
        InputStream logoImage = resolveCertificateMainLogo(org);
        InputStream n4LogoImage = getImageFileStream(PathHandler.getCommonImageFile(n4LogoFileName));

        params.put("n4LogoImage", n4LogoImage);
        params.put("logoImage", logoImage);
    }

    /**
     * Opens an InputStream for a file.
     *
     * @param imageFile
     *            The file to open
     * @return An InputStream for this file or null if the file could not be
     *         found.
     */
    private InputStream getImageFileStream(File imageFile) {
        InputStream imageStream = null;

        if (imageFile != null) {
            try {
                imageStream = new FileInputStream(imageFile);
            } catch (FileNotFoundException e) {
                logger.warn("Cannot open stream for image file", e);
            }
        }

        return imageStream;
    }

    private InputStream resolveCertificateMainLogo(InternalOrg organization) throws ReportException {
        try {
            byte[] image = s3service.downloadInternalOrgCertificateLogo(organization);
            return image != null ? new ByteArrayInputStream(image) : null;
        } catch (IOException e) {
            logger.warn("Failed downloading internal org certificate logo", e);
            return null;
        }
    }

    private List<StringListingPair> produceInfoOptionLP(List<InfoOptionBean> infoOptionList) {
        List<StringListingPair> infoOptions = new ArrayList<StringListingPair>(infoOptionList.size());
        for (InfoOptionBean option : infoOptionList) {
            infoOptions.add(new StringListingPair(option.getInfoField().getName().toLowerCase().replaceAll("\\s", ""), option.getName()));
        }
        return infoOptions;
    }

}
