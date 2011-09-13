package com.n4systems.fieldid.service.export;

import static com.google.common.base.Preconditions.*;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.n4systems.fieldid.service.FieldIdPersistenceService;
import com.n4systems.fieldid.service.event.EventService;
import com.n4systems.fieldid.service.task.AsyncService;
import com.n4systems.fieldid.service.user.UserService;
import com.n4systems.model.Event;
import com.n4systems.model.EventType;
import com.n4systems.model.downloadlink.ContentType;
import com.n4systems.model.downloadlink.DownloadLink;
import com.n4systems.model.downloadlink.DownloadState;
import com.n4systems.model.user.User;

public class ExportService extends FieldIdPersistenceService {
	
	@Autowired private UserService userService;
	@Autowired private EventService eventService;
	@Autowired private AsyncService asyncService;
	
	@Transactional
	public DownloadLink exportEventTypeToExcel(Long userId, EventType eventType, String fileName) {
		User user = userService.getUser(userId);
		checkNotNull(user);		
		DownloadLink link = createDownloadLink(user, fileName, ContentType.EXCEL);		
		List<Event> eventsByType = eventService.getEventsByType(eventType);		
		asyncService.generateEventByTypeExport(fileName, "fixme..get downloadURL.", eventsByType);
		System.out.println("returning link  " + link.getId());			
		return link;
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
	
}
