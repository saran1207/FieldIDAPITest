package com.n4systems.taskscheduling.task;

import com.n4systems.exporting.Exporter;
import com.n4systems.exporting.io.ExcelMapWriter;
import com.n4systems.exporting.io.ExcelXSSFMapWriter;
import com.n4systems.exporting.io.MapWriter;
import com.n4systems.mail.MailManager;
import com.n4systems.model.downloadlink.DownloadLink;
import com.n4systems.model.user.User;
import com.n4systems.model.utils.DateTimeDefiner;
import com.n4systems.model.utils.StreamUtils;
import com.n4systems.persistence.savers.Saver;

import java.io.*;
import java.util.TimeZone;

public class AbstractExportTask extends DownloadTask {
	protected final Exporter exporter;
	
	public AbstractExportTask(DownloadLink downloadLink, String downloadUrl, String templateName, Saver<DownloadLink> linkSaver, MailManager mailManager, Exporter exporter) {
		super(downloadLink, downloadUrl, templateName, linkSaver, mailManager);
		this.exporter = exporter;
	}

	@Override
	protected void generateFile(OutputStream fileContents, User user, String downloadName) throws Exception {
		MapWriter mapWriter = null;
		try {
			mapWriter = createXSSFMapWriter(user);
			exporter.export(mapWriter);
			((ExcelXSSFMapWriter)mapWriter).writeToStream(fileContents);
		} finally {
			StreamUtils.close(mapWriter);
		}
	}
	
	protected OutputStream getFileStream(File downloadFile) throws FileNotFoundException {
		return new FileOutputStream(downloadFile);
	}

	//No longer useful, we're using a new type of mapwriter...
	@Deprecated
	protected MapWriter createMapWriter(OutputStream fileContents, User user) throws IOException {
		return new ExcelMapWriter(fileContents, getDateFormat(user), getTimeZone(user));
	}

	protected MapWriter createXSSFMapWriter(User user) {
		return new ExcelXSSFMapWriter(new DateTimeDefiner(user));
	}

	protected MapWriter createMapWriter(File downloadFile, User user) throws IOException {
		return new ExcelMapWriter(getFileStream(downloadFile), getDateFormat(user), getTimeZone(user));
	}
	
	protected String getDateFormat(User user) {
		return user.getOwner().getPrimaryOrg().getDateFormat();
	}
    
    protected TimeZone getTimeZone(User user) {
        return user.getTimeZone();
    }

}
