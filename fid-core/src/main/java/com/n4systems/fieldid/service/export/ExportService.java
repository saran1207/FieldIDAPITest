package com.n4systems.fieldid.service.export;

import static com.google.common.base.Preconditions.*;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Callable;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

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
import com.n4systems.fieldid.service.task.AsyncTaskFactory;
import com.n4systems.fieldid.service.task.AsyncTaskFactory.AsyncTask;
import com.n4systems.fieldid.service.user.UserService;
import com.n4systems.model.Event;
import com.n4systems.model.downloadlink.DownloadLink;
import com.n4systems.model.downloadlink.DownloadState;
import com.n4systems.model.user.User;
import com.n4systems.model.utils.StreamUtils;

public class ExportService extends FieldIdPersistenceService {
	
	@Autowired private UserService userService;
	@Autowired private AsyncService asyncService;
	@Autowired private AsyncTaskFactory asyncTaskFactory;		
	@Autowired private EventService eventService;
	@Autowired private PersistenceService persistenceService;
	
	
	/**
	 * First attempt at Spring-ifying async transactions.   this needs serious refactoring before next service method is added.  it's ugly!!!!
	 * suggestions : need to avoid creating runnables, need a DownloadService that handles all the link stuff automatically.  a couple of advices might make things cleaner.
	 * may need to implement MailManager for this stuff...ask matt.  
	 * 
	 * TODO DD : TESTS are needed for this and all composing classes (particularly Excel??Managers).
	 */
	
	@Transactional
	public void exportEventTypeToExcel(Long userId, final Long eventTypeId, final Date from, final Date to, Long linkId) {
		System.out.println("calling service in thread " + Thread.currentThread().getId() + Thread.currentThread().getName());
		
		final User user = userService.getUser(userId);
		checkNotNull(user);		
		final String dateFormat = user.getOwner().getPrimaryOrg().getDateFormat();

		final DownloadLink link = persistenceService.find(DownloadLink.class,linkId);

		AsyncTask<?> task = asyncTaskFactory.createTask(new Callable<Void>() {
			@Override public Void call() { 
				generateEventByTypeExport(link.getFile(), link.getId(), eventTypeId, dateFormat, from, to); 
				return null;  // Void 
			}

		});
		asyncService.run(task);				
	}
	
	protected MapWriter createMapWriter(File downloadFile, String dateFormat) throws IOException {
		return new ExcelMapWriter(new FileOutputStream(downloadFile), dateFormat).withExcelSheetManager(new EventExcelSheetManager());
	}
	
	private void updateDownloadLinkState(Long linkId, DownloadState state) {
		DownloadLink downloadLink = persistenceService.find(DownloadLink.class, linkId);
		downloadLink.setState(state);
		persistenceService.save(downloadLink);
	}		

	// TODO DD : this smells of aop implemention...e.g. @nnotate method as "@Downloadable" which will wrap downloadLink handling.
	private void generateEventByTypeExport(File file, Long downloadLinkId, Long eventTypeId, String dateFormat, Date from, Date to) {
		MapWriter mapWriter = null;
		try {
			updateDownloadLinkState(downloadLinkId, DownloadState.INPROGRESS);
			mapWriter = createMapWriter(file, dateFormat);
			export(mapWriter, eventTypeId, from, to);
			updateDownloadLinkState(downloadLinkId, DownloadState.COMPLETED);
		} catch (Exception e) {
			updateDownloadLinkState(downloadLinkId, DownloadState.FAILED);
		} finally {
			StreamUtils.close(mapWriter);
		}
	}
								
	private void export(MapWriter excelMapWriter, Long eventTypeId, Date from, Date to) throws ConversionException, IOException, MarshalingException  {	
		ExportMapMarshaller<EventView> exportMapMarshaller = new ExportMapMarshaller<EventView>(EventView.class);
		ModelToViewConverter<Event,EventView> converter = new EventToViewConverter();
			
		List<Event> events = eventService.getEventsByType(eventTypeId, from, to);
		EventView view;				
		for (Event event:events) {
			view = converter.toView(event);
			excelMapWriter.write(exportMapMarshaller.toBeanMap(view));
		}			
	}	
	
}
