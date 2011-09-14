package com.n4systems.fieldid.service.export;

import static com.google.common.base.Preconditions.*;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.n4systems.fieldid.service.FieldIdPersistenceService;
import com.n4systems.fieldid.service.task.AsyncService;
import com.n4systems.fieldid.service.task.AsyncTaskFactory;
import com.n4systems.fieldid.service.task.AsyncTaskFactory.AsyncTask;
import com.n4systems.fieldid.service.user.UserService;
import com.n4systems.model.EventType;
import com.n4systems.model.downloadlink.ContentType;
import com.n4systems.model.downloadlink.DownloadLink;
import com.n4systems.model.downloadlink.DownloadState;
import com.n4systems.model.user.User;

public class ExportService extends FieldIdPersistenceService {
	
	@Autowired private UserService userService;
	@Autowired private AsyncService asyncService;
	@Autowired private AsyncTaskFactory asyncTaskFactory; 
	
	@Transactional
	public DownloadLink exportEventTypeToExcel(Long userId, final EventType eventType, String fileName) {
		System.out.println("calling service in thread " + Thread.currentThread().getId() + Thread.currentThread().getName());
		User user = userService.getUser(userId);
		checkNotNull(user);		
		DownloadLink link = createDownloadLink(user, fileName, ContentType.EXCEL);

		AsyncTask<?> task = asyncTaskFactory.createTask(new Runnable() {
			@Override public void run() { generateEventByTypeExport("foo", "barURL", eventType.getId()); }
		});
		asyncService.run(task);		
		return link;
	}
	
	private void generateEventByTypeExport(String name, String downloadUrl, Long eventTypeId) {
		try {				
			System.out.println("starting task in thread " + Thread.currentThread().getId() + Thread.currentThread().getName() + "...");			
			List<User> users = userService.getUsers(true, true);			
			System.out.println("...finished task");			
		} catch (Exception e) { 
			e.printStackTrace();
		}
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
