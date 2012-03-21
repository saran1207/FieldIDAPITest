package com.n4systems.fieldid.service.event;

import com.n4systems.exceptions.ReportException;
import com.n4systems.fieldid.service.FieldIdPersistenceService;
import com.n4systems.fieldid.service.certificate.ReportCompiler;
import com.n4systems.fieldid.service.org.OrgService;
import com.n4systems.model.Event;
import com.n4systems.model.EventSchedule;
import com.n4systems.model.ExtendedFeature;
import com.n4systems.model.Status;
import com.n4systems.model.SubEvent;
import com.n4systems.model.orgs.InternalOrg;
import com.n4systems.model.orgs.PrimaryOrg;
import com.n4systems.model.search.EventReportCriteria;
import com.n4systems.model.utils.DateTimeDefiner;
import com.n4systems.reporting.EventReportMapProducer;
import com.n4systems.reporting.PathHandler;
import com.n4systems.reporting.SubEventReportMapProducer;
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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EventSummaryJasperGenerator extends FieldIdPersistenceService {

    private static final String n4LogoFileName = "n4_logo.gif";
    private Logger logger = Logger.getLogger(EventSummaryJasperGenerator.class);

    @Autowired
    private OrgService orgService;

    @Transactional
    public JasperPrint generate(EventReportCriteria criteria, List<Long> sortedIdList) throws ReportException {
        File jasperFile = PathHandler.getCompiledSummaryReportFile(getCurrentTenant());
        try {
            new ReportCompiler().compileReports(jasperFile.getParentFile());
        } catch (JRException e) {
            throw new ReportException(e);
        }

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

            Event event;
            for (Long eventScheduleId : sortedIdList) {

                event = persistenceService.find(EventSchedule.class, eventScheduleId).getEvent();

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
                eventMap.put("result", event.getStatus().getDisplayName());
                eventMap.put("division", (event.getOwner().isDivision()) ? event.getOwner().getName() : null);

                PrimaryOrg tenant = orgService.getPrimaryOrgForTenant(getCurrentTenant().getId());

                eventMap.put("tenantAddress", tenant.getAddressInfo() !=null? tenant.getAddressInfo().getDisplay() : "");

                eventMap.put("productTypeGroup", event.getAsset().getType().getGroup() != null ? event.getAsset().getType().getGroup().getName() : "");
                eventMap.put("assetComments", event.getAsset().getComments());
                eventMap.put("customer", event.getOwner().getCustomerOrg() != null? event.getOwner().getCustomerOrg().getName() :"");

                eventMap.put("orderNumber", getOrderNumber(event));
                eventMap.put("PONumber", event.getAsset().getPurchaseOrder());
                eventMap.put("attributes", produceInfoOptionLP(event.getAsset().getOrderedInfoOptionList()));
                eventMap.put("productStatus", event.getAsset().getAssetStatus()!=null ? event.getAsset().getAssetStatus().getName() : "");

                eventMap.put("eventTypeGroup", event.getType().getGroup() !=null ? event.getType().getGroup().getName() : "");
                eventMap.put("location", event.getAdvancedLocation() != null ? event.getAdvancedLocation().getFullName() : "");
                eventMap.put("eventComments", event.getComments());
                eventMap.put("assignedTo", event.getAssignedTo() != null && event.getAssignedTo().getAssignedUser() != null
                        ? event.getAssignedTo().getAssignedUser().getDisplayName() : "");


                Map<String, Object> eventReportMap = new EventReportMapProducer(event, dateDefiner).produceMap();
                eventMap.put("mainInspection", eventReportMap);
                eventMap.put("product", eventReportMap.get("product"));

                List<Map<String, Object>> inspectionResultMaps = new ArrayList<Map<String, Object>>();
                inspectionResultMaps.add(eventReportMap);

                for (SubEvent subEvent : event.getSubEvents()) {
                    inspectionResultMaps.add(new SubEventReportMapProducer(subEvent, event, dateDefiner).produceMap());
                }

                eventMap.put("allInspections", inspectionResultMaps);

                if (event.getProofTestInfo() != null) {
                    eventMap.put("peakLoad", event.getProofTestInfo().getPeakLoad());
                }

                collection.add(eventMap);

                totalNumEvents++;
                if (event.getStatus().equals(Status.PASS)) {
                    totalPassedEvents++;
                }
                if (event.getStatus().equals(Status.FAIL)) {
                    totalFailedEvents++;
                }
                if (event.getStatus().equals(Status.NA)) {
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
            JasperReport jasperReport = (JasperReport) JRLoader.loadObject(PathHandler.getCompiledSummaryReportFile(getCurrentTenant()));
            jasperPrint = JasperFillManager.fillReport(jasperReport, reportMap, jrDataSource);

        } catch (JRException e) {
            throw new ReportException("Failed to generate report", e);
        }

        return jasperPrint;
    }

    private String getOrderNumber(Event event) {
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
        reportMap.put("toDate", criteria.getDateRange().calculateFromDate());
        reportMap.put("fromDate", criteria.getDateRange().calculateToDate());
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

    /**
     * Returns an input stream of the logo for an Organization. If the
     * organization is not null and has a logo, that logo is returned. Failing
     * that, the tenants logo is returned. If the tenant has no logo, null is
     * returned.
     *
     * @param organization
     *            An Organization
     * @param tenant
     *            The Tenant
     * @return An InputStream or null if no logo could be resolved.
     * @throws ReportException
     */
    private InputStream resolveCertificateMainLogo(InternalOrg organization) throws ReportException {
        InputStream logoStream = null;
        File tenantLogo = PathHandler.getCertificateLogo(organization);

        try {
            logoStream = new FileInputStream(tenantLogo);
        } catch (FileNotFoundException e) {
            return null;
        }

        return logoStream;
    }

    private List<StringListingPair> produceInfoOptionLP(List<InfoOptionBean> infoOptionList) {
        List<StringListingPair> infoOptions = new ArrayList<StringListingPair>(infoOptionList.size());
        for (InfoOptionBean option : infoOptionList) {
            infoOptions.add(new StringListingPair(option.getInfoField().getName().toLowerCase().replaceAll("\\s", ""), option.getName()));
        }
        return infoOptions;
    }

}
