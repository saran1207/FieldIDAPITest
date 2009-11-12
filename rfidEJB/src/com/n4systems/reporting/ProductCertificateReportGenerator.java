package com.n4systems.reporting;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import net.sf.jasperreports.engine.JasperPrint;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;

import rfid.ejb.entity.UserBean;

import com.n4systems.exceptions.EmptyReportException;
import com.n4systems.exceptions.ReportException;
import com.n4systems.model.Product;
import com.n4systems.persistence.Transaction;
import com.n4systems.util.ConfigContext;
import com.n4systems.util.ConfigEntry;
import com.n4systems.util.ListHelper;

public class ProductCertificateReportGenerator {
	private static final String PDF_EXT = "pdf";
	private Logger logger = Logger.getLogger(ProductCertificateReportGenerator.class);
	
	private final ProductCertificateGenerator certGenerator;
	
	public ProductCertificateReportGenerator(ProductCertificateGenerator certGenerator) {
		this.certGenerator = certGenerator;
	}
	
	public ProductCertificateReportGenerator() {
		this(new ProductCertificateGenerator());
	}
	
	public void generate(List<Product> products, File outputFile, String packageName, UserBean user, Transaction transaction) throws ReportException, EmptyReportException {
		int reportsPerPage = ConfigContext.getCurrentContext().getInteger(ConfigEntry.REPORTING_MAX_REPORTS_PER_FILE);
		
		// first filter out any non printable products, then we subdivide the list into groups corresponding to each pdf file that will be created
		List<List<Product>> productFileGroups = ListHelper.splitList(filterNonPrintableProducts(products), reportsPerPage);
		
		if (productFileGroups.isEmpty()) {
			throw new EmptyReportException("Report contained no products with manufacturer certificates");
		}
		
		int pageNumber = 1;
		ZipOutputStream zipOut = null;
		try {
			zipOut = new ZipOutputStream(new FileOutputStream(outputFile));
			
			for (List<Product> productPage: productFileGroups) {
				zipOut.putNextEntry(new ZipEntry(createPageFileName(packageName, pageNumber)));
				
				generateReportPage(zipOut, productPage, user);
				pageNumber++;
			}
		} catch(IOException e) {
			throw new ReportException("Could not write to zip file");
		} finally {
			IOUtils.closeQuietly(zipOut);
		}
	}
	
	private List<Product> filterNonPrintableProducts(List<Product> products) {
		List<Product> printableProducts = new ArrayList<Product>();
		
		for (Product product: products) {
			if (product.getType().isHasManufactureCertificate()) {
				printableProducts.add(product);
			}
		}
		return printableProducts;
	}
	
	private void generateReportPage(OutputStream reportOut, List<Product> products, UserBean user) {
		List<JasperPrint> page = new ArrayList<JasperPrint>();
		
		for (Product product: products) {
			try {
				JasperPrint cert = certGenerator.generate(product, user);
				page.add(cert);
			} catch (Exception e) {
				logger.warn("Failed to manufacturer certificate for Product [" + product.getId() + "].  Moving on to next Product.", e);
			}
		}
		
		try {
			CertificatePrinter.printToPDF(page, reportOut);
		} catch(Exception e) {
			logger.warn("Failed to print report page, Moving on to next page.", e);
		}
	}

	private String createPageFileName(String packageName, int page) {
		// note any /'s get replaced with _'s so we don't create directories within the zip file
		return String.format("%s - %d.%s", packageName.replace('/', '_'), page, PDF_EXT);
	}
}
