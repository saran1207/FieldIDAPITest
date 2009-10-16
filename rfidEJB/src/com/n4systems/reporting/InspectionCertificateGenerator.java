package com.n4systems.reporting;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRMapCollectionDataSource;
import net.sf.jasperreports.engine.util.JRLoader;

import org.apache.log4j.Logger;

import rfid.ejb.entity.UserBean;

import com.n4systems.exceptions.NonPrintableEventType;
import com.n4systems.exceptions.ReportException;
import com.n4systems.model.AbstractInspection;
import com.n4systems.model.AddressInfo;
import com.n4systems.model.Inspection;
import com.n4systems.model.InspectionSchedule;
import com.n4systems.model.InspectionTypeGroup;
import com.n4systems.model.LineItem;
import com.n4systems.model.PrintOut;
import com.n4systems.model.ProductType;
import com.n4systems.model.SubInspection;
import com.n4systems.model.inspectionschedule.NextInspectionScheduleLoader;
import com.n4systems.model.orgs.CustomerOrg;
import com.n4systems.model.orgs.DivisionOrg;
import com.n4systems.model.orgs.InternalOrg;
import com.n4systems.model.orgs.PrimaryOrg;
import com.n4systems.model.utils.DateTimeDefiner;
import com.n4systems.model.utils.PlainDate;
import com.n4systems.persistence.Transaction;
import com.n4systems.util.DateTimeDefinition;
import com.n4systems.util.FieldidDateFormatter;
import com.n4systems.util.ReportMap;

public class InspectionCertificateGenerator {
	private Logger logger = Logger.getLogger(InspectionCertificateGenerator.class);
	private static final String n4LogoFileName = "n4_logo.gif";
	
	private final NextInspectionScheduleLoader nextInspectionScheduleLoader;
	
	public InspectionCertificateGenerator(NextInspectionScheduleLoader nextInspectionScheduleLoader) {
		this.nextInspectionScheduleLoader = nextInspectionScheduleLoader;
	}
	
	public InspectionCertificateGenerator() {
		this(new NextInspectionScheduleLoader());
	}
	
	public JasperPrint generate(InspectionReportType type, Inspection inspection, UserBean user, Transaction transaction) throws NonPrintableEventType, ReportException {
		JasperPrint jPrint = null;
		
		PrintOut printOutToPrint = null;
		
		switch(type) {
			case INSPECTION_CERT:
				printOutToPrint = inspection.getType().getGroup().getPrintOut();
				break;
			case OBSERVATION_CERT:
				printOutToPrint = inspection.getType().getGroup().getObservationPrintOut();
				break;
		}
		
		if (printOutToPrint == null) {
			throw new NonPrintableEventType("Event type group [" + inspection.getType().getGroup().getDisplayName() + "] does not have a printout of type " + type.getDisplayName());
		}
		
		
		
		
		if (printOutToPrint.isWithSubInspections()) {
			jPrint = generateFull(inspection, user, printOutToPrint);
		} else {
			jPrint = generate(inspection, user, transaction, printOutToPrint);
		}
	
		
		
		return jPrint;
	}

	private JasperPrint generateFull(Inspection inspection, UserBean user, PrintOut printOut) throws NonPrintableEventType, ReportException {
		// If the inspection is not printable, stop immediately
		if (!inspection.isPrintable()) {
			throw new NonPrintableEventType();
		}

		File jasperFile = PathHandler.getPrintOutFile(printOut);

		// check to see if the report exists
		if (!jasperFile.canRead()) {
			throw new ReportException("No report file for tenant [" + inspection.getTenant().getName() + "] and Inspection Type Group [" + inspection.getType().getGroup().getName() + "] found at ["
					+ jasperFile.getAbsolutePath() + "]");
		}

		JasperPrint jasperPrint = null;
		try {
			ReportMap<Object> inspectionReportMap = new InspectionReportMapProducer(inspection, new DateTimeDefiner(user)).produceMap();

			ReportMap<Object> reportMap = new ReportMap<Object>();
			reportMap.put("mainInspection", inspectionReportMap);
			reportMap.put("product", inspectionReportMap.get("product"));
			addInspectionTypeGroupParams(reportMap, inspection.getType().getGroup());
			reportMap.put("SUBREPORT_DIR", jasperFile.getParent() + "/");
			addImageStreams(reportMap, inspection.getInspector().getOwner().getInternalOrg());
			addTenantParams(reportMap, inspection.getOwner().getPrimaryOrg());
			addUserParams(reportMap, inspection.getInspector());
			addOrganizationParams(reportMap, inspection.getInspector().getOwner().getInternalOrg());
			addCustomerParams(reportMap, inspection.getOwner().getCustomerOrg());
			addDivisionParams(reportMap, inspection.getOwner().getDivisionOrg());
			addInspectionScheduleParams(reportMap, nextInspectionScheduleLoader.setFieldsFromInspection(inspection).load(), user);

			List<ReportMap<Object>> inspectionResultMaps = new ArrayList<ReportMap<Object>>();
			inspectionResultMaps.add(inspectionReportMap);
			for (SubInspection subInspection : inspection.getSubInspections()) {
				inspectionResultMaps.add(new InspectionReportMapProducer(subInspection, new DateTimeDefiner(user)).produceMap());
			}

			JRDataSource jrDataSource = new JRMapCollectionDataSource(inspectionResultMaps);
			JasperReport jasperReport = (JasperReport) JRLoader.loadObject(jasperFile);
			jasperPrint = JasperFillManager.fillReport(jasperReport, reportMap, jrDataSource);
		} catch (Exception e) {
			throw new ReportException("Failed to generate report", e);
		}

		return jasperPrint;
	}

	private JasperPrint generate(Inspection inspection, UserBean user, Transaction transaction, PrintOut printOut) throws NonPrintableEventType, ReportException {
		// If the inspection is not printable, stop immediately
		if (!inspection.isPrintable()) {
			throw new NonPrintableEventType();
		}

		File jasperFile = PathHandler.getPrintOutFile(printOut);

		// check to see if the report exists
		if (!jasperFile.canRead()) {
			throw new ReportException("No report file for tenant [" + inspection.getTenant().getName() + "] and Inspection Type Group [" + inspection.getType().getGroup().getName() + "] found at ["
					+ jasperFile.getAbsolutePath() + "]");
		}
		ReportMap<Object> reportMap = createInspectionReportMap(inspection, user, jasperFile, transaction);

		JasperPrint jasperPrint = null;
		try {

			JRDataSource jrDataSource = (JRDataSource) new InspectionReportMapProducer(inspection, new DateTimeDefiner(user)).produceMap().get("results");
			JasperReport jasperReport = (JasperReport) JRLoader.loadObject(jasperFile);
			jasperPrint = JasperFillManager.fillReport(jasperReport, reportMap, jrDataSource);
		} catch (Exception e) {
			throw new ReportException("Failed to generate report", e);
		}

		return jasperPrint;
	}
	
	private ReportMap<Object> createInspectionReportMap(Inspection inspection, UserBean user, File jasperFile, Transaction transaction) throws ReportException {
		ReportMap<Object> reportMap = createAbstractInspectionReportMap(inspection, user);

		reportMap.put("SUBREPORT_DIR", jasperFile.getParent() + "/");
		addImageStreams(reportMap, inspection.getInspector().getOwner().getInternalOrg());
		addTenantParams(reportMap, inspection.getOwner().getPrimaryOrg());
		addOrganizationParams(reportMap, inspection.getOwner().getInternalOrg());
		addUserParams(reportMap, inspection.getInspector());
		addCustomerParams(reportMap, inspection.getOwner().getCustomerOrg());
		addDivisionParams(reportMap, inspection.getOwner().getDivisionOrg());
		addProofTestInfoParams(reportMap, inspection);

		addInspectionScheduleParams(reportMap, nextInspectionScheduleLoader.setFieldsFromInspection(inspection).load(transaction), user);

		return reportMap;
	}

	private ReportMap<Object> createAbstractInspectionReportMap(AbstractInspection inspection, UserBean user) throws ReportException {
		ReportMap<Object> reportMap = new ReportMap<Object>();

		reportMap.putAll(new ProductReportMapProducer(inspection.getProduct(), new DateTimeDefiner(user)).produceMap());
		addProductTypeParams(reportMap, inspection.getProduct().getType());
		addOrderParams(reportMap, inspection.getProduct().getShopOrder());
		reportMap.putAll(new InspectionReportMapProducer(inspection, new DateTimeDefiner(user)).produceMap());
		addInspectionTypeGroupParams(reportMap, inspection.getType().getGroup());

		return reportMap;

	}

	private void addImageStreams(ReportMap<Object> params, InternalOrg org) throws ReportException {
		InputStream logoImage = resolveCertificateMainLogo(org);
		InputStream n4LogoImage = getImageFileStream(PathHandler.getCommonImageFile(n4LogoFileName));

		params.put("n4LogoImage", n4LogoImage);
		params.put("logoImage", logoImage);
	}

	private void addTenantParams(ReportMap<Object> params, PrimaryOrg primaryOrg) {
		params.putEmpty("manName", "manAddress");

		params.put("manName", primaryOrg.getName());
		if (primaryOrg.getAddressInfo() != null) {
			params.put("manAddress", primaryOrg.getAddressInfo().getDisplay());
		}
	}

	private void addCustomerParams(ReportMap<Object> params, CustomerOrg customer) {
		params.putEmpty("customerNumber", "endUserName", "customerAddress", "customerCity", "customerState", "customerPostalCode", "customerPhoneNumber", "customerFaxNumber");

		if (customer != null) {
			params.put("customerNumber", customer.getCode());
			params.put("endUserName", customer.getName());

			AddressInfo addressInfo = customer.getAddressInfo();
			if (addressInfo != null) {
				params.put("customerAddress", addressInfo.getStreetAddress());
				params.put("customerCity", addressInfo.getCity());
				params.put("customerState", addressInfo.getState());
				params.put("customerPostalCode", addressInfo.getZip());
				params.put("customerPhoneNumber", addressInfo.getPhone1());
				params.put("customerPhoneNumber2", addressInfo.getPhone2());
				params.put("customerFaxNumber", addressInfo.getFax1());
			}
		}
	}

	private void addDivisionParams(ReportMap<Object> params, DivisionOrg division) {
		params.putEmpty("division");

		if (division != null) {
			params.put("division", division.getName());
			params.put("divisionID", division.getCode());

			if (division.getContact() != null) {
				params.put("divisionContactName", division.getContact().getName());
				params.put("divisionContactEmail", division.getContact().getEmail());
			}
			
			AddressInfo addressInfo = division.getAddressInfo();
			if (addressInfo != null) {
				params.put("divisionAddress", addressInfo.getStreetAddress());
				params.put("divisionCity", addressInfo.getCity());
				params.put("divisionState", addressInfo.getState());
				params.put("divisionPostalCode", addressInfo.getZip());
				params.put("divisionPhoneNumber", addressInfo.getPhone1());
				params.put("divisionPhoneNumber2", addressInfo.getPhone2());
				params.put("divisionFaxNumber", addressInfo.getFax1());
			}
		}
	}

	private void addOrderParams(ReportMap<Object> params, LineItem lineItem) {
		params.putEmpty("orderNumber", "customerPartDescription", "partNumber", "productName");

		if (lineItem != null) {
			params.put("orderNumber", lineItem.getOrder().getOrderNumber());
			params.put("customerPartDescription", lineItem.getDescription());
			params.put("productName", lineItem.getProductCode());
		}
	}

	private void addUserParams(ReportMap<Object> params, UserBean user) {
		params.putEmpty("inspectorName", "identifiedBy", "position", "initials");

		if (user != null) {
			params.put("identifiedBy", user.getUserLabel());
			params.put("inspectorName", user.getUserLabel());
			params.put("position", user.getPosition());
			params.put("initials", user.getInitials());

			File userImagePath = PathHandler.getSignatureImage(user);

			if (userImagePath.exists()) {
				try {
					params.put("signatureImage", new FileInputStream(userImagePath));
				} catch (FileNotFoundException fne) {
					// since we check to see that the file does exist first,
					// we'll just log these and move on, rather then throwing it
					// up the stack
					logger.warn("Unable to open user signature image stream", fne);
				}
			}
		}
	}

	private void addOrganizationParams(ReportMap<Object> params, InternalOrg org) {
		params.putEmpty("organizationalPrintName", "organizationalAddress", "organizationalCity", "organizationalState", "organizationalPostalCode", "organizationalPhoneNumber",
				"organizationalFaxNumber");

		if (org != null) {
			params.put("organizationalPrintName", org.getCertificateName());

			AddressInfo addressInfo = org.getAddressInfo();
			if (addressInfo != null) {
				params.put("organizationalAddress", addressInfo.getStreetAddress());
				params.put("organizationalCity", addressInfo.getCity());
				params.put("organizationalState", addressInfo.getState());
				params.put("organizationalPostalCode", addressInfo.getZip());
				params.put("organizationalPhoneNumber", addressInfo.getPhone1());
				params.put("organizationalPhoneNumber2", addressInfo.getPhone2());
				params.put("organizationalFaxNumber", addressInfo.getFax1());
			}
		}
	}

	private void addProductTypeParams(ReportMap<Object> params, ProductType productType) {
		params.put("productType", productType.getName());
		params.put("itemNumber", productType.getName());
		params.put("productWarning", productType.getWarnings());
		params.put("certificateText", productType.getManufactureCertificateText());
	}

	private void addProofTestInfoParams(ReportMap<Object> params, Inspection inspection) {
		params.putEmpty("proofTest", "peakLoad", "testDuration", "peakLoadDuration");

		if (inspection.getProofTestInfo() != null) {
			params.put("peakLoad", inspection.getProofTestInfo().getPeakLoad());
			params.put("testDuration", inspection.getProofTestInfo().getDuration());
			params.put("chartPath", PathHandler.getChartImageFile(inspection).getAbsolutePath());
			params.put("peakLoadDuration", inspection.getProofTestInfo().getPeakLoadDuration());
		}
	}

	private void addInspectionScheduleParams(ReportMap<Object> params, InspectionSchedule schedule, UserBean user) {
		params.putEmpty("nextDate");
		params.putEmpty("nextDate_date");
		if (schedule != null) {
			params.put("nextDate", formatDate(schedule.getNextDate(), new DateTimeDefiner(user), false));
			params.put("nextDate_date", schedule.getNextDate());
		}
	}

	private void addInspectionTypeGroupParams(ReportMap<Object> params, InspectionTypeGroup group) {
		params.put("reportTitle", group.getReportTitle());
	}


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
		InputStream logoStream = null;
		File tenantLogo = PathHandler.getCertificateLogo(organization);

		try {
			logoStream = new FileInputStream(tenantLogo);
			
		} catch (FileNotFoundException e) {
			throw new ReportException("Failed creating certificate logo input stream", e);
		}

		return logoStream;
	}


	private String formatDate(Date date, DateTimeDefinition dateTimeDefinition, boolean showTime) {
		if (date instanceof PlainDate) {
			return new FieldidDateFormatter(date, dateTimeDefinition, false, showTime).format();
		}
		return new FieldidDateFormatter(date, dateTimeDefinition, true, showTime).format();

	}
}
