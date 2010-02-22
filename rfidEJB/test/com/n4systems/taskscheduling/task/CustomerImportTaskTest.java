package com.n4systems.taskscheduling.task;

import static org.easymock.EasyMock.*;
import static org.easymock.classextension.EasyMock.*;
import static org.junit.Assert.*;

import org.junit.Test;

import rfid.ejb.entity.UserBean;

import com.n4systems.exporting.ImportException;
import com.n4systems.exporting.Importer;
import com.n4systems.model.builders.UserBuilder;
import com.n4systems.notifiers.Notifier;
import com.n4systems.notifiers.notifications.Notification;

public class CustomerImportTaskTest {

	@Test
	public void constructor_generates_id() {
		CustomerImportTask task = new CustomerImportTask(null, null, null);
		
		assertNotNull(task.getId());
	}
	
	@Test
	public void test_run() throws ImportException {
		UserBean user = UserBuilder.anEmployee().build();
		Importer importer = createMock(Importer.class);
		Notifier notifier = createMock(Notifier.class);
		
		CustomerImportTask task = new CustomerImportTask(importer, user, notifier);
		
		expect(importer.runImport()).andReturn(5);
		expect(notifier.notify((Notification)anyObject())).andReturn(true);
				
		replay(importer);
		replay(notifier);
		
		task.run();
		
		verify(importer);
		verify(notifier);
	}
}
