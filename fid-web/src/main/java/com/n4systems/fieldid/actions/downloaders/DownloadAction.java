package com.n4systems.fieldid.actions.downloaders;

import com.n4systems.ejb.PersistenceManager;
import com.n4systems.fieldid.actions.api.AbstractAction;
import com.n4systems.fieldid.service.amazon.S3Service;
import com.n4systems.util.ContentTypeUtil;
import com.n4systems.util.ServiceLocator;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

@SuppressWarnings("serial")
public abstract class DownloadAction extends AbstractAction {

	private static Logger logger = Logger.getLogger(DownloadAction.class);
	protected Long uniqueID;
	protected String fileName;
	protected Integer fileSize;
	protected Long attachmentID;
    protected S3Service s3service;

	public DownloadAction(PersistenceManager persistenceManager) {
		super(persistenceManager);
        s3service = ServiceLocator.getS3Service();
	}

	protected String sendFile(InputStream stream) throws IOException {
		HttpServletResponse response = getServletResponse();
		response.setContentType(ContentTypeUtil.getContentType(fileName));
		response.addHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");
		
		if (fileSize != null) {
		response.setContentLength(fileSize);
		}

		try {
			OutputStream ouputStream = response.getOutputStream();
			IOUtils.copy(stream, ouputStream);
			ouputStream.flush();
			ouputStream.close();
            postSendActions();
		} catch (IOException e) {
			logger.error("Problem outputting for file " + fileName, e);
			throw e;
		}
		return null;
	}

	public Long getUniqueID() {
		return uniqueID;
	}

	public void setUniqueID(Long uniqueID) {
		this.uniqueID = uniqueID;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public Long getAttachmentID() {
		return attachmentID;
	}

	public void setAttachmentID(Long attachmentID) {
		this.attachmentID = attachmentID;
	}

    public void postSendActions() {}

}