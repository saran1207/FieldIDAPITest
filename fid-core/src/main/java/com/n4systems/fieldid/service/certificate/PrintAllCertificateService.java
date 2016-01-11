package com.n4systems.fieldid.service.certificate;

import com.n4systems.exceptions.NonPrintableEventType;
import com.n4systems.exceptions.NonPrintableManufacturerCert;
import com.n4systems.exceptions.ReportException;
import com.n4systems.fieldid.service.FieldIdPersistenceService;
import com.n4systems.fieldid.service.amazon.S3Service;
import com.n4systems.fieldid.service.mail.MailService;
import com.n4systems.fieldid.service.search.AssetSearchService;
import com.n4systems.fieldid.service.search.ReportService;
import com.n4systems.fieldid.service.task.AsyncService;
import com.n4systems.fieldid.service.task.AsyncService.AsyncTask;
import com.n4systems.fieldid.service.task.DownloadLinkService;
import com.n4systems.model.Event;
import com.n4systems.model.downloadlink.DownloadLink;
import com.n4systems.model.downloadlink.DownloadState;
import com.n4systems.model.search.AssetSearchCriteria;
import com.n4systems.model.search.EventReportCriteria;
import com.n4systems.reporting.EventReportType;
import com.n4systems.services.config.ConfigService;
import com.n4systems.util.ConfigEntry;
import com.n4systems.util.mail.TemplateMailMessage;
import com.n4systems.util.selection.MultiIdSelection;
import net.sf.jasperreports.engine.JasperPrint;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Transactional(readOnly = true)
public class PrintAllCertificateService extends FieldIdPersistenceService {
	private final Logger logger = Logger.getLogger(PrintAllCertificateService.class);
	
	private final CertificatePrinter printer = new CertificatePrinter();

	@Autowired private DownloadLinkService downloadLinkService;
	@Autowired private CertificateService certificateService;
	@Autowired private ConfigService configService;
	@Autowired private MailService mailService;
	@Autowired private AsyncService asyncService;

    @Autowired private ReportService reportService;
    @Autowired private AssetSearchService assetSearchService;

	@Autowired private S3Service s3Service;
	
	public DownloadLink generateAssetCertificates(AssetSearchCriteria criteria, final String downloadUrl, final DownloadLink link) {
        final List<Long> assetIds = criteria.getSelection().getSelectedIds();

        if (criteria.getSortColumn() != null) {
            List<Long> resultOrder = assetSearchService.idSearch(criteria);
            sortAssetIdsByResultOrder(assetIds,resultOrder);
        }
		
		AsyncTask<?> task = asyncService.createTask(() -> {
            generateCertificatePackage(assetIds, null, link, downloadUrl, "printAllAssetCerts");
            return null;
        });
		asyncService.run(task);
		
		return link;
	}

    private void sortAssetIdsByResultOrder(List<Long> assetIds, final List<Long> resultOrder) {
        Collections.sort(assetIds, (id1, id2) -> new Integer(resultOrder.indexOf(id1)).compareTo(resultOrder.indexOf(id2)));
    }

    public DownloadLink generateEventCertificates(EventReportCriteria criteriaModel, final EventReportType reportType, final String downloadUrl, final DownloadLink link) {
        final List<Long> sortedSearchResultsList = reportService.idSearch(criteriaModel);
        final List<Long> sortedSelectedList = sortSelectionBasedOnIndexIn(criteriaModel.getSelection(), sortedSearchResultsList);

        AsyncTask<?> task = asyncService.createTask(() -> {
            generateCertificatePackage(sortedSelectedList, reportType, link, downloadUrl, "printAllEventCerts");
            return null;
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

            ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
			
			zipOut = new ZipOutputStream(byteOut);

			int pageNumber = 1;
			JasperPrint jPrint;
			List<JasperPrint> printGroup = new ArrayList<>();
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
			} else {
                if(pageNumber == 1) {
                    //In this case, the printGroup is empty AND pageNumber was never incremented past one.  This means
                    //we didn't produce any printed reports.  Something went wrong... that "something" is likely that
                    //the user tried to print a boatload of unprintable events.
                    throw new Exception("No reports were printed, likely because they were all non-printable.");
                }
            }

            //Finish working on the zipOut only... we'll handle the byte array separately.
            zipOut.finish();

			//Before we update the state of the DownloadLink, we want to shove it up into S3.  Otherwise any attempted
			//downloads could fail if the file is big enough to present a considerable delay between update of state
			//and arrival in the cloud.
			s3Service.uploadGeneratedReport(byteOut.toByteArray(), link);

			//Now the file is up there... or we at least think it is.  Flip it to completed.
			downloadLinkService.updateState(link, DownloadState.COMPLETED);
			sendSuccessNotification(link, downloadUrl, templateName);
		} catch (Exception e) {
			downloadLinkService.updateState(link, DownloadState.FAILED);
			logger.error("Failed generating multi event certificate download", e);
		} finally {
			IOUtils.closeQuietly(zipOut);
		}
	}

	private JasperPrint generateCertificate(EventReportType eventReportType, Long entityId) throws Exception {
		JasperPrint jPrint = null;
		try {
			// If eventReportType is null, assume it's an asset certificate
			if (eventReportType != null) {
                // Must convert from event schedule id to event id to generate the event certificate
                Event eventSchedule = persistenceService.find(Event.class, entityId);
				jPrint = certificateService.generateEventCertificate(eventReportType, eventSchedule.getId());
			} else {
				jPrint = certificateService.generateAssetCertificate(entityId);
			}
		} catch (NonPrintableEventType e) {
            logger.warn("Just tried to print a Non-Printable Event!  Moving on.");
		} catch (NonPrintableManufacturerCert e) {
            logger.warn("Tried to print a Non-Printable Manufacturer Certificate!  Moving on.");
		} catch (ReportException e) {
            //This exception, after trimming off the non-printable exceptions, seems to be the place we're likely to
            //fail if the .jasper files aren't present.  Lets just kick back an exception that should result in
            //early termination of the print run.  This should probably be all or nothing, barring any non-printable
            //certificates... for those, we'll just omit them from the .zip file and fail their generation silently.
			logger.error("Failed generating certificate, moving on", e);
            throw new Exception("Failed to print a PRINTABLE Event or Manufacturer certificate.  This is likely due to a missing or corrupt .jasper file.");
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

    private List<Long> sortSelectionBasedOnIndexIn(MultiIdSelection selection, final List<Long> idList) {
        final List<Long> selectedIds = selection.getSelectedIds();
        Collections.sort(selectedIds, (id1, id2) -> new Integer(idList.indexOf(id1)).compareTo(idList.indexOf(id2)));
        return selectedIds;
    }

}
