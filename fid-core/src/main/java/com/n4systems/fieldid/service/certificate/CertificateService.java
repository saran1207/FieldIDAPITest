package com.n4systems.fieldid.service.certificate;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.data.JRMapCollectionDataSource;
import net.sf.jasperreports.engine.util.JRLoader;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.n4systems.exceptions.NonPrintableEventType;
import com.n4systems.exceptions.NonPrintableManufacturerCert;
import com.n4systems.exceptions.ReportException;
import com.n4systems.fieldid.certificate.model.Job;
import com.n4systems.fieldid.service.FieldIdPersistenceService;
import com.n4systems.fieldid.service.event.EventScheduleService;
import com.n4systems.fieldid.service.event.EventService;
import com.n4systems.model.AddressInfo;
import com.n4systems.model.Asset;
import com.n4systems.model.AssetStatus;
import com.n4systems.model.AssetType;
import com.n4systems.model.Contact;
import com.n4systems.model.Event;
import com.n4systems.model.EventSchedule;
import com.n4systems.model.EventTypeGroup;
import com.n4systems.model.LineItem;
import com.n4systems.model.PrintOut;
import com.n4systems.model.Project;
import com.n4systems.model.ProofTestInfo;
import com.n4systems.model.SubEvent;
import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.model.orgs.CustomerOrg;
import com.n4systems.model.orgs.DivisionOrg;
import com.n4systems.model.orgs.InternalOrg;
import com.n4systems.model.orgs.PrimaryOrg;
import com.n4systems.model.user.User;
import com.n4systems.model.utils.DateTimeDefiner;
import com.n4systems.reporting.AssetReportMapProducer;
import com.n4systems.reporting.EventReportMapProducer;
import com.n4systems.reporting.EventReportType;
import com.n4systems.reporting.PathHandler;
import com.n4systems.reporting.SubEventReportMapProducer;
import com.n4systems.util.DateHelper;
import com.n4systems.util.StreamHelper;

@Transactional(readOnly = true)
public class CertificateService extends FieldIdPersistenceService {
	
	private final ReportCompiler reportCompiler = new ReportCompiler();
	private final CertificatePrinter printer = new CertificatePrinter();
	
	@Autowired private EventScheduleService eventScheduleService;
	@Autowired private EventService eventService;
	
	public byte[] generateAssetCertificatePdf(Long assetId) throws ReportException, NonPrintableManufacturerCert {
		return printer.printToPDF(generateAssetCertificate(assetId));
	}
	
	public JasperPrint generateAssetCertificate(Long assetId) throws ReportException, NonPrintableManufacturerCert {
		Asset asset = persistenceService.find(Asset.class, assetId);
		
		if (!asset.getType().isHasManufactureCertificate()) {
			throw new NonPrintableManufacturerCert();
		}
		
		try {	
			File jasperFile = PathHandler.getReportFile(asset);
			reportCompiler.compileReports(jasperFile.getParentFile());
			
			Map<String, Object> reportMap = new HashMap<String, Object>();
			reportMap.put("SUBREPORT_DIR", jasperFile.getParent() + "/");
	
			addIdentifiedByParams(reportMap, asset.getIdentifiedBy());
			reportMap.putAll(new AssetReportMapProducer(asset, new DateTimeDefiner(getCurrentUser())).produceMap());
			addAssetTypeParams(reportMap, asset.getType());
			addShopOrderParams(reportMap, asset);
			addOrganizationParams(reportMap, asset.getOwner().getInternalOrg());
			addOwnerParams(reportMap, asset.getOwner());
	
			List<Asset> reportCollection = new ArrayList<Asset>();
			reportCollection.add(asset);

			JasperReport jasperReport = (JasperReport) JRLoader.loadObject(jasperFile);
			JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, reportMap, new JRBeanCollectionDataSource(reportCollection));
			return jasperPrint;
		} catch (JRException e) {
			throw new ReportException("Failed to generate asset certificate", e);
		}
	}
	
	public byte[] generateEventCertificatePdf(EventReportType type, Long eventId) throws NonPrintableEventType, ReportException {
		return printer.printToPDF(generateEventCertificate(type, eventId));
	}

	public JasperPrint generateEventCertificate(EventReportType type, Long eventId) throws NonPrintableEventType, ReportException {
		Event event = eventService.getEventFromSafetyNetwork(eventId);
		
		DateTimeDefiner dateDefiner = new DateTimeDefiner(getCurrentUser());
		
		if (!event.isPrintableForReportType(type)) {
			throw new NonPrintableEventType(String.format("Event [%s] was not printable or did not have a PrintOut for EventReportType [%s]", event.getId(), type.getDisplayName()));
		}

		try {
			PrintOut printOutToPrint = event.getType().getGroup().getPrintOutForReportType(type);
			
			JasperPrint jPrint;
			if (printOutToPrint.isWithSubEvents()) {
				jPrint = generateEventCertificateWithSubEvents(event, printOutToPrint, dateDefiner);
			} else {
				jPrint = generateEventCertificateWithoutSubEvents(event, printOutToPrint, dateDefiner);
			}
			return jPrint;
		} catch (Exception e) {	
			throw new ReportException("Failed to generate event certificate", e);
		}
	}

	private JasperPrint generateEventCertificateWithSubEvents(Event event, PrintOut printOut, DateTimeDefiner dateDefiner) throws JRException {
		File jasperFile = PathHandler.getPrintOutFile(printOut);
		reportCompiler.compileReports(jasperFile.getParentFile());
		
		Map<String, Object> reportMap = new HashMap<String, Object>();
		reportMap.put("SUBREPORT_DIR", jasperFile.getParent() + "/");
		
		addPerformedByParams(reportMap, event.getPerformedBy());
		addEventTypeGroupParams(reportMap, event.getType().getGroup());
		addOwnerParams(reportMap, event.getOwner());
		addAssetStatusParams(reportMap, event.getAssetStatus());
		addNextEventScheduleParams(reportMap, event, dateDefiner);
		addEventScheduleParams(reportMap, event);

		Map<String, Object> masterEventReportMap = new EventReportMapProducer(event, dateDefiner).produceMap();
		reportMap.put("mainInspection", masterEventReportMap);
		reportMap.put("product", masterEventReportMap.get("product"));

		List<Map<String, ?>> eventResultMaps = new ArrayList<Map<String, ?>>();
		eventResultMaps.add(masterEventReportMap);

		for (SubEvent subEvent : event.getSubEvents()) {
			eventResultMaps.add(new SubEventReportMapProducer(subEvent, event, dateDefiner).produceMap());
		}
		reportMap.put("allInspections", eventResultMaps);

		JasperReport jasperReport = (JasperReport) JRLoader.loadObject(jasperFile);
		JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, reportMap, new JRMapCollectionDataSource(eventResultMaps));
		return jasperPrint;
	}

	private JasperPrint generateEventCertificateWithoutSubEvents(Event event, PrintOut printOut, DateTimeDefiner dateDefiner) throws NonPrintableEventType, ReportException, JRException {
		File jasperFile = PathHandler.getPrintOutFile(printOut);
		reportCompiler.compileReports(jasperFile.getParentFile());
		
		Map<String, Object> reportMap = new HashMap<String, Object>();
		reportMap.put("SUBREPORT_DIR", jasperFile.getParent() + "/");
		
		addPerformedByParams(reportMap, event.getPerformedBy());
		addEventTypeGroupParams(reportMap, event.getType().getGroup());
		addOwnerParams(reportMap, event.getOwner());
		addAssetStatusParams(reportMap, event.getAssetStatus());
		addNextEventScheduleParams(reportMap, event, dateDefiner);
		addEventScheduleParams(reportMap, event);
		addProofTestParams(reportMap, event);
		addAssetTypeParams(reportMap, event.getAsset().getType());
		addShopOrderParams(reportMap, event.getAsset());

		reportMap.putAll(new AssetReportMapProducer(event.getAsset(), dateDefiner).produceMap());

		Map<String, Object> eventMap = new EventReportMapProducer(event, dateDefiner).produceMap();
		reportMap.putAll(eventMap);

		JasperReport jasperReport = (JasperReport) JRLoader.loadObject(jasperFile);
		JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, reportMap, (JRDataSource) eventMap.get("results"));
		return jasperPrint;
	}

	private void addProofTestParams(Map<String, Object> reportMap, Event event) {
		reportMap.put("chartPath", PathHandler.getChartImageFile(event).getAbsolutePath());
		addProofTestInfoParams(reportMap, event.getProofTestInfo());
	}

	private void addEventScheduleParams(Map<String, Object> reportMap, Event event) {
		if (event.getSchedule() != null) {
			Project job = event.getSchedule().getProject();
			if (job != null) {
				reportMap.put("job", new Job(job.getProjectID(), job.getName()));
			}
		}
	}

	private void addNextEventScheduleParams(Map<String, Object> reportMap, Event event, DateTimeDefiner dateDefiner) {
		EventSchedule schedule = eventScheduleService.getNextEventSchedule(event.getAsset().getId(), event.getType().getId());
		if (schedule != null) {
			reportMap.put("nextDate_date", schedule.getNextDate());
			reportMap.put("nextDate", DateHelper.format(schedule.getNextDate(), dateDefiner));
		}
	}

	private void addAssetStatusParams(Map<String, Object> reportMap, AssetStatus assetStatus) {
		if (assetStatus != null) {
			reportMap.put("productStatus", assetStatus.getName());
		}
	}

	private void addOwnerParams(Map<String, Object> reportMap, BaseOrg ownerOrg) {
		addPrimaryOrgParams(reportMap, ownerOrg.getPrimaryOrg());
		addCustomerOrgParams(reportMap, ownerOrg.getCustomerOrg());
		addDivisionOrgParams(reportMap, ownerOrg.getDivisionOrg());
	}
	
	private void addPrimaryOrgParams(Map<String, Object> reportMap, PrimaryOrg ownerPrimaryOrg) {
		reportMap.put("manName", ownerPrimaryOrg.getName());

		AddressInfo ownerPrimaryOrgAddress = ownerPrimaryOrg.getAddressInfo();
		if (ownerPrimaryOrgAddress != null) {
			reportMap.put("manAddress", ownerPrimaryOrgAddress.getDisplay());
		}
	}
	
	private void addCustomerOrgParams(Map<String, Object> reportMap, CustomerOrg customer) {
		if (customer != null) {
			reportMap.put("customerNumber", customer.getCode());
			reportMap.put("endUserName", customer.getName());

			Contact contact = customer.getContact();
			if (contact != null) {
				reportMap.put("customerContactName", contact.getName());
				reportMap.put("customerContactEmail", contact.getEmail());
			}
			
			AddressInfo addressInfo = customer.getAddressInfo();
			if (addressInfo != null) {
				reportMap.put("customerAddress", addressInfo.getStreetAddress());
				reportMap.put("customerCity", addressInfo.getCity());
				reportMap.put("customerState", addressInfo.getState());
				reportMap.put("customerPostalCode", addressInfo.getZip());
				reportMap.put("customerPhoneNumber", addressInfo.getPhone1());
				reportMap.put("customerPhoneNumber2", addressInfo.getPhone2());
				reportMap.put("customerFaxNumber", addressInfo.getFax1());
			}
		}
	}
	
	private void addDivisionOrgParams(Map<String, Object> reportMap, DivisionOrg division) {
		if (division != null) {
			reportMap.put("division", division.getName());
			reportMap.put("divisionID", division.getCode());

			Contact contact = division.getContact();
			if (contact != null) {
				reportMap.put("divisionContactName", contact.getName());
				reportMap.put("divisionContactEmail", contact.getEmail());
			}

			AddressInfo addressInfo = division.getAddressInfo();
			if (addressInfo != null) {
				reportMap.put("divisionAddress", addressInfo.getStreetAddress());
				reportMap.put("divisionCity", addressInfo.getCity());
				reportMap.put("divisionState", addressInfo.getState());
				reportMap.put("divisionPostalCode", addressInfo.getZip());
				reportMap.put("divisionPhoneNumber", addressInfo.getPhone1());
				reportMap.put("divisionPhoneNumber2", addressInfo.getPhone2());
				reportMap.put("divisionFaxNumber", addressInfo.getFax1());
			}
		}
	}

	private void addOrganizationParams(Map<String, Object> reportMap, InternalOrg ownerInternalOrg) {
		reportMap.put("organizationalPrintName", ownerInternalOrg.getCertificateName());
		reportMap.put("n4LogoImage", StreamHelper.openQuietly(PathHandler.getN4LogoImageFile()));
		reportMap.put("logoImage", StreamHelper.openQuietly(PathHandler.getCertificateLogo(ownerInternalOrg)));

		AddressInfo ownerInternalOrgAddress = ownerInternalOrg.getAddressInfo();
		if (ownerInternalOrgAddress != null) {
			reportMap.put("organizationalAddress", ownerInternalOrgAddress.getStreetAddress());
			reportMap.put("organizationalCity", ownerInternalOrgAddress.getCity());
			reportMap.put("organizationalState", ownerInternalOrgAddress.getState());
			reportMap.put("organizationalPostalCode", ownerInternalOrgAddress.getZip());
			reportMap.put("organizationalPhoneNumber", ownerInternalOrgAddress.getPhone1());
			reportMap.put("organizationalPhoneNumber2", ownerInternalOrgAddress.getPhone2());
			reportMap.put("organizationalFaxNumber", ownerInternalOrgAddress.getFax1());
		}
	}

	private void addEventTypeGroupParams(Map<String, Object> reportMap, EventTypeGroup eventTypeGroup) {
		reportMap.put("reportTitle", eventTypeGroup.getReportTitle());
	}

	private void addPerformedByParams(Map<String, Object> reportMap, User performedBy) {
		reportMap.put("inspectorName", performedBy.getUserLabel());
		reportMap.put("performByName", performedBy.getUserLabel());
		reportMap.put("position", performedBy.getPosition());
		reportMap.put("initials", performedBy.getInitials());
		reportMap.put("signatureImage", StreamHelper.openQuietly(PathHandler.getSignatureImage(performedBy)));
		
		addOrganizationParams(reportMap, performedBy.getOwner().getInternalOrg());
	}

	private void addIdentifiedByParams(Map<String, Object> reportMap, User identifiedBy) {
		if (identifiedBy != null) {
			reportMap.put("inspectorName", identifiedBy.getUserLabel());
			reportMap.put("identifiedBy", identifiedBy.getUserLabel());
			reportMap.put("position", identifiedBy.getPosition());
			reportMap.put("initials", identifiedBy.getInitials());
			reportMap.put("signatureImage", StreamHelper.openQuietly(PathHandler.getSignatureImage(identifiedBy)));
		}
	}
	
	private void addShopOrderParams(Map<String, Object> params, Asset asset) {
		LineItem shopOrder = asset.getShopOrder();
		if (shopOrder != null) {
			params.put("customerPartDescription", shopOrder.getDescription());
			params.put("productName", shopOrder.getAssetCode());
			params.put("orderNumber", shopOrder.getOrder().getOrderNumber());
		} else {
			params.put("orderNumber", asset.getNonIntergrationOrderNumber());
		}
	}

	private void addProofTestInfoParams(Map<String, Object> reportMap, ProofTestInfo proofTestInfo) {
		if (proofTestInfo != null) {
			reportMap.put("peakLoad", proofTestInfo.getPeakLoad());
			reportMap.put("testDuration", proofTestInfo.getDuration());
			reportMap.put("peakLoadDuration", proofTestInfo.getPeakLoadDuration());
		}
	}

	private void addAssetTypeParams(Map<String, Object> reportMap, AssetType assetType) {
		reportMap.put("productType", assetType.getName());
		reportMap.put("itemNumber", assetType.getName());
		reportMap.put("productWarning", assetType.getWarnings());
		reportMap.put("certificateText", assetType.getManufactureCertificateText());
	}
}
