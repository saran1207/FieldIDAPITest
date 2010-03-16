package com.n4systems.taskscheduling.task;

import com.n4systems.ejb.MailManager;
import com.n4systems.exporting.AutoAttributeExporter;
import com.n4systems.exporting.Exporter;
import com.n4systems.exporting.io.MapWriterFactory;
import com.n4systems.model.AutoAttributeDefinition;
import com.n4systems.model.downloadlink.DownloadLink;
import com.n4systems.model.downloadlink.DownloadLinkSaver;
import com.n4systems.persistence.loaders.ListLoader;

public class AutoAttributeExportTask extends AbstractExportTask {
	private static final String TEMPLATE_NAME = "autoAttributeExport";
	
	public AutoAttributeExportTask(DownloadLink downloadLink, String downloadUrl, String dateFormat, ListLoader<AutoAttributeDefinition> attribLoader) {
		super(downloadLink, downloadUrl, TEMPLATE_NAME, new AutoAttributeExporter(attribLoader), dateFormat);
	}
	
	public AutoAttributeExportTask(DownloadLink downloadLink, String downloadUrl, DownloadLinkSaver linkSaver, MailManager mailManager, MapWriterFactory writerFactory, Exporter exporter) {
		super(downloadLink, downloadUrl, TEMPLATE_NAME, linkSaver, mailManager, writerFactory, exporter);
	}

}
