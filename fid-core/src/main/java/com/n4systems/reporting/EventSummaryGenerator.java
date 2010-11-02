package com.n4systems.reporting;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

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
import com.n4systems.ejb.SearchPerformerWithReadOnlyTransactionManagement;
import com.n4systems.exceptions.ReportException;
import com.n4systems.model.AssetType;
import com.n4systems.model.ExtendedFeature;
import com.n4systems.model.Event;
import com.n4systems.model.EventBook;
import com.n4systems.model.EventTypeGroup;
import com.n4systems.model.LineItem;
import com.n4systems.model.Status;
import com.n4systems.model.SubEvent;
import com.n4systems.model.orgs.InternalOrg;
import com.n4systems.model.orgs.PrimaryOrg;
import com.n4systems.model.assettype.AssetTypeLoader;
import com.n4systems.model.user.User;
import com.n4systems.model.utils.DateTimeDefiner;
import com.n4systems.persistence.loaders.LoaderFactory;
import com.n4systems.util.ReportMap;
import com.n4systems.util.ServiceLocator;
import com.n4systems.util.StringListingPair;
import com.n4systems.util.persistence.search.ImmutableBaseSearchDefiner;

public class EventSummaryGenerator {
	private static final String n4LogoFileName = "n4_logo.gif";
	private Logger logger = Logger.getLogger(EventSummaryGenerator.class);
	
	private final PersistenceManager persistenceManager;
	private final EventManager eventManager;
	private final DateTimeDefiner dateDefiner;
	
	public EventSummaryGenerator(DateTimeDefiner dateDefiner, PersistenceManager persistenceManager, EventManager eventManager) {
		this.dateDefiner = dateDefiner;
		this.persistenceManager = persistenceManager;
		this.eventManager = eventManager;
	}
	
	public EventSummaryGenerator(DateTimeDefiner dateDefiner) {
		this(dateDefiner, ServiceLocator.getPersistenceManager(), ServiceLocator.getInspectionManager());
	}
	
	public JasperPrint generate(ReportDefiner reportDefiner, User user) throws ReportException {
		File jasperFile = PathHandler.getSummaryReportFile(user.getTenant());

		// check to see if the report exists
		if (!jasperFile.canRead()) {
			throw new ReportException("Could not access Jasper File " + jasperFile);
		}

		List<Long> inspectionIds = getSearchIds(reportDefiner, user);

		ReportMap<Object> reportMap = criteriaMap(reportDefiner, user.getOwner().getPrimaryOrg(), jasperFile);
		List<ReportMap<Object>> collection = new ArrayList<ReportMap<Object>>();

		addImageStreams(reportMap, user.getOwner().getInternalOrg());

		try {
			Integer totalNumEvents = 0;
			Integer totalPassedEvents = 0;
			Integer totalFailedEvents = 0;
			Integer totalNAEvents = 0;
			
			Event event;
			for (Long inspectionId : inspectionIds) {
				
				event = eventManager.findAllFields(inspectionId, user.getSecurityFilter());

				ReportMap<Object> inspectionMap = new ReportMap<Object>();
				inspectionMap.put("date", event.getDate());
				inspectionMap.put("referenceNumber", event.getAsset().getCustomerRefNumber());
				inspectionMap.put("productType", event.getAsset().getType().getName());
				inspectionMap.put("serialNumber", event.getAsset().getSerialNumber());
				inspectionMap.put("description", event.getAsset().getDescription());
				inspectionMap.put("organization", event.getOwner().getInternalOrg().getName());
				inspectionMap.put("inspectionType", event.getType().getName());
				inspectionMap.put("dateFormat", new DateTimeDefiner(user).getDateFormat());
				inspectionMap.put("performedBy", event.getPerformedBy().getUserLabel());
				inspectionMap.put("result", event.getStatus().getDisplayName());
				inspectionMap.put("division", (event.getOwner().isDivision()) ? event.getOwner().getName() : null);
				
				Long tenantId = event.getTenant().getId();
				
				PrimaryOrg tenant = getTenant(user, tenantId);
				
				inspectionMap.put("tenantAddress", tenant.getAddressInfo() !=null? tenant.getAddressInfo().getDisplay() : "");
				
				inspectionMap.put("productTypeGroup", event.getAsset().getType().getGroup() != null ? event.getAsset().getType().getGroup().getName() : "");
				inspectionMap.put("assetComments", event.getAsset().getComments());
				inspectionMap.put("customer", event.getOwner().getCustomerOrg() != null? event.getOwner().getCustomerOrg().getName() :"");
				
				LineItem shopOrder = event.getAsset().getShopOrder();
				inspectionMap.put("orderNumber", shopOrder !=null ? shopOrder.getOrder().getOrderNumber() : "");
				inspectionMap.put("PONumber", event.getAsset().getPurchaseOrder());
				inspectionMap.put("attributes", produceInfoOptionLP(event.getAsset().getOrderedInfoOptionList()));
				inspectionMap.put("productStatus", event.getAsset().getAssetStatus()!=null ? event.getAsset().getAssetStatus().getName() : "");
				
				inspectionMap.put("eventTypeGroup", event.getType().getGroup() !=null ? event.getType().getGroup().getName() : "");
				inspectionMap.put("location", event.getAdvancedLocation() != null ? event.getAdvancedLocation().getFullName() : "");
				inspectionMap.put("eventComments", event.getComments());
				inspectionMap.put("assignedTo", event.getAssignedTo() != null && event.getAssignedTo().getAssignedUser() != null
						? event.getAssignedTo().getAssignedUser().getDisplayName() : "");

				
				ReportMap<Object> inspectionReportMap = new EventReportMapProducer(event, dateDefiner).produceMap();
				inspectionMap.put("mainInspection", inspectionReportMap);
				inspectionMap.put("product", inspectionReportMap.get("product"));
				
				List<ReportMap<Object>> inspectionResultMaps = new ArrayList<ReportMap<Object>>();
				inspectionResultMaps.add(inspectionReportMap);
				
				for (SubEvent subEvent : event.getSubEvents()) {
					inspectionResultMaps.add(new SubEventReportMapProducer(subEvent, event, dateDefiner).produceMap());
				}

				inspectionMap.put("allInspections", inspectionResultMaps);
				
				if (event.getProofTestInfo() != null) {
					inspectionMap.put("peakLoad", event.getProofTestInfo().getPeakLoad());
				}

				collection.add(inspectionMap);
				
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
			JasperReport jasperReport = (JasperReport) JRLoader.loadObject(jasperFile);
			jasperPrint = JasperFillManager.fillReport(jasperReport, reportMap, jrDataSource);

		} catch (JRException e) {
			throw new ReportException("Failed to generate report", e);
		}

		return jasperPrint;
	}

	protected PrimaryOrg getTenant(User user, Long tenantId) {
		return new LoaderFactory(user.getSecurityFilter()).createPrimaryOrgByTenantLoader().setTenantId(tenantId).load();
	}

	protected List<Long> getSearchIds(ReportDefiner reportDefiner, User user) {
		return new SearchPerformerWithReadOnlyTransactionManagement().idSearch(new ImmutableBaseSearchDefiner(reportDefiner), user.getSecurityFilter());
	}

	// XXX - document me
	private ReportMap<Object> criteriaMap(ReportDefiner reportDefiner, PrimaryOrg primaryOrg, File jasperFile) {
		ReportMap<Object> reportMap = new ReportMap<Object>();

		reportMap.put("SUBREPORT_DIR", jasperFile.getParent() + "/");
		reportMap.put("serialNumber", reportDefiner.getSerialNumber());
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

		if (reportDefiner.getInspectionBook() != null) { 
			if (reportDefiner.getInspectionBook() != 0L) {
			reportMap.put("inspectionBook", persistenceManager.find(EventBook.class, reportDefiner.getInspectionBook()).getName());
			} else {
				reportMap.put("inspectionBook", "no inspection book");
			}
		}

		if (reportDefiner.getInspectionTypeGroup() != null) {
			reportMap.put("inspectionTypeGroup", persistenceManager.find(EventTypeGroup.class, reportDefiner.getInspectionTypeGroup()).getName());
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

	private void addImageStreams(ReportMap<Object> params, InternalOrg org) throws ReportException {
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
