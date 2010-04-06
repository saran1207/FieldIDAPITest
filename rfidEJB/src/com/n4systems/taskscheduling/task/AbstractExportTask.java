package com.n4systems.taskscheduling.task;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import rfid.ejb.entity.UserBean;

import com.n4systems.exporting.Exporter;
import com.n4systems.exporting.io.ExcelMapWriter;
import com.n4systems.exporting.io.MapWriter;
import com.n4systems.mail.MailManager;
import com.n4systems.model.downloadlink.DownloadLink;
import com.n4systems.model.utils.StreamUtils;
import com.n4systems.persistence.savers.Saver;

public class AbstractExportTask extends DownloadTask {
	protected final Exporter exporter;
	
	public AbstractExportTask(DownloadLink downloadLink, String downloadUrl, String templateName, Saver<DownloadLink> linkSaver, MailManager mailManager, Exporter exporter) {
		super(downloadLink, downloadUrl, templateName, linkSaver, mailManager);
		this.exporter = exporter;
	}

	@Override
	protected void generateFile(File downloadFile, UserBean user, String downloadName) throws Exception {
		MapWriter mapWriter = null;
		try {
			mapWriter = createMapWriter(downloadFile, user);
			exporter.export(mapWriter);
		} finally {
			StreamUtils.close(mapWriter);
		}
	}
	
	protected OutputStream getFileStream(File downloadFile) throws FileNotFoundException {
		return new FileOutputStream(downloadFile);
	}
	
	protected MapWriter createMapWriter(File downloadFile, UserBean user) throws IOException {
		return new ExcelMapWriter(getFileStream(downloadFile), getDateFormat(user));
	}
	
	protected String getDateFormat(UserBean user) {
		return user.getOwner().getPrimaryOrg().getDateFormat();
	}

}
