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
import com.n4systems.exceptions.ReportException;
import com.n4systems.model.Product;
import com.n4systems.util.ConfigContext;
import com.n4systems.util.ConfigEntry;
import com.n4systems.util.FileHelper;

public class ProductCertificateReportGenerator {
	private static final String pdfExt = ".pdf";
	private Logger logger = Logger.getLogger(ProductCertificateReportGenerator.class);
	
	private final ProductCertificateGenerator certGenerator;
	
	public ProductCertificateReportGenerator(ProductCertificateGenerator certGenerator) {
		this.certGenerator = certGenerator;
	}
	
	public ProductCertificateReportGenerator() {
		this(new ProductCertificateGenerator());
	}
	
	public File generate(List<Product> products, String packageName, UserBean user) throws ReportException, EmptyReportException {
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
			for (Product product: products) {
				logger.debug("Processing Report for Product [" + product.getId() + "]");

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
					printList.add(certGenerator.generate(product, user));
					reportsInFile++;
					totalReports++;
				} catch (ReportException e) {
					logger.debug("Report for Product [" + product.getId() + "] is NonPrintable");
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

}
