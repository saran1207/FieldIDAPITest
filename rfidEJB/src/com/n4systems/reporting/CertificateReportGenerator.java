package com.n4systems.reporting;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.zip.ZipOutputStream;

import net.sf.jasperreports.engine.JasperPrint;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;

import com.n4systems.exceptions.ReportException;
import com.n4systems.persistence.Transaction;
import com.n4systems.util.ConfigContext;
import com.n4systems.util.ConfigEntry;

public abstract class CertificateReportGenerator<T> {
	
	
	private static final String PDF_EXT = "pdf";
	protected ZipOutputStream zipOut;
	
	protected List<T> certObjects;
	private Iterator<T> certIterator;

	public CertificateReportGenerator() {
		super();
	}

	
	protected abstract void createReports(Transaction transaction) throws IOException;
	protected abstract void generateReportPage(OutputStream reportOut, List<T> inspections, Transaction transaction);
	
	protected int reportsPerOutputFile() {
		return ConfigContext.getCurrentContext().getInteger(ConfigEntry.REPORTING_MAX_REPORTS_PER_FILE);
	}

	protected String createPageFileName(String packageName, int page) {
		// note any /'s get replaced with _'s so we don't create directories within the zip file
		return String.format("%s - %d.%s", packageName.replace('/', '_'), page, PDF_EXT);
	}

	protected void generateReports(OutputStream outputFile, Transaction transaction) throws ReportException {
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


	protected Iterator<T> certIterator() {
		if (certIterator == null) {
			certIterator = certObjects.iterator();
		}
		return certIterator;
	}


	protected List<T> getNextCertGroup() {
		List<T> certGroup = new ArrayList<T>();
		while(certGroup.size() < reportsPerOutputFile() && certIterator().hasNext()) {
			T nextInspection = certIterator().next();
			if (isPrintable(nextInspection)) {
				certGroup.add(nextInspection);
			}
			
		}
		return certGroup;
	}


	protected abstract boolean isPrintable(T certObject);


	protected void printCerts(OutputStream reportOut, List<JasperPrint> page) {
		try {
			new CertificatePrinter().printToPDF(page, reportOut);
		} catch(Exception e) {
			getLogger().warn("Failed to print report page, Moving on to next page.", e);
		}
	}


	protected abstract Logger getLogger();
		
	
	
	

}