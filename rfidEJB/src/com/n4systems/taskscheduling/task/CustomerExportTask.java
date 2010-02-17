package com.n4systems.taskscheduling.task;

import java.io.File;
import java.io.FileOutputStream;

import rfid.ejb.entity.UserBean;

import com.n4systems.ejb.MailManager;
import com.n4systems.exporting.CustomerExporter;
import com.n4systems.exporting.Exporter;
import com.n4systems.exporting.io.MapWriter;
import com.n4systems.exporting.io.MapWriterFactory;
import com.n4systems.model.downloadlink.DownloadLink;
import com.n4systems.model.downloadlink.DownloadLinkSaver;
import com.n4systems.model.orgs.customer.CustomerOrgListLoader;
import com.n4systems.model.security.SecurityFilter;

public class CustomerExportTask extends DownloadTask {
	private static final String TEMPLATE = "customerExport";
	
	protected final MapWriterFactory writerFactory;
	protected final Exporter exporter;
	
	public CustomerExportTask(DownloadLink downloadLink, String downloadUrl, SecurityFilter filter) {
		super(downloadLink, downloadUrl, TEMPLATE);
		exporter = new CustomerExporter(new CustomerOrgListLoader(filter), filter);
		writerFactory = new MapWriterFactory();
	}
	
	public CustomerExportTask(DownloadLink downloadLink, String downloadUrl, DownloadLinkSaver linkSaver, MailManager mailManager, Exporter exporter, MapWriterFactory writerFactory) {
		super(downloadLink, downloadUrl, TEMPLATE, linkSaver, mailManager);
		this.exporter = exporter;
		this.writerFactory = writerFactory;
	}

	@Override
	protected void generateFile(File downloadFile, UserBean user, String downloadName) throws Exception {
		MapWriter mapWriter = writerFactory.create(new FileOutputStream(downloadFile), downloadLink.getContentType());

		exporter.export(mapWriter);

		mapWriter.close();
	}


	
}
