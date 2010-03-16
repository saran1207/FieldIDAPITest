package com.n4systems.model.downloadlink;

import static org.easymock.EasyMock.*;
import static org.easymock.classextension.EasyMock.*;

import java.util.concurrent.Executor;

import org.junit.Test;

import rfid.ejb.entity.UserBean;

import com.n4systems.model.AutoAttributeDefinition;
import com.n4systems.model.builders.UserBuilder;
import com.n4systems.model.security.OpenSecurityFilter;
import com.n4systems.model.security.SecurityFilter;
import com.n4systems.persistence.loaders.ListLoader;
import com.n4systems.persistence.savers.Saver;
import com.n4systems.taskscheduling.task.AutoAttributeExportTask;
import com.n4systems.taskscheduling.task.CustomerExportTask;
import com.n4systems.taskscheduling.task.DownloadTaskFactory;

public class DownloadCoordinatorTest {

	
	@Test
	@SuppressWarnings("unchecked")
	public void generate_customer_export_creates_and_saves_download_link() {
		UserBean user = UserBuilder.anEmployee().build();
		
		Saver<DownloadLink> linkSaver = createMock(Saver.class);
		DownloadLinkFactory linkFactory = createMock(DownloadLinkFactory.class);
		
		DownloadLink link = new DownloadLink();
		
		DownloadCoordinator dc = new DownloadCoordinator(user, linkSaver, createMock(Executor.class), linkFactory, createMock(DownloadTaskFactory.class));
		
		expect(linkFactory.createDownloadLink(user, "name", ContentType.EXCEL)).andReturn(link);
		
		linkSaver.save(link);
		
		replay(linkFactory);
		replay(linkSaver);
		
		dc.generateCustomerExport("name", null, ContentType.EXCEL, null, null);
		
		verify(linkFactory);
		verify(linkSaver);
	}
	
	@Test
	@SuppressWarnings("unchecked")
	public void generate_customer_export_creates_and_executes_download_task() {
		Executor executor =  createMock(Executor.class);
		DownloadLinkFactory linkFactory = createMock(DownloadLinkFactory.class);
		DownloadTaskFactory taskFactory = createMock(DownloadTaskFactory.class);
		
		DownloadCoordinator dc = new DownloadCoordinator(new UserBean(), createMock(Saver.class), executor, linkFactory, taskFactory);
		
		String url = "http://url";
		String dateFormat = "yyyy-MM-dd";
		
		DownloadLink link = new DownloadLink();
		SecurityFilter filter = new OpenSecurityFilter();
		CustomerExportTask task = new CustomerExportTask(new DownloadLink(), url, dateFormat, new OpenSecurityFilter());
		
		expect(linkFactory.createDownloadLink((UserBean)anyObject(), (String)anyObject(), (ContentType)anyObject())).andReturn(link);
		
		expect(taskFactory.createCustomerExportTask(link, url, dateFormat, filter)).andReturn(task);
		
		executor.execute(task);
		
		replay(linkFactory);
		replay(taskFactory);
		replay(executor);
		
		dc.generateCustomerExport(null, url, ContentType.EXCEL, dateFormat, filter);
		
		verify(taskFactory);
		verify(executor);
	}
	
	@Test
	@SuppressWarnings("unchecked")
	public void generate_auto_attribute_export_creates_and_executes_download_task() {
		Executor executor =  createMock(Executor.class);
		DownloadLinkFactory linkFactory = createMock(DownloadLinkFactory.class);
		DownloadTaskFactory taskFactory = createMock(DownloadTaskFactory.class);
		
		DownloadCoordinator dc = new DownloadCoordinator(new UserBean(), createMock(Saver.class), executor, linkFactory, taskFactory);
		
		String url = "http://url";
		String dateFormat = "yyyy-MM-dd";
		
		DownloadLink link = new DownloadLink();
		ListLoader<AutoAttributeDefinition> attribLoader = createMock(ListLoader.class);
		
		AutoAttributeExportTask task = new AutoAttributeExportTask(new DownloadLink(), url, dateFormat, attribLoader);
		
		expect(linkFactory.createDownloadLink((UserBean)anyObject(), (String)anyObject(), (ContentType)anyObject())).andReturn(link);
		
		expect(taskFactory.createAutoAttributeExportTask(link, url, dateFormat, attribLoader)).andReturn(task);
		
		executor.execute(task);
		
		replay(linkFactory);
		replay(taskFactory);
		replay(executor);
		
		dc.generateAutoAttributeExport(null, url, ContentType.EXCEL, dateFormat, attribLoader);
		
		verify(taskFactory);
		verify(executor);
	}
}
