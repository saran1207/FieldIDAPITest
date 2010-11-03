package com.n4systems.taskscheduling.task;

import com.n4systems.exporting.Exporter;
import com.n4systems.mail.MailManager;
import com.n4systems.model.downloadlink.DownloadLink;
import com.n4systems.persistence.savers.Saver;

public class EventExportTask extends AbstractExportTask {
	private static final String TEMPLATE_NAME = "eventExport";

	public EventExportTask(DownloadLink downloadLink, String downloadUrl, Saver<DownloadLink> linkSaver, MailManager mailManager, Exporter exporter) {
		super(downloadLink, downloadUrl, TEMPLATE_NAME, linkSaver, mailManager, exporter);
	}

}
