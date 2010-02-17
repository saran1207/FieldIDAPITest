package com.n4systems.taskscheduling.task;
import static org.junit.Assert.*;

import org.junit.Test;

import com.n4systems.model.downloadlink.DownloadLink;
import com.n4systems.model.security.OpenSecurityFilter;

public class DownloadTaskFactoryTest {

	@Test
	public void test_create_customer_export_task() {
		DownloadTaskFactory dtf = new DownloadTaskFactory();
		
		DownloadLink link = new DownloadLink();
		
		CustomerExportTask task = dtf.createCustomerExportTask(link, "url", new OpenSecurityFilter());
		
		assertSame(link, task.downloadLink);
		assertEquals("url", task.downloadUrl);
	}
}
