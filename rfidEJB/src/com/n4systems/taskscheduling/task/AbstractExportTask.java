package com.n4systems.taskscheduling.task;

import java.io.File;
import java.io.FileOutputStream;

import rfid.ejb.entity.UserBean;

import com.n4systems.ejb.MailManager;
import com.n4systems.exporting.Exporter;
import com.n4systems.exporting.io.MapWriter;
import com.n4systems.exporting.io.MapWriterFactory;
import com.n4systems.model.downloadlink.DownloadLink;
import com.n4systems.model.downloadlink.DownloadLinkSaver;

public class AbstractExportTask extends DownloadTask {
	private final MapWriterFactory writerFactory;
	private final Exporter exporter;
	
	public AbstractExportTask(DownloadLink downloadLink, String downloadUrl, String templateName, Exporter exporter, String dateFormat) {
		super(downloadLink, downloadUrl, templateName);
		this.exporter = exporter;
		this.writerFactory = new MapWriterFactory(dateFormat);
	}
	
	public AbstractExportTask(DownloadLink downloadLink, String downloadUrl, String templateName, DownloadLinkSaver linkSaver, MailManager mailManager, MapWriterFactory writerFactory, Exporter exporter) {
		super(downloadLink, downloadUrl, templateName, linkSaver, mailManager);
		this.writerFactory = writerFactory;
		this.exporter = exporter;
	}

	@Override
	protected void generateFile(File downloadFile, UserBean user, String downloadName) throws Exception {
		MapWriter mapWriter = null;
		try {
			mapWriter = writerFactory.create(new FileOutputStream(downloadFile), downloadLink.getContentType());
	
			exporter.export(mapWriter);
		} finally {
			if (mapWriter != null) {
				mapWriter.close();
			}
		}
	}

}
