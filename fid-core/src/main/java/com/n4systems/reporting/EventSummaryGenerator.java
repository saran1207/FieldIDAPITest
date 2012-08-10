package com.n4systems.reporting;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.n4systems.fieldid.service.amazon.S3Service;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.util.JRLoader;

import org.apache.log4j.Logger;

import rfid.ejb.entity.InfoOptionBean;

import com.n4systems.ejb.EventManager;
import com.n4systems.ejb.PersistenceManager;
import com.n4systems.exceptions.ReportException;
import com.n4systems.fieldid.service.certificate.ReportCompiler;
import com.n4systems.model.AssetType;
import com.n4systems.model.Event;
import com.n4systems.model.EventBook;
import com.n4systems.model.EventTypeGroup;
import com.n4systems.model.ExtendedFeature;
import com.n4systems.model.Status;
import com.n4systems.model.SubEvent;
import com.n4systems.model.assettype.AssetTypeLoader;
import com.n4systems.model.orgs.InternalOrg;
import com.n4systems.model.orgs.PrimaryOrg;
import com.n4systems.model.user.User;
import com.n4systems.model.utils.DateTimeDefiner;
import com.n4systems.persistence.loaders.LoaderFactory;
import com.n4systems.util.ServiceLocator;
import com.n4systems.util.StringListingPair;

public class EventSummaryGenerator {
	private static final String n4LogoFileName = "n4_logo.gif";
	private Logger logger = Logger.getLogger(EventSummaryGenerator.class);
	
	private final PersistenceManager persistenceManager;
	private final EventManager eventManager;
	private final DateTimeDefiner dateDefiner;
    private final S3Service s3service;

	public EventSummaryGenerator(DateTimeDefiner dateDefiner, PersistenceManager persistenceManager, EventManager eventManager, S3Service s3service) {
		this.dateDefiner = dateDefiner;
		this.persistenceManager = persistenceManager;
		this.eventManager = eventManager;
        this.s3service = s3service;
	}
	
	public EventSummaryGenerator(DateTimeDefiner dateDefiner) {
		this(dateDefiner, ServiceLocator.getPersistenceManager(), ServiceLocator.getEventManager(), ServiceLocator.getS3Service());
	}
	
	public JasperPrint generate(ReportDefiner reportDefiner, List<Long> eventIds, User user) throws ReportException {
		File jasperFile = PathHandler.getCompiledSummaryReportFile(user.getTenant());
		try {
			new ReportCompiler().compileReports(jasperFile.getParentFile());
		} catch (JRException e) {
			throw new ReportException(e);
		}
		
		// check to see if the report exists
		if (!jasperFile.canRead()) {
			throw new ReportException("Could not access Jasper File " + jasperFile);
		}

		Map<String, Object> reportMap = criteriaMap(reportDefiner, user.getOwner().getPrimaryOrg(), jasperFile);
		List<Map<String, Object>> collection = new ArrayList<Map<String, Object>>();

		addImageStreams(reportMap, user.getOwner().getInternalOrg());

		try {
			Integer totalNumEvents = 0;
			Integer totalPassedEvents = 0;
			Integer totalFailedEvents = 0;
			Integer totalNAEvents = 0;
			
			Event event;
			for (Long inspectionId : eventIds) {
				
				event = eventManager.findAllFields(inspectionId, user.getSecurityFilter());

				Map<String, Object> eventMap = new HashMap<String, Object>();
				eventMap.put("date", event.getDate());
				eventMap.put("referenceNumber", event.getAsset().getCustomerRefNumber());
				eventMap.put("productType", event.getAsset().getType().getName());
				eventMap.put("serialNumber", event.getAsset().getIdentifier());
				eventMap.put("description", event.getAsset().getDescription());
				eventMap.put("organization", event.getOwner().getInternalOrg().getName());
				eventMap.put("inspectionType", event.getType().getName());
				eventMap.put("dateFormat", new DateTimeDefiner(user).getDateFormat());
				eventMap.put("performedBy", event.getPerformedBy().getUserLabel());
				eventMap.put("result", event.getStatus().getDisplayName());
				eventMap.put("division", (event.getOwner().isDivision()) ? event.getOwner().getName() : null);
				
				Long tenantId = event.getTenant().getId();
				
				PrimaryOrg tenant = getTenant(user, tenantId);
				
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

				
				Map<String, Object> eventReportMap = new EventReportMapProducer(event, dateDefiner, s3service).produceMap();
				eventMap.put("mainInspection", eventReportMap);
				eventMap.put("product", eventReportMap.get("product"));
				
				List<Map<String, Object>> inspectionResultMaps = new ArrayList<Map<String, Object>>();
				inspectionResultMaps.add(eventReportMap);
				
				for (SubEvent subEvent : event.getSubEvents()) {
					inspectionResultMaps.add(new SubEventReportMapProducer(subEvent, event, dateDefiner, s3service).produceMap());
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
			JasperReport jasperReport = (JasperReport) JRLoader.loadObject(PathHandler.getCompiledSummaryReportFile(user.getTenant()));
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

	protected PrimaryOrg getTenant(User user, Long tenantId) {
		return new LoaderFactory(user.getSecurityFilter()).createPrimaryOrgByTenantLoader().setTenantId(tenantId).load();
	}

	// XXX - document me
	private Map<String, Object> criteriaMap(ReportDefiner reportDefiner, PrimaryOrg primaryOrg, File jasperFile) {
		Map<String, Object> reportMap = new HashMap<String, Object>();

		reportMap.put("SUBREPORT_DIR", jasperFile.getParent() + "/");
		reportMap.put("serialNumber", reportDefiner.getIdentifier());
		reportMap.put("rfidNumber", reportDefiner.getRfidNumber());
		reportMap.put("orderNumber", reportDefiner.getOrderNumber());
		reportMap.put("purchaseOrder", reportDefiner.getPurchaseOrder());
		reportMap.put("toDate", reportDefiner.getToDate());
		reportMap.put("fromDate", reportDefiner.getFromDate());
		reportMap.put("hasIntegration", primaryOrg.hasExtendedFeature(ExtendedFeature.Integration));

		if (reportDefiner.getAssetType() != null) {
			AssetType assetType = new AssetTypeLoader(primaryOrg.getTenant()).setId(reportDefiner.getAssetType()).load();
			reportMap.put("assetType", assetType.getName());
		}

		if (reportDefiner.getEventBook() != null) {
			if (reportDefiner.getEventBook() != 0L) {
			reportMap.put("inspectionBook", persistenceManager.find(EventBook.class, reportDefiner.getEventBook()).getName());
			} else {
				reportMap.put("inspectionBook", "no inspection book");
			}
		}

		if (reportDefiner.getEventTypeGroup() != null) {
			reportMap.put("inspectionTypeGroup", persistenceManager.find(EventTypeGroup.class, reportDefiner.getEventTypeGroup()).getName());
		}

		if (reportDefiner.getPerformedBy() != null) {
			reportMap.put("performedBy", persistenceManager.find(User.class, reportDefiner.getPerformedBy()).getUserLabel());
		}
		
		if (reportDefiner.getOwner() != null) {
			reportMap.put("customer", (reportDefiner.getOwner().getCustomerOrg() != null) ? reportDefiner.getOwner().getCustomerOrg().getName() : "");
			reportMap.put("division", (reportDefiner.getOwner().getDivisionOrg() != null) ? reportDefiner.getOwner().getDivisionOrg().getName() : "");
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
            return new ByteArrayInputStream(image);
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
