package com.n4systems.reporting;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;

import net.sf.jasperreports.engine.JasperPrint;

import org.apache.log4j.Logger;

import rfid.ejb.entity.UserBean;

import com.n4systems.exceptions.EmptyReportException;
import com.n4systems.exceptions.ReportException;
import com.n4systems.model.Product;
import com.n4systems.persistence.Transaction;

public class ProductCertificateReportGenerator extends CertificateReportGenerator<Product> {
	private Logger logger = Logger.getLogger(ProductCertificateReportGenerator.class);
	
	private final ProductCertificateGenerator certGenerator;

	private String packageName;

	private UserBean user;
	
	public ProductCertificateReportGenerator(ProductCertificateGenerator certGenerator) {
		this.certGenerator = certGenerator;
	}
	
	public ProductCertificateReportGenerator() {
		this(new ProductCertificateGenerator());
	}
	
	public void generate(List<Product> products, FileOutputStream outputFile, String packageName, UserBean user, Transaction transaction) throws ReportException, EmptyReportException {
		
		// first filter out any non printable products, then we subdivide the list into groups corresponding to each pdf file that will be created
		this.packageName = packageName;
		this.user = user;
		this.certObjects = products;
		
		guard(products);
		
		generateReports(outputFile, transaction);
	}

	private void guard(List<Product> products) throws EmptyReportException {
		if (products.isEmpty()) {
			throw new EmptyReportException("Report contained no products with manufacturer certificates");
		}
	}

	
	@Override
	protected void generateReportPage(OutputStream reportOut, List<Product> products, Transaction transaction) {
		List<JasperPrint> page = createCerts(products);
		
		printCerts(reportOut, page);
	}

	
	private List<JasperPrint> createCerts(List<Product> products) {
		List<JasperPrint> page = new ArrayList<JasperPrint>();
		
		for (Product product: products) {
			try {
				JasperPrint cert = certGenerator.generate(product, user);
				page.add(cert);
			} catch (Exception e) {
				logger.warn("Failed to manufacturer certificate for Product [" + product.getId() + "].  Moving on to next Product.", e);
			}
		}
		return page;
	}

	@Override
	protected void createReports(Transaction transaction) throws IOException {
		int pageNumber = 1;
		List<Product> productGroup = null;
		while ((productGroup = getNextCertGroup()) != null && !productGroup.isEmpty()) {
			zipOut.putNextEntry(new ZipEntry(createPageFileName(packageName, pageNumber)));
			generateReportPage(zipOut, productGroup, transaction);
			pageNumber++;
		}
		
	}

	@Override
	protected boolean isPrintable(Product certObject) {
		return certObject.getType().isHasManufactureCertificate();
	}
	

	@Override
	protected Logger getLogger() {
		return logger;
	}

}
