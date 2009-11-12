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
import com.n4systems.model.Inspection;
import com.n4systems.model.utils.DateTimeDefiner;
import com.n4systems.persistence.Transaction;
import com.n4systems.util.ConfigContext;
import com.n4systems.util.ConfigEntry;
import com.n4systems.util.ListHelper;

public class InspectionCertificateReportGenerator {
	private static final String PDF_EXT = "pdf";
	private Logger logger = Logger.getLogger(InspectionCertificateReportGenerator.class);
	
	private final InspectionCertificateGenerator certGenerator;
	
	public InspectionCertificateReportGenerator(InspectionCertificateGenerator certGenerator) {
		this.certGenerator = certGenerator;
	}
	
	public InspectionCertificateReportGenerator(DateTimeDefiner dateDefiner) {
		this(new InspectionCertificateGenerator(dateDefiner));
	}
	
	public void generate(InspectionReportType type, List<Inspection> inspections, File outputFile, UserBean user, String packageName, Transaction transaction) throws ReportException, EmptyReportException {
		int reportsPerPage = ConfigContext.getCurrentContext().getInteger(ConfigEntry.REPORTING_MAX_REPORTS_PER_FILE);
		
		// first filter out any non printable inspections, then we subdivide the list into groups corresponding to each pdf file that will be created
		List<List<Inspection>> inspectionFileGroups = ListHelper.splitList(filterNonPrintableInspections(inspections, type), reportsPerPage);
		
		if (inspectionFileGroups.isEmpty()) {
			throw new EmptyReportException("Report contained no printable inspections");
		}
		
		int pageNumber = 1;
		ZipOutputStream zipOut = null;
		try {
			zipOut = new ZipOutputStream(new FileOutputStream(outputFile));
			
			for (List<Inspection> inspectionPage: inspectionFileGroups) {
				zipOut.putNextEntry(new ZipEntry(createPageFileName(packageName, pageNumber)));
				
				generateReportPage(zipOut, inspectionPage, type, transaction);
				pageNumber++;
			}
		} catch(IOException e) {
			throw new ReportException("Could not write to zip file");
		} finally {
			IOUtils.closeQuietly(zipOut);
		}
	}
	
	private List<Inspection> filterNonPrintableInspections(List<Inspection> inspections, InspectionReportType type) {
		List<Inspection> printableInspections = new ArrayList<Inspection>();
		
		for (Inspection inspection: inspections) {
			if (inspection.isPrintableForReportType(type)) {
				printableInspections.add(inspection);
			}
		}
		return printableInspections;
	}
	
	private void generateReportPage(OutputStream reportOut, List<Inspection> inspections, InspectionReportType type, Transaction transaction) {
		List<JasperPrint> page = new ArrayList<JasperPrint>();
		
		for (Inspection inspection: inspections) {
			try {
				JasperPrint cert = certGenerator.generate(type, inspection, transaction);
				page.add(cert);
			} catch (Exception e) {
				logger.warn("Failed to generate report for Inspection [" + inspection.getId() + "].  Moving on to next Inspection.", e);
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
