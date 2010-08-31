package com.n4systems.fieldid.actions.downloaders;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.activation.FileTypeMap;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;

import com.n4systems.ejb.PersistenceManager;
import com.n4systems.fieldid.actions.api.AbstractAction;

@SuppressWarnings("serial")
public abstract class DownloadAction extends AbstractAction {

	private static Logger logger = Logger.getLogger( DownloadAttachedInspectionFile.class );
	protected Long uniqueID;
	protected String fileName;
	protected Integer fileSize;
	protected Long attachmentID;
	protected boolean forceDownload = true;
	
	public DownloadAction(PersistenceManager persistenceManager) {
		super(persistenceManager);
	}
	
	public abstract String doDownload();

	protected String sendFile( InputStream stream ) throws IOException {
		HttpServletResponse response = getServletResponse();
		
		response.setContentType( FileTypeMap.getDefaultFileTypeMap().getContentType(fileName) );
		if (forceDownload) {
			response.addHeader( "Content-Disposition:", "inline; filename=\"" + fileName + "\"" );
		}
		if( fileSize != null ) {
			response.setContentLength( fileSize );
		}
		
		try {
			OutputStream ouputStream = response.getOutputStream();
			IOUtils.copy( stream, ouputStream );
			ouputStream.flush();
			ouputStream.close();
		} catch (IOException e) {
			logger.error("Problem outputting for file " + fileName, e);
			throw e;
		} 
		return null;
	}

	public Long getUniqueID() {
		return uniqueID;
	}

	public void setUniqueID( Long uniqueID ) {
		this.uniqueID = uniqueID;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName( String fileName ) {
		this.fileName = fileName;
	}

	public Long getAttachmentID() {
		return attachmentID;
	}

	public void setAttachmentID(Long attachmentID) {
		this.attachmentID = attachmentID;
	}

	
}