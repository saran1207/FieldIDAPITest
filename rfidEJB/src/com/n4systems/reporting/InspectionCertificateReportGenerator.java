package com.n4systems.reporting;

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
import com.n4systems.model.Inspection;
import com.n4systems.model.utils.DateTimeDefiner;
import com.n4systems.persistence.Transaction;

public class InspectionCertificateReportGenerator extends CertificateReportGenerator<Inspection> {
	private Logger logger = Logger.getLogger(InspectionCertificateReportGenerator.class);
	
	private final InspectionCertificateGenerator certGenerator;
	
	private String packageName;
	InspectionReportType type;
	public InspectionCertificateReportGenerator(InspectionCertificateGenerator certGenerator) {
		this.certGenerator = certGenerator;
	}
	
	public InspectionCertificateReportGenerator(DateTimeDefiner dateDefiner) {
		this(new InspectionCertificateGenerator(dateDefiner));
	}
	
	
	
	public void generate(InspectionReportType type, List<Inspection> inspections, OutputStream outputFile, UserBean user, String packageName, Transaction transaction) throws ReportException, EmptyReportException {
		
		gaurd(inspections);
		this.type = type;
		this.certObjects = inspections;
		this.packageName = packageName;
		generateReports(outputFile, transaction);
	}

	private void gaurd(List<Inspection> inspections) throws EmptyReportException {
		if (inspections.isEmpty()) {
			throw new EmptyReportException("Report contained no printable inspections");
		}
	}

	@Override
	protected void createReports(Transaction transaction) throws IOException {
		int pageNumber = 1;
		List<Inspection> inspectionPage = null;
		while ((inspectionPage = getNextCertGroup()) != null && !inspectionPage.isEmpty()) {
			zipOut.putNextEntry(new ZipEntry(createPageFileName(packageName, pageNumber)));
			generateReportPage(zipOut, inspectionPage, transaction);
			pageNumber++;
		}
	}

	
	

	protected void generateReportPage(OutputStream reportOut, List<Inspection> inspections, Transaction transaction) {
		List<JasperPrint> page = createCerts(inspections, transaction);
		printCerts(reportOut, page);
	}

	private List<JasperPrint> createCerts(List<Inspection> inspections, Transaction transaction) {
		List<JasperPrint> page = new ArrayList<JasperPrint>();
		
		for (Inspection inspection: inspections) {
			try {
				JasperPrint cert = certGenerator.generate(type, inspection, transaction);
				page.add(cert);
			} catch (Exception e) {
				logger.warn("Failed to generate report for Inspection [" + inspection.getId() + "].  Moving on to next Inspection.", e);
			}
		}
		return page;
	}
	
	@Override
	protected  boolean isPrintable(Inspection certObject) {
		return certObject.isPrintableForReportType(type);

	}

	@Override
	protected Logger getLogger() {
		return logger;
	}
	
}
