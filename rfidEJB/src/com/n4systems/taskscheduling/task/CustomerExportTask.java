package com.n4systems.taskscheduling.task;

import com.n4systems.ejb.MailManager;
import com.n4systems.exporting.CustomerExporter;
import com.n4systems.exporting.Exporter;
import com.n4systems.exporting.io.MapWriterFactory;
import com.n4systems.model.downloadlink.DownloadLink;
import com.n4systems.model.downloadlink.DownloadLinkSaver;
import com.n4systems.model.orgs.customer.CustomerOrgListLoader;
import com.n4systems.model.security.SecurityFilter;

public class CustomerExportTask extends AbstractExportTask {
	private static final String TEMPLATE_NAME = "customerExport";
	
	public CustomerExportTask(DownloadLink downloadLink, String downloadUrl, String dateFormat, SecurityFilter filter) {
		super(downloadLink, downloadUrl, TEMPLATE_NAME, new CustomerExporter(new CustomerOrgListLoader(filter), filter), dateFormat);
	}
	
	public CustomerExportTask(DownloadLink downloadLink, String downloadUrl, DownloadLinkSaver linkSaver, MailManager mailManager, MapWriterFactory writerFactory, Exporter exporter) {
		super(downloadLink, downloadUrl, TEMPLATE_NAME, linkSaver, mailManager, writerFactory, exporter);
	}
	
}
