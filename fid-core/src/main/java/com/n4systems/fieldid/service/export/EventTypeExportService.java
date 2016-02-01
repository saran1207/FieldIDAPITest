package com.n4systems.fieldid.service.export;

import com.n4systems.api.conversion.ConversionException;
import com.n4systems.api.conversion.ModelToViewConverter;
import com.n4systems.api.conversion.event.EventToViewConverter;
import com.n4systems.api.model.EventView;
import com.n4systems.exporting.beanutils.ExportMapMarshaller;
import com.n4systems.exporting.beanutils.MarshalingException;
import com.n4systems.exporting.io.ExcelXSSFMapWriter;
import com.n4systems.exporting.io.MapWriter;
import com.n4systems.fieldid.service.FieldIdPersistenceService;
import com.n4systems.fieldid.service.PersistenceService;
import com.n4systems.fieldid.service.amazon.S3Service;
import com.n4systems.fieldid.service.event.EventService;
import com.n4systems.fieldid.service.task.AsyncService;
import com.n4systems.fieldid.service.task.AsyncService.LegacyAsyncTask;
import com.n4systems.fieldid.service.user.UserService;
import com.n4systems.model.ThingEvent;
import com.n4systems.model.downloadlink.DownloadLink;
import com.n4systems.model.downloadlink.DownloadState;
import com.n4systems.model.eventschedule.NextEventDateByEventPassthruLoader;
import com.n4systems.model.user.User;
import com.n4systems.model.utils.DateTimeDefiner;
import com.n4systems.reporting.PathHandler;
import com.n4systems.services.config.ConfigService;
import com.n4systems.util.time.RateTimer;
import org.apache.commons.lang.time.DateUtils;
import org.apache.commons.lang.time.StopWatch;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.io.*;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import static com.google.common.base.Preconditions.checkNotNull;

public class EventTypeExportService extends FieldIdPersistenceService {
	static Logger logger = Logger.getLogger("com.n4systems.taskscheduling");

	@Autowired private UserService userService;
	@Autowired private AsyncService asyncService;	
	@Autowired private EventService eventService;
	@Autowired private PersistenceService persistenceService;
    @Autowired private S3Service s3Service;
	@Autowired private ConfigService configService;

	/**
	 * First attempt at Spring-ifying async transactions.   this needs serious refactoring before next service method is added.  it's ugly!!!!
	 * suggestions : need to avoid creating runnables, need a DownloadService that handles all the link stuff automatically.  a couple of advices might make things cleaner.
	 * may need to implement MailManager for this stuff...ask matt.  
	 * 
	 */
	
	@Transactional
	public void exportEventTypeToExcel(Long userId, final Long eventTypeId, final Date from, final Date to, Long linkId) {
		final User user = userService.getUser(userId);
		checkNotNull(user);		
		final String dateFormat = user.getOwner().getPrimaryOrg().getDateFormat();
        final TimeZone timeZone = user.getTimeZone();
        
		final DownloadLink link = persistenceService.find(DownloadLink.class,linkId);

		LegacyAsyncTask<Void> task = asyncService.createLegacyTask(() -> {
//            generateEventByTypeExport(link.getFile(), link.getId(), eventTypeId, dateFormat, timeZone, from, to);
            generateEventByTypeExport(link, eventTypeId, dateFormat, timeZone, from, to);
            return null;  // Void
        });
		asyncService.run(task);				
	}
	
	private void updateDownloadLinkState(Long linkId, DownloadState state) {
		DownloadLink downloadLink = persistenceService.find(DownloadLink.class, linkId);
		downloadLink.setState(state);
		persistenceService.save(downloadLink);
	}

	private void generateEventByTypeExport(DownloadLink downloadLink, Long eventTypeId, String dateFormat, TimeZone timeZone, Date from, Date to) {
		logger.info("[" + downloadLink + "]: Started event type export for type [" + eventTypeId + "]");

		RateTimer timer = new RateTimer();
		timer.start().split();

		File tmpFile = PathHandler.getTempFileWithExt(downloadLink.getContentType().getExtension());
		try {
            updateDownloadLinkState(downloadLink.getId(), DownloadState.INPROGRESS);

			try (OutputStream stream = new BufferedOutputStream(new FileOutputStream(tmpFile));
				 ExcelXSSFMapWriter mapWriter = new ExcelXSSFMapWriter(new DateTimeDefiner(downloadLink.getUser()))) {
				export(mapWriter, eventTypeId, from, to, dateFormat, timeZone, downloadLink, timer);
				mapWriter.writeToStream(stream);
			}

			logger.info("[" + downloadLink + "]: Finished " + getClass().getSimpleName() + " generate, starting S3 upload");
            s3Service.uploadGeneratedReport(tmpFile, downloadLink);

            //It might be okay to just set the state to completed... I'm pretty sure a legitimate failure of the
            //upload ends up with an Exception that would jump this following line and drop execution right into the
            //catch block...
            updateDownloadLinkState(downloadLink.getId(), DownloadState.COMPLETED);
			logger.info("[" + downloadLink + "]: Completed in " + timer.elapsedString());
        } catch (Exception e) {
			logger.error("[" + downloadLink + "]: Failed", e);
            updateDownloadLinkState(downloadLink.getId(), DownloadState.FAILED);
        } finally {
			tmpFile.delete();
		}
    }
								
	private void export(MapWriter excelMapWriter, Long eventTypeId, Date from, Date to, String dateFormat, TimeZone timeZone, DownloadLink link, RateTimer timer) throws ConversionException, IOException, MarshalingException  {
		int exportPageSize = configService.getConfig().getLimit().getExportPageSize();

		ExportMapMarshaller<EventView> exportMapMarshaller = new ExportMapMarshaller<>(EventView.class);
		ModelToViewConverter<ThingEvent,EventView> converter = new EventToViewConverter(new NextEventDateByEventPassthruLoader(), dateFormat, timeZone);

		Long totalEvents = eventService.countThingEventsByType(eventTypeId, from, DateUtils.addDays(to, 1));

		timer.split();
		int page = -1;
		EventView view;
		List<ThingEvent> events;
		while ((events = eventService.getThingEventsByType(eventTypeId, from, DateUtils.addDays(to, 1), ++page, exportPageSize)).size() > 0) {
			for (ThingEvent event: events) {
				view = converter.toView(event);
				excelMapWriter.write(exportMapMarshaller.toBeanMap(view));
				timer.increment();
			}
			logger.info(String.format("[%s]: (%d/%d) Elapsed: %s, Since last %d: %s, Avg Rate: %.2f ms/entity, Interval Rate: %.2f ms/entity",
					link, timer.getCount(), totalEvents, timer.elapsedString(), timer.getSplitCountDiff(), timer.elapsedSinceSplitString(),
					timer.paceAvg(TimeUnit.MILLISECONDS), timer.paceSinceSplit(TimeUnit.MILLISECONDS)));

			timer.split();
		}
	}
	
}
