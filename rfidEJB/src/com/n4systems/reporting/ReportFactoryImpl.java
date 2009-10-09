package com.n4systems.reporting;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.interceptor.Interceptors;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.util.JRLoader;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;

import rfid.ejb.entity.UserBean;
import rfid.ejb.session.User;

import com.n4systems.ejb.PersistenceManager;
import com.n4systems.ejb.ProductManager;
import com.n4systems.ejb.interceptor.TimingInterceptor;
import com.n4systems.exceptions.EmptyReportException;
import com.n4systems.exceptions.NonPrintableManufacturerCert;
import com.n4systems.exceptions.ReportException;
import com.n4systems.model.AddressInfo;
import com.n4systems.model.ExtendedFeature;
import com.n4systems.model.Inspection;
import com.n4systems.model.InspectionBook;
import com.n4systems.model.InspectionTypeGroup;
import com.n4systems.model.LineItem;
import com.n4systems.model.Product;
import com.n4systems.model.ProductType;
import com.n4systems.model.Tenant;
import com.n4systems.model.orgs.CustomerOrg;
import com.n4systems.model.orgs.DivisionOrg;
import com.n4systems.model.orgs.InternalOrg;
import com.n4systems.model.orgs.PrimaryOrg;
import com.n4systems.model.producttype.ProductTypeLoader;
import com.n4systems.model.security.SecurityFilter;
import com.n4systems.model.utils.DateTimeDefiner;
import com.n4systems.persistence.EntityManagerTransactionWrapper;
import com.n4systems.util.ConfigContext;
import com.n4systems.util.ConfigEntry;
import com.n4systems.util.FileHelper;
import com.n4systems.util.ReportMap;

@Interceptors( { TimingInterceptor.class })
@Stateless
public class ReportFactoryImpl implements ReportFactory {
	private static final String n4LogoFileName = "n4_logo.gif";
	private static final String pdfExt = ".pdf";
	private Logger logger = Logger.getLogger(ReportFactory.class);
	
	@EJB private ProductManager productManager;
	@EJB private PersistenceManager persistenceManager;
	@EJB private User userManager;
	
	public File generateProductCertificateReport(Collection<Long> productIds, String packageName, UserBean user) throws ReportException, EmptyReportException {
		// XXX - this could probable be improved now the the rest is a little
		// cleaner
		int reportsInFile = 0;
		int totalReports = 0;
		int reportFileCount = 1;
		List<JasperPrint> printList = new ArrayList<JasperPrint>();
		File tempDir = null;
		File reportFile;
		OutputStream reportOut = null;
		File zipFile = null;

		try {
			tempDir = PathHandler.getTempDir();

			reportFile = new File(tempDir, packageName + "_" + reportFileCount + pdfExt);
			reportOut = new FileOutputStream(reportFile);

			// builds sets of pdfs.
			for (Long productId : productIds) {
				logger.debug("Processing Report for Product [" + productId + "]");

				if (reportsInFile >= ConfigContext.getCurrentContext().getInteger(ConfigEntry.REPORTING_MAX_REPORTS_PER_FILE)) {
					logger.debug("Exporing report pdf to [" + reportFile.getPath() + "]");
					CertificatePrinter.printToPDF(printList, reportOut);
					reportFileCount++;

					reportsInFile = 0;
					printList = new ArrayList<JasperPrint>();

					reportOut.close();
					reportFile = new File(tempDir, packageName + "_" + reportFileCount + pdfExt);
					reportOut = new FileOutputStream(reportFile);
				}

				try {
					printList.add(generateProductCertificate(productManager.findProduct(productId), user));
					reportsInFile++;
					totalReports++;
				} catch (ReportException e) {
					logger.debug("Report for Product [" + productId + "] is NonPrintable");
				}
			}

			// export any remaining certs
			if (printList.size() > 0) {
				CertificatePrinter.printToPDF(printList, reportOut);
				reportOut.close();
			} else {
				// there were no certs left, the file created will be empty and
				// should be deleted
				reportOut.close();
				reportFile.delete();
			}

			if (totalReports == 0) {
				throw new EmptyReportException("No printable reports found");
			}

			zipFile = FileHelper.zipDirectory(tempDir, user.getPrivateDir(), packageName);
		} catch (IOException e) {
			throw new ReportException("File access product occured", e);
		} finally {
			// close any streams that haven't been closed yet and clean up the
			// temp dir
			IOUtils.closeQuietly(reportOut);

			try {
				FileUtils.deleteDirectory(tempDir);
			} catch (IOException e) {
				logger.error("could not delete the directory " + tempDir.toString(), e);
			}
		}

		return zipFile;
	}


	/**
	 * @see com.n4systems.reporting.ReportFactory#generateProductCertificate(java.lang.Long,
	 *      java.lang.String)
	 */
	public JasperPrint generateProductCertificate(Product product, UserBean user) throws ReportException, NonPrintableManufacturerCert {

		if (!product.getType().isHasManufactureCertificate()) {
			throw new NonPrintableManufacturerCert("no cert for product.");
		}

		File jasperFile = PathHandler.getReportFile(product);

		// check to see if the report exists
		if (!jasperFile.canRead()) {
			throw new ReportException("No Product report file for tenant [" + product.getTenant().getName() + "]");
		}

		ReportMap<Object> reportMap = new ReportMap<Object>();

		reportMap.put("SUBREPORT_DIR", jasperFile.getParent() + "/");

		addImageStreams(reportMap, product.getOwner().getInternalOrg());

		addTenantParams(reportMap, product.getOwner().getPrimaryOrg());
		addOrganizationParams(reportMap, product.getOwner().getInternalOrg());
		addUserParams(reportMap, product.getIdentifiedBy());

		reportMap.putAll(new ProductReportMapProducer(product, new DateTimeDefiner(user)).produceMap());
		addProductTypeParams(reportMap, product.getType());
		addOrderParams(reportMap, product.getShopOrder());

		addCustomerParams(reportMap, product.getOwner().getCustomerOrg());
		addDivisionParams(reportMap, product.getOwner().getDivisionOrg());

		List<Product> reportCollection = new ArrayList<Product>();
		reportCollection.add(product);

		JasperPrint jasperPrint = null;
		try {
			JRBeanCollectionDataSource jrDataSource = new JRBeanCollectionDataSource(reportCollection);

			JasperReport jasperReport = (JasperReport) JRLoader.loadObject(jasperFile);
			jasperPrint = JasperFillManager.fillReport(jasperReport, reportMap, jrDataSource);

		} catch (JRException e) {
			throw new ReportException("Failed to generate report", e);
		}

		return jasperPrint;
	}

	/**
	 * @see com.n4systems.reporting.ReportFactory#generateInspectionReport(com.n4systems.util.ReportCriteria,
	 *      java.lang.String, com.n4systems.model.Tenant)
	 */
	public JasperPrint generateInspectionReport(ReportDefiner reportDefiner, SecurityFilter filter, UserBean user, Tenant tenant) throws ReportException {
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
			throw new ReportException("Failed creating certificate logo input stream", e);
		}

		return logoStream;
	}

}
