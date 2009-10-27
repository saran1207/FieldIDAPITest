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

import rfid.ejb.entity.UserBean;
import rfid.ejb.session.User;

import com.n4systems.ejb.PersistenceManager;
import com.n4systems.exceptions.ReportException;
import com.n4systems.model.ExtendedFeature;
import com.n4systems.model.Inspection;
import com.n4systems.model.InspectionBook;
import com.n4systems.model.InspectionTypeGroup;
import com.n4systems.model.ProductType;
import com.n4systems.model.Tenant;
import com.n4systems.model.orgs.InternalOrg;
import com.n4systems.model.orgs.PrimaryOrg;
import com.n4systems.model.producttype.ProductTypeLoader;
import com.n4systems.model.security.SecurityFilter;
import com.n4systems.model.utils.DateTimeDefiner;
import com.n4systems.persistence.EntityManagerTransactionWrapper;
import com.n4systems.util.ReportMap;

public class InspectionSummaryGenerator {
	private static final String n4LogoFileName = "n4_logo.gif";
	private Logger logger = Logger.getLogger(InspectionSummaryGenerator.class);
	
	private PersistenceManager persistenceManager;
	private User userManager;
	
	public InspectionSummaryGenerator() {}
	
	public JasperPrint generate(ReportDefiner reportDefiner, SecurityFilter filter, UserBean user, Tenant tenant) throws ReportException {
		File jasperFile = PathHandler.getSummaryReportFile(tenant);

		// check to see if the report exists
		if (!jasperFile.canRead()) {
			throw new ReportException("No Product report file summary report ");
		}

		List<Long> inspectionIds = persistenceManager.idSearch(reportDefiner, filter);

		ReportMap<Object> reportMap = criteriaMap(reportDefiner, user.getOwner().getPrimaryOrg(), jasperFile);
		List<ReportMap<Object>> collection = new ArrayList<ReportMap<Object>>();

		addImageStreams(reportMap, user.getOwner().getInternalOrg());

		try {
			Inspection inspection;
			for (Long inspectionId : inspectionIds) {
				inspection = persistenceManager.find(Inspection.class, inspectionId);

				ReportMap<Object> inspectionMap = new ReportMap<Object>();
				inspectionMap.put("date", inspection.getDate());
				inspectionMap.put("productType", inspection.getProduct().getType().getName());
				inspectionMap.put("serialNumber", inspection.getProduct().getSerialNumber());
				inspectionMap.put("description", inspection.getProduct().getDescription());
				inspectionMap.put("organization", inspection.getOwner().getInternalOrg().getName());
				inspectionMap.put("inspectionType", inspection.getType().getName());
				inspectionMap.put("dateFormat", new DateTimeDefiner(user).getDateFormat());
				inspectionMap.put("inspector", inspection.getInspector().getUserLabel());
				inspectionMap.put("result", inspection.getStatus().getDisplayName());
				inspectionMap.put("division", (inspection.getOwner().isDivision()) ? inspection.getOwner().getName() : null);
				
				if (inspection.getProofTestInfo() != null) {
					inspectionMap.put("peakLoad", inspection.getProofTestInfo().getPeakLoad());
				}

				collection.add(inspectionMap);
			}
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

		if (reportDefiner.getProductType() != null) {
			ProductType productType = new ProductTypeLoader(primaryOrg.getTenant()).setId(reportDefiner.getProductType()).load(new EntityManagerTransactionWrapper(persistenceManager.getEntityManager()));
			reportMap.put("productType", productType.getName());
		}

		if (reportDefiner.getInspectionBook() != null) { 
			if (reportDefiner.getInspectionBook() != 0L) {
			reportMap.put("inspectionBook", persistenceManager.find(InspectionBook.class, reportDefiner.getInspectionBook()).getName());
			} else {
				reportMap.put("inspectionBook", "no inspection book");
			}
		}

		if (reportDefiner.getInspectionTypeGroup() != null) {
			reportMap.put("inspectionTypeGroup", persistenceManager.find(InspectionTypeGroup.class, reportDefiner.getInspectionTypeGroup()).getName());
		}

		if (reportDefiner.getInspector() != null) {
			reportMap.put("inspector", userManager.findUserBean(reportDefiner.getInspector()).getUserLabel());
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
}
