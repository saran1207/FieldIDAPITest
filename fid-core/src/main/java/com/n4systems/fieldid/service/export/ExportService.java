package com.n4systems.fieldid.service.export;

import static com.google.common.base.Preconditions.*;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.Callable;

import jxl.write.WritableSheet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.n4systems.api.conversion.ConversionException;
import com.n4systems.api.conversion.ModelToViewConverter;
import com.n4systems.api.conversion.event.EventToViewConverter;
import com.n4systems.api.model.EventView;
import com.n4systems.exporting.beanutils.CriteriaResultSerializationHandler;
import com.n4systems.exporting.beanutils.ExportMapMarshaller;
import com.n4systems.exporting.beanutils.MarshalingException;
import com.n4systems.exporting.io.ExcelMapWriter;
import com.n4systems.exporting.io.ExcelSheetManager;
import com.n4systems.exporting.io.MapWriter;
import com.n4systems.fieldid.service.FieldIdPersistenceService;
import com.n4systems.fieldid.service.PersistenceService;
import com.n4systems.fieldid.service.event.EventService;
import com.n4systems.fieldid.service.task.AsyncService;
import com.n4systems.fieldid.service.task.AsyncTaskFactory;
import com.n4systems.fieldid.service.task.AsyncTaskFactory.AsyncTask;
import com.n4systems.fieldid.service.user.UserService;
import com.n4systems.model.Event;
import com.n4systems.model.EventType;
import com.n4systems.model.downloadlink.ContentType;
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
	 * First attempt at Spring-ifying async transactions.   this needs serious refactoring before next service method is added.
	 * suggestions : need to avoid creating runnables, need a DownloadService that handles all the link stuff automatically.  a couple of advices might make things cleaner.
	 * may need to implement MailManager for this stuff...ask matt.  
	 */
	
	@Transactional
	public DownloadLink exportEventTypeToExcel(Long userId, final EventType eventType, final String fileName) {
		System.out.println("calling service in thread " + Thread.currentThread().getId() + Thread.currentThread().getName());
		
		final User user = userService.getUser(userId);
		checkNotNull(user);		
		final String dateFormat = user.getOwner().getPrimaryOrg().getDateFormat();
		
		final DownloadLink link = createDownloadLink(user, fileName, ContentType.EXCEL);

		AsyncTask<?> task = asyncTaskFactory.createTask(new Callable<Void>() {
			@Override public Void call() { 
				generateEventByTypeExport(fileName, link.getId(), "FIXME DD : need to implement this downloadURL", eventType.getId(), dateFormat); 
				return null;  // Void 
			}
		});
		asyncService.run(task);		
		return link;
	}

	protected MapWriter createMapWriter(File downloadFile, String dateFormat) throws IOException {
		return new ExcelMapWriter(new FileOutputStream(downloadFile), dateFormat).withExcelSheetManager(new EventExcelSheetManager());
	}
	
	private void updateDownloadLinkState(Long linkId, DownloadState state) {
		DownloadLink downloadLink = persistenceService.find(DownloadLink.class, linkId);
		downloadLink.setState(state);
		persistenceService.save(downloadLink);
	}		
	
	private void generateEventByTypeExport(String fileName, Long downloadLinkId, String downloadUrl, Long eventTypeId, String dateFormat) {
		MapWriter mapWriter = null;
		try {
			updateDownloadLinkState(downloadLinkId, DownloadState.INPROGRESS);
			mapWriter = createMapWriter(new File(fileName), dateFormat);
			export(mapWriter, eventTypeId);
			updateDownloadLinkState(downloadLinkId, DownloadState.COMPLETED);
		} catch (Exception e) {
			updateDownloadLinkState(downloadLinkId, DownloadState.FAILED);
		} finally {
			StreamUtils.close(mapWriter);
		}
	}
								
	private void export(MapWriter excelMapWriter, Long eventTypeId) throws ConversionException, IOException, MarshalingException  {	
		System.out.println("starting task in thread " + Thread.currentThread().getId() + Thread.currentThread().getName() + "...");							
		ExportMapMarshaller<EventView> exportMapMarshaller = new ExportMapMarshaller<EventView>(EventView.class);
		ModelToViewConverter<Event,EventView> converter = new EventToViewConverter();
			
		List<Event> events = eventService.getEventsByType(eventTypeId);
		EventView view;				
		for (Event event:events) {
			view = converter.toView(event);
			excelMapWriter.write(exportMapMarshaller.toBeanMap(view));
		}			
		
		System.out.println("...finished task");
	}	

	private DownloadLink createDownloadLink(User user, String name, ContentType type) {
		DownloadLink link = new DownloadLink();
		link.setState(DownloadState.REQUESTED);
		link.setContentType(type);
		link.setTenant(user.getTenant());
		link.setUser(user);
		link.setName(name);
		persistenceService.save(link);
		return link;
	}			
	
	
	class EventExcelSheetManager extends ExcelSheetManager {
		private final String DEFICIENCIES = "Deficiencies";
		private final String RECOMMENDATIONS = "Recommendations";
		private final String EVENTS = "Events";
		private String[] sheetTitles = {EVENTS, RECOMMENDATIONS, DEFICIENCIES};			

		public EventExcelSheetManager() {
			super();			
		}
		
		@Override
		protected WritableSheet getSheetForColumn(String columnTitle) {			
			if (columnTitle.endsWith(CriteriaResultSerializationHandler.RECOMMENDATIONS_SUFFIX)) {
				return super.getSheetByName(RECOMMENDATIONS);
			} else if (columnTitle.endsWith(CriteriaResultSerializationHandler.DEFICIENCIES_SUFFIX)) {
				return super.getSheetByName(DEFICIENCIES);
			}
			return super.getSheetByName(EVENTS);
		}
		
		@Override
		protected String[] getSheetTitles() {
			return sheetTitles;
		}
		
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
}
