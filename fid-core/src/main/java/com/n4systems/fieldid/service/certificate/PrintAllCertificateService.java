package com.n4systems.fieldid.service.certificate;

import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import net.sf.jasperreports.engine.JasperPrint;

import org.apache.log4j.Logger;
import org.apache.wicket.util.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.n4systems.exceptions.NonPrintableEventType;
import com.n4systems.exceptions.NonPrintableManufacturerCert;
import com.n4systems.exceptions.ReportException;
import com.n4systems.fieldid.service.FieldIdPersistenceService;
import com.n4systems.fieldid.service.mail.MailService;
import com.n4systems.fieldid.service.task.AsyncService;
import com.n4systems.fieldid.service.task.DownloadLinkService;
import com.n4systems.fieldid.service.task.AsyncService.AsyncTask;
import com.n4systems.model.downloadlink.ContentType;
import com.n4systems.model.downloadlink.DownloadLink;
import com.n4systems.model.downloadlink.DownloadState;
import com.n4systems.reporting.EventReportType;
import com.n4systems.services.ConfigService;
import com.n4systems.util.ConfigEntry;
import com.n4systems.util.mail.TemplateMailMessage;

@Transactional(readOnly = true)
public class PrintAllCertificateService extends FieldIdPersistenceService {
	private final Logger logger = Logger.getLogger(PrintAllCertificateService.class);
	
	private final CertificatePrinter printer = new CertificatePrinter();
	
	@Autowired private DownloadLinkService downloadLinkService;
	@Autowired private CertificateService certificateService;
	@Autowired private ConfigService configService;
	@Autowired private MailService mailService;
	@Autowired private AsyncService asyncService; 
	
	public DownloadLink generateAssetCertificates(final List<Long> assetIds, final String downloadUrl, String reportName) {
		final DownloadLink link = downloadLinkService.createDownloadLink(reportName, ContentType.ZIP);
		
		AsyncTask<?> task = asyncService.createTask(new Callable<Void>() {
			@Override
			public Void call() throws Exception {
				generateCertificatePackage(assetIds, null, link, downloadUrl, "printAllAssetCerts");
				return null;
			}
		});
		asyncService.run(task);
		
		return link;
	}

	public DownloadLink generateEventCertificates(final List<Long> eventIds, final EventReportType reportType, final String downloadUrl, String reportName) {
		final DownloadLink link = downloadLinkService.createDownloadLink(reportName, ContentType.ZIP);
		
		AsyncTask<?> task = asyncService.createTask(new Callable<Void>() {
			@Override
			public Void call() throws Exception {
				generateCertificatePackage(eventIds, reportType, link, downloadUrl, "printAllEventCerts");
				return null;
			}
		});
		asyncService.run(task);
		
		return link;
	}

	private void generateCertificatePackage(List<Long> entityIds, EventReportType eventReportType, DownloadLink link, String downloadUrl, String templateName) {
		ZipOutputStream zipOut = null;
		try {
			if (entityIds.isEmpty()) {
				downloadLinkService.updateState(link, DownloadState.FAILED);	
				mailService.sendMessage(link.generateMailMessage("We're sorry, your report did not contain any printable events."));
				return;
			}
			
			downloadLinkService.updateState(link, DownloadState.INPROGRESS);
			
			Integer maxCertsPerGroup = configService.getInteger(ConfigEntry.REPORTING_MAX_REPORTS_PER_FILE);
			
			zipOut = new ZipOutputStream(new FileOutputStream(link.getFile()));
			
			int pageNumber = 1;
			JasperPrint jPrint;
			List<JasperPrint> printGroup = new ArrayList<JasperPrint>();
			for (Long entityId: entityIds) {
				
				if (printGroup.size() == maxCertsPerGroup) {
					zipOut.putNextEntry(new ZipEntry(pdfFileName(link.getName(), pageNumber)));
					printer.printToPDF(printGroup, zipOut);
					printGroup.clear();
					pageNumber++;
				}
				
				jPrint = generateCertificate(eventReportType, entityId);
				if (jPrint != null) {
					printGroup.add(jPrint);
				}
			}
			
			if (!printGroup.isEmpty()) {
				zipOut.putNextEntry(new ZipEntry(pdfFileName(link.getName(), pageNumber)));
				printer.printToPDF(printGroup, zipOut);
			}
			
			downloadLinkService.updateState(link, DownloadState.COMPLETED);
			sendSuccessNotification(link, downloadUrl, templateName);
			
		} catch (Exception e) {
			downloadLinkService.updateState(link, DownloadState.FAILED);
			logger.error("Failed generating multi event certificate download", e);
		} finally {
			IOUtils.closeQuietly(zipOut);
		}
	}

	private JasperPrint generateCertificate(EventReportType eventReportType, Long entityId) {
		JasperPrint jPrint = null;
		try {
			// If eventReportType is null, assume it's an asset certificate
			if (eventReportType != null) {
				jPrint = certificateService.generateEventCertificate(eventReportType, entityId);
			} else {
				jPrint = certificateService.generateAssetCertificate(entityId);
			}
		} catch (NonPrintableEventType e) {
		} catch (NonPrintableManufacturerCert e) {
		} catch (ReportException e) {
			logger.warn("Failed generating certificate, moving on", e);
		}
		return jPrint;
	}
	
	private String pdfFileName(String packageName, int page) {
		// note any /'s get replaced with _'s so we don't create directories within the zip file
		return String.format("%s - %d.%s", packageName.replace('/', '_'), page, "pdf");
	}
	
	private void sendSuccessNotification(DownloadLink downloadLink, String downloadUrl, String template) {
		try {
			TemplateMailMessage msg = new TemplateMailMessage(downloadLink.getName(), template);
			msg.getToAddresses().add(downloadLink.getUser().getEmailAddress());
			msg.getTemplateMap().put("downloadLink", downloadLink);
			msg.getTemplateMap().put("downloadUrl", downloadUrl + downloadLink.getId());
			mailService.sendMessage(msg);
		} catch(Exception e) {
			logger.error("Failed sending success notification", e);
		}
	}

}
