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


import com.n4systems.exceptions.NonPrintableManufacturerCert;
import com.n4systems.exceptions.ReportException;
import com.n4systems.model.AddressInfo;
import com.n4systems.model.LineItem;
import com.n4systems.model.Product;
import com.n4systems.model.ProductType;
import com.n4systems.model.orgs.CustomerOrg;
import com.n4systems.model.orgs.DivisionOrg;
import com.n4systems.model.orgs.InternalOrg;
import com.n4systems.model.orgs.PrimaryOrg;
import com.n4systems.model.user.User;
import com.n4systems.model.utils.DateTimeDefiner;
import com.n4systems.util.ReportMap;

public class ProductCertificateGenerator {
	private static final String n4LogoFileName = "n4_logo.gif";
	private Logger logger = Logger.getLogger(ProductCertificateGenerator.class);
	
	public ProductCertificateGenerator() {}
	
	public JasperPrint generate(Product product, User user) throws ReportException, NonPrintableManufacturerCert {

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


		addUserParams(reportMap, product.getIdentifiedBy());

		reportMap.putAll(new ProductReportMapProducer(product, new DateTimeDefiner(user)).produceMap());
		addProductTypeParams(reportMap, product.getType());
		addOrderParams(reportMap, product.getShopOrder());
		addOrganizationParams(reportMap, product.getOwner().getInternalOrg());
		
		addTenantParams(reportMap, product.getOwner().getPrimaryOrg());
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
	
	private void addUserParams(ReportMap<Object> params, User user) {
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
	
	private void addProductTypeParams(ReportMap<Object> params, ProductType productType) {
		params.put("productType", productType.getName());
		params.put("itemNumber", productType.getName());
		params.put("productWarning", productType.getWarnings());
		params.put("certificateText", productType.getManufactureCertificateText());
	}
	
	private void addOrderParams(ReportMap<Object> params, LineItem lineItem) {
		params.putEmpty("orderNumber", "customerPartDescription", "partNumber", "productName");

		if (lineItem != null) {
			params.put("orderNumber", lineItem.getOrder().getOrderNumber());
			params.put("customerPartDescription", lineItem.getDescription());
			params.put("productName", lineItem.getProductCode());
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
	
}
