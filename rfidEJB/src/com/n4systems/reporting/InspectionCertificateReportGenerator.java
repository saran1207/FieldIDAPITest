package com.n4systems.reporting;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import net.sf.jasperreports.engine.JasperPrint;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;

import rfid.ejb.entity.UserBean;

import com.n4systems.exceptions.EmptyReportException;
import com.n4systems.exceptions.NonPrintableEventType;
import com.n4systems.exceptions.ReportException;
import com.n4systems.model.Inspection;
import com.n4systems.persistence.Transaction;
import com.n4systems.util.ConfigContext;
import com.n4systems.util.ConfigEntry;
import com.n4systems.util.FileHelper;

public class InspectionCertificateReportGenerator {
	private static final String pdfExt = ".pdf";
	private Logger logger = Logger.getLogger(InspectionCertificateReportGenerator.class);
	
	private final InspectionCertificateGenerator certGenerator;
	
	public InspectionCertificateReportGenerator(InspectionCertificateGenerator certGenerator) {
		this.certGenerator = certGenerator;
	}
	
	public InspectionCertificateReportGenerator() {
		this(new InspectionCertificateGenerator());
	}
	
	public File generate(InspectionReportType type, List<Inspection> inspections, String packageName, UserBean user, Transaction transaction) throws ReportException, EmptyReportException {
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

			for (Inspection inspection: inspections) {
				logger.debug("Processing Report for Inspection [" + inspection.getId() + "]");
				
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
					JasperPrint cert = certGenerator.generate(type, inspection, user, transaction);
					printList.add(cert);
					reportsInFile += cert.getPages().size();
					totalReports += cert.getPages().size();
				} catch (NonPrintableEventType npe) {
					logger.debug("Report for Inspection [" + inspection.getId() + "] is not Printable");
				} catch (ReportException e) {
					logger.warn("Failed generating report for Inspection [" + inspection.getId() + "].  Moving on to next Inspection.", e);
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
			throw new ReportException("error with file access", e);
		} finally {
			// close any streams that haven't been closed yet and clean up the
			// temp dir
			IOUtils.closeQuietly(reportOut);

			if (tempDir != null) {
				try {
					FileUtils.deleteDirectory(tempDir);
				} catch (IOException e) {
					logger.error("could not delete the directory " + tempDir.toString(), e);
				}
			}
		}

		return zipFile;
	}

}
