package com.n4systems.model.downloadlink;

import static org.easymock.EasyMock.*;

import java.util.concurrent.Executor;

import org.junit.Test;


import com.n4systems.model.AutoAttributeDefinition;
import com.n4systems.model.Asset;
import com.n4systems.model.builders.UserBuilder;
import com.n4systems.model.orgs.CustomerOrg;
import com.n4systems.model.security.OpenSecurityFilter;
import com.n4systems.model.security.SecurityFilter;
import com.n4systems.model.user.User;
import com.n4systems.persistence.loaders.ListLoader;
import com.n4systems.persistence.savers.Saver;
import com.n4systems.taskscheduling.task.AutoAttributeExportTask;
import com.n4systems.taskscheduling.task.CustomerExportTask;
import com.n4systems.taskscheduling.task.DownloadTaskFactory;
import com.n4systems.taskscheduling.task.ProductExportTask;

public class DownloadCoordinatorTest {

	@Test
	@SuppressWarnings("unchecked")
	public void generate_customer_export_creates_and_saves_download_link() {
		User user = UserBuilder.anEmployee().build();
		
		Saver<DownloadLink> linkSaver = createMock(Saver.class);
		DownloadLinkFactory linkFactory = createMock(DownloadLinkFactory.class);
		
		DownloadLink link = new DownloadLink();
		String downloadName = "name";
		
		DownloadCoordinator dc = new DownloadCoordinator(user, linkSaver, createMock(Executor.class), linkFactory, createMock(DownloadTaskFactory.class));
		
		expect(linkFactory.createDownloadLink(user, downloadName, ContentType.EXCEL)).andReturn(link);
		
		linkSaver.save(link);
		
		replay(linkFactory);
		replay(linkSaver);
		
		dc.generateCustomerExport(downloadName, null, null, null);
		
		verify(linkFactory);
		verify(linkSaver);
	}
	
	@Test
	@SuppressWarnings("unchecked")
	public void generate_auto_atribute_export_creates_and_saves_download_link() {
		User user = UserBuilder.anEmployee().build();
		
		Saver<DownloadLink> linkSaver = createMock(Saver.class);
		DownloadLinkFactory linkFactory = createMock(DownloadLinkFactory.class);
		
		DownloadLink link = new DownloadLink();
		String downloadName = "name";
		
		DownloadCoordinator dc = new DownloadCoordinator(user, linkSaver, createMock(Executor.class), linkFactory, createMock(DownloadTaskFactory.class));		
		
		expect(linkFactory.createDownloadLink(user, downloadName, ContentType.EXCEL)).andReturn(link);
		linkSaver.save(link);
		
		replay(linkFactory);
		replay(linkSaver);
		
		dc.generateAutoAttributeExport(downloadName, null, null);
		
		verify(linkFactory);
		verify(linkSaver);
	}
//	
	@Test
	@SuppressWarnings("unchecked")
	public void generate_product_export_creates_and_saves_download_link() {
		User user = UserBuilder.anEmployee().build();
		
		Saver<DownloadLink> linkSaver = createMock(Saver.class);
		DownloadLinkFactory linkFactory = createMock(DownloadLinkFactory.class);
		
		DownloadLink link = new DownloadLink();
		String downloadName = "name";
		
		DownloadCoordinator dc = new DownloadCoordinator(user, linkSaver, createMock(Executor.class), linkFactory, createMock(DownloadTaskFactory.class));
		
		expect(linkFactory.createDownloadLink(user, downloadName, ContentType.EXCEL)).andReturn(link);
		
		linkSaver.save(link);
		
		replay(linkFactory);
		replay(linkSaver);
		
		dc.generateProductExport(downloadName, null, null);
		
		verify(linkFactory);
		verify(linkSaver);
	}
	
	@Test
	@SuppressWarnings("unchecked")
	public void generate_customer_export_creates_and_executes_download_task() {
		Executor executor =  createMock(Executor.class);
		DownloadLinkFactory linkFactory = createMock(DownloadLinkFactory.class);
		DownloadTaskFactory taskFactory = createMock(DownloadTaskFactory.class);
		CustomerExportTask task = createMock(CustomerExportTask.class);
		ListLoader<CustomerOrg> loader = createMock(ListLoader.class);
		
		DownloadCoordinator dc = new DownloadCoordinator(new User(), createMock(Saver.class), executor, linkFactory, taskFactory);
		
		String url = "http://url";
		
		DownloadLink link = new DownloadLink();
		SecurityFilter filter = new OpenSecurityFilter();
		
		expect(linkFactory.createDownloadLink((User)anyObject(), (String)anyObject(), (ContentType)anyObject())).andReturn(link);
		
		expect(taskFactory.createCustomerExportTask(link, url, loader, filter)).andReturn(task);
		
		executor.execute(task);
		
		replay(linkFactory);
		replay(taskFactory);
		replay(executor);
		
		dc.generateCustomerExport(null, url, loader, filter);
		
		verify(taskFactory);
		verify(executor);
	}
	
	@Test
	@SuppressWarnings("unchecked")
	public void generate_auto_attribute_export_creates_and_executes_download_task() {
		Executor executor =  createMock(Executor.class);
		DownloadLinkFactory linkFactory = createMock(DownloadLinkFactory.class);
		DownloadTaskFactory taskFactory = createMock(DownloadTaskFactory.class);
		AutoAttributeExportTask task = createMock(AutoAttributeExportTask.class);
		ListLoader<AutoAttributeDefinition> loader = createMock(ListLoader.class);
		
		DownloadCoordinator dc = new DownloadCoordinator(new User(), createMock(Saver.class), executor, linkFactory, taskFactory);
		
		String url = "http://url";
		
		DownloadLink link = new DownloadLink();
		
		expect(linkFactory.createDownloadLink((User)anyObject(), (String)anyObject(), (ContentType)anyObject())).andReturn(link);
		
		expect(taskFactory.createAutoAttributeExportTask(link, url, loader)).andReturn(task);
		
		executor.execute(task);
		
		replay(linkFactory);
		replay(taskFactory);
		replay(executor);
		
		dc.generateAutoAttributeExport(null, url, loader);
		
		verify(taskFactory);
		verify(executor);
	}
	
	@Test
	@SuppressWarnings("unchecked")
	public void generate_product_export_creates_and_executes_download_task() {
		Executor executor =  createMock(Executor.class);
		DownloadLinkFactory linkFactory = createMock(DownloadLinkFactory.class);
		DownloadTaskFactory taskFactory = createMock(DownloadTaskFactory.class);
		ProductExportTask task = createMock(ProductExportTask.class);
		ListLoader<Asset> loader = createMock(ListLoader.class);
		
		DownloadCoordinator dc = new DownloadCoordinator(new User(), createMock(Saver.class), executor, linkFactory, taskFactory);
		
		String url = "http://url";
		
		DownloadLink link = new DownloadLink();
		
		expect(linkFactory.createDownloadLink((User)anyObject(), (String)anyObject(), (ContentType)anyObject())).andReturn(link);
		
		expect(taskFactory.createProductExportTask(link, url, loader)).andReturn(task);
		
		executor.execute(task);
		
		replay(linkFactory);
		replay(taskFactory);
		replay(executor);
		
		dc.generateProductExport(null, url, loader);
		
		verify(taskFactory);
		verify(executor);
	}
}
