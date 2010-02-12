package com.n4systems.taskscheduling.task;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import rfid.ejb.entity.UserBean;

import com.n4systems.ejb.MailManager;
import com.n4systems.exporting.CustomerExporter;
import com.n4systems.exporting.Exporter;
import com.n4systems.exporting.io.CsvMapWriter;
import com.n4systems.exporting.io.ExcelMapWriter;
import com.n4systems.exporting.io.MapWriter;
import com.n4systems.model.downloadlink.DownloadLink;
import com.n4systems.model.downloadlink.DownloadLinkSaver;
import com.n4systems.model.orgs.customer.CustomerOrgListLoader;
import com.n4systems.model.security.SecurityFilter;

public class CustomerExportTask extends DownloadTask {
	private static final String TEMPLATE = "customerExport";
	
	private final Exporter exporter;
	
	public CustomerExportTask(DownloadLink downloadLink, String downloadUrl, SecurityFilter filter) {
		super(downloadLink, downloadUrl, TEMPLATE);
		exporter = new CustomerExporter(new CustomerOrgListLoader(filter), filter);
	}
	
	public CustomerExportTask(DownloadLink downloadLink, String downloadUrl, DownloadLinkSaver linkSaver, MailManager mailManager, Exporter exporter) {
		super(downloadLink, downloadUrl, TEMPLATE, linkSaver, mailManager);
		this.exporter = exporter;
	}

	@Override
	protected void generateFile(File downloadFile, UserBean user, String downloadName) throws Exception {
		MapWriter mapWriter = createWriter(new FileOutputStream(downloadFile));

		exporter.export(mapWriter);

		mapWriter.close();
	}

	private MapWriter createWriter(OutputStream out) throws IOException {
		switch (downloadLink.getContentType()) {
			case EXCEL:
				return new ExcelMapWriter(out);
			case CSV:
				return new CsvMapWriter(out);
			default:
				throw new IllegalArgumentException("Invalid export content type [" + downloadLink.getContentType().name() + "]");
		}
	}
	
}
