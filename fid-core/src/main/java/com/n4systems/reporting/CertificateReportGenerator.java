package com.n4systems.reporting;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import net.sf.jasperreports.engine.JasperPrint;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;

import com.n4systems.exceptions.EmptyReportException;
import com.n4systems.exceptions.ReportException;
import com.n4systems.persistence.Transaction;
import com.n4systems.util.ConfigContext;
import com.n4systems.util.ConfigEntry;

public abstract class CertificateReportGenerator<T> {
	
	
	private static final String PDF_EXT = "pdf";
	protected ZipOutputStream zipOut;
	
	protected List<T> certObjects;
	private Iterator<T> certIterator;
	protected String packageName;
	protected Transaction transaction;

	public CertificateReportGenerator() {
		super();
	}

	

	private int reportsPerOutputFile() {
		return ConfigContext.getCurrentContext().getInteger(ConfigEntry.REPORTING_MAX_REPORTS_PER_FILE);
	}

	private String createPageFileName(String packageName, int page) {
		// note any /'s get replaced with _'s so we don't create directories within the zip file
		return String.format("%s - %d.%s", packageName.replace('/', '_'), page, PDF_EXT);
	}

	private void generateReports(OutputStream outputFile, Transaction transaction) throws ReportException {
		try {
			zipOut = createOutputStream(outputFile);
			createReports(transaction);
		} catch(IOException e) {
			throw new ReportException("Could not write to zip file");
		} finally {
			IOUtils.closeQuietly(zipOut);
		}
	}
	
	private ZipOutputStream createOutputStream(OutputStream outputFile) throws FileNotFoundException {
		return new ZipOutputStream(outputFile);
	}


	private Iterator<T> certIterator() {
		if (certIterator == null) {
			certIterator = certObjects.iterator();
		}
		return certIterator;
	}


	private List<T> getNextCertGroup() {
		List<T> certGroup = new ArrayList<T>();
		while(certGroup.size() < reportsPerOutputFile() && certIterator().hasNext()) {
			T nextEvent = certIterator().next();
			if (isPrintable(nextEvent)) {
				certGroup.add(nextEvent);
			}
			
		}
		return certGroup;
	}





	private void printCerts(OutputStream reportOut, List<JasperPrint> page) {
		try {
			new CertificatePrinter().printToPDF(page, reportOut);
		} catch(Exception e) {
			getLogger().warn("Failed to print report page, Moving on to next page.", e);
		}
	}
	
	private List<JasperPrint> createCerts(List<T> events) {
		List<JasperPrint> page = new ArrayList<JasperPrint>();
		
		for (T event: events) {
			try {
				JasperPrint cert = singleCert(event);
				page.add(cert);
			} catch (Exception e) {
				logCertError(event, e);
			}
		}
		return page;
	}
	
	protected abstract boolean isPrintable(T certObject);

	protected abstract Logger getLogger();
	protected abstract void guard();
	protected abstract JasperPrint singleCert(T event) throws ReportException;
	protected abstract void logCertError(T product, Exception e);

	public void generate(List<T> products, OutputStream outputFile, String packageName, Transaction transaction)
			throws ReportException, EmptyReportException {
			if (products.isEmpty()) {
				throw new EmptyReportException("Report contained no printable certificates");
			}
			// first filter out any non printable products, then we subdivide the list into groups corresponding to each pdf file that will be created
			this.packageName = packageName;
			
			this.certObjects = products;
			this.transaction = transaction;
			guard();
			
			generateReports(outputFile, transaction);
		}


	
	private void generateReportPage(OutputStream reportOut, List<T> products, Transaction transaction) {
		List<JasperPrint> page = createCerts(products);
		
		printCerts(reportOut, page);
	}


	
	private void createReports(Transaction transaction) throws IOException {
		int pageNumber = 1;
		List<T> eventPage = null;
		while ((eventPage = getNextCertGroup()) != null && !eventPage.isEmpty()) {
			zipOut.putNextEntry(new ZipEntry(createPageFileName(packageName, pageNumber)));
			generateReportPage(zipOut, eventPage, transaction);
			pageNumber++;
		}
	}
	
	
	
	

}