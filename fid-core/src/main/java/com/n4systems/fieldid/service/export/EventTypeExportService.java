package com.n4systems.fieldid.service.export;

import com.n4systems.api.conversion.ConversionException;
import com.n4systems.api.conversion.ModelToViewConverter;
import com.n4systems.api.conversion.event.EventToViewConverter;
import com.n4systems.api.model.EventView;
import com.n4systems.exporting.beanutils.ExportMapMarshaller;
import com.n4systems.exporting.beanutils.MarshalingException;
import com.n4systems.exporting.io.EventExcelSheetManager;
import com.n4systems.exporting.io.ExcelMapWriter;
import com.n4systems.exporting.io.MapWriter;
import com.n4systems.fieldid.service.FieldIdPersistenceService;
import com.n4systems.fieldid.service.PersistenceService;
import com.n4systems.fieldid.service.event.EventService;
import com.n4systems.fieldid.service.task.AsyncService;
import com.n4systems.fieldid.service.task.AsyncService.LegacyAsyncTask;
import com.n4systems.fieldid.service.user.UserService;
import com.n4systems.model.Event;
import com.n4systems.model.downloadlink.DownloadLink;
import com.n4systems.model.downloadlink.DownloadState;
import com.n4systems.model.eventschedule.NextEventDateByEventPassthruLoader;
import com.n4systems.model.user.User;
import com.n4systems.model.utils.StreamUtils;
import org.apache.commons.lang.time.DateUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.concurrent.Callable;

import static com.google.common.base.Preconditions.checkNotNull;

public class EventTypeExportService extends FieldIdPersistenceService {
	
	@Autowired private UserService userService;
	@Autowired private AsyncService asyncService;	
	@Autowired private EventService eventService;
	@Autowired private PersistenceService persistenceService;

    private static final Logger log = Logger.getLogger(EventTypeExportService.class);
	
	
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

		LegacyAsyncTask<?> task = asyncService.createLegacyTask(new Callable<Void>() {
			@Override public Void call() { 
				generateEventByTypeExport(link.getFile(), link.getId(), eventTypeId, dateFormat, timeZone, from, to);
				return null;  // Void 
			}

		});
		asyncService.run(task);				
	}
	
	protected MapWriter createMapWriter(File downloadFile, String dateFormat, TimeZone timeZone) throws IOException {
		return new ExcelMapWriter(new FileOutputStream(downloadFile), dateFormat, timeZone).withExcelSheetManager(new EventExcelSheetManager());
	}
	
	private void updateDownloadLinkState(Long linkId, DownloadState state) {
		DownloadLink downloadLink = persistenceService.find(DownloadLink.class, linkId);
		downloadLink.setState(state);
		persistenceService.save(downloadLink);
	}		

	private void generateEventByTypeExport(File file, Long downloadLinkId, Long eventTypeId, String dateFormat, TimeZone timeZone, Date from, Date to) {
		MapWriter mapWriter = null;
		try {
			updateDownloadLinkState(downloadLinkId, DownloadState.INPROGRESS);
			mapWriter = createMapWriter(file, dateFormat, timeZone);
			export(mapWriter, eventTypeId, from, to, dateFormat, timeZone);
			updateDownloadLinkState(downloadLinkId, DownloadState.COMPLETED);
		} catch (Exception e) {
            log.error(e);
			updateDownloadLinkState(downloadLinkId, DownloadState.FAILED);
		} finally {
			StreamUtils.close(mapWriter);
		}
	}
								
	private void export(MapWriter excelMapWriter, Long eventTypeId, Date from, Date to, String dateFormat, TimeZone timeZone) throws ConversionException, IOException, MarshalingException  {
		ExportMapMarshaller<EventView> exportMapMarshaller = new ExportMapMarshaller<EventView>(EventView.class);
		ModelToViewConverter<Event,EventView> converter = new EventToViewConverter(new NextEventDateByEventPassthruLoader(), dateFormat, timeZone);

        to = DateUtils.addDays(to, 1);
		List<Event> events = eventService.getEventsByType(eventTypeId, from, to);
		EventView view;				
		for (Event event:events) {
			view = converter.toView(event);
			excelMapWriter.write(exportMapMarshaller.toBeanMap(view));
		}			
	}	
	
}
