package com.n4systems.taskscheduling.task;

import com.n4systems.exporting.AssetExporter;
import com.n4systems.exporting.AutoAttributeExporter;
import com.n4systems.exporting.CustomerExporter;
import com.n4systems.exporting.ExporterFactory;
import com.n4systems.model.Asset;
import com.n4systems.model.AutoAttributeDefinition;
import com.n4systems.model.downloadlink.DownloadLink;
import com.n4systems.model.orgs.CustomerOrg;
import com.n4systems.model.security.OpenSecurityFilter;
import com.n4systems.model.security.SecurityFilter;
import com.n4systems.persistence.loaders.ListLoader;
import com.n4systems.persistence.savers.Saver;
import com.n4systems.util.ConfigContextRequiredTestCase;
import org.junit.Test;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.*;

public class DownloadTaskFactoryTest extends ConfigContextRequiredTestCase {

	@SuppressWarnings("unchecked")
	@Test
	public void test_create_customer_export_task() {
		Saver<DownloadLink> linkSaver = createMock(Saver.class);
		ExporterFactory exporterFactory = createMock(ExporterFactory.class);
		CustomerExporter exporter = createMock(CustomerExporter.class);
		ListLoader<CustomerOrg> loader = createMock(ListLoader.class);
		
		DownloadLink link = new DownloadLink();
		SecurityFilter filter = new OpenSecurityFilter();
		
		DownloadTaskFactory dtf = new DownloadTaskFactory(linkSaver, exporterFactory);
		
		expect(exporterFactory.createCustomerExporter(loader, filter)).andReturn(exporter);
		
		replay(exporterFactory);
		
		CustomerExportTask task = dtf.createCustomerExportTask(link, "url", loader, filter);
		
		verify(exporterFactory);
		
		assertNotNull(task);
		assertSame(exporter, task.exporter);
		assertSame(linkSaver, task.linkSaver);
		assertSame(link, task.downloadLink);
		assertEquals("url", task.downloadUrl);
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void test_create_auto_attribute_export_task() {
		Saver<DownloadLink> linkSaver = createMock(Saver.class);
		ExporterFactory exporterFactory = createMock(ExporterFactory.class);
		AutoAttributeExporter exporter = createMock(AutoAttributeExporter.class);
		ListLoader<AutoAttributeDefinition> loader = createMock(ListLoader.class);
		
		DownloadLink link = new DownloadLink();
		
		DownloadTaskFactory dtf = new DownloadTaskFactory(linkSaver, exporterFactory);
		
		expect(exporterFactory.createAutoAttributeExporter(loader)).andReturn(exporter);
		
		replay(exporterFactory);
		
		AutoAttributeExportTask task = dtf.createAutoAttributeExportTask(link, "url", loader);
		
		verify(exporterFactory);
		
		assertNotNull(task);
		assertSame(exporter, task.exporter);
		assertSame(linkSaver, task.linkSaver);
		assertSame(link, task.downloadLink);
		assertEquals("url", task.downloadUrl);
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void test_create_product_export_task() {
		Saver<DownloadLink> linkSaver = createMock(Saver.class);
		ExporterFactory exporterFactory = createMock(ExporterFactory.class);
		AssetExporter exporter = createMock(AssetExporter.class);
		ListLoader<Asset> loader = createMock(ListLoader.class);
		
		DownloadLink link = new DownloadLink();
		
		DownloadTaskFactory dtf = new DownloadTaskFactory(linkSaver, exporterFactory);
		
		expect(exporterFactory.createAssetExporter(loader, null)).andReturn(exporter);
		
		replay(exporterFactory);
		
		AssetExportTask task = dtf.createAssetExportTask(link, "url", loader, null);
		
		verify(exporterFactory);
		
		assertNotNull(task);
		assertSame(exporter, task.exporter);
		assertSame(linkSaver, task.linkSaver);
		assertSame(link, task.downloadLink);
		assertEquals("url", task.downloadUrl);
	}
}
