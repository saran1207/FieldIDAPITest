package com.n4systems.fieldid.actions;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.log4j.Logger;

import rfid.ejb.entity.UserBean;
import rfid.ejb.session.User;

import com.n4systems.ejb.DownloadManager;
import com.n4systems.ejb.PersistenceManager;
import com.n4systems.fieldid.actions.api.AbstractAction;
import com.opensymphony.xwork2.validator.annotations.RequiredStringValidator;

public class SecureFileAccess extends AbstractAction {
	private static final long serialVersionUID = 1L;
	private static final Logger logger = Logger.getLogger(SecureFileAccess.class);
	
	private DownloadManager downloadManager;
	private User userManager;
	private String downloadPath; 
	
	public SecureFileAccess(DownloadManager downloadManager, User userManager, PersistenceManager persistenceManager) {
		super(persistenceManager);
		this.downloadManager = downloadManager;
		this.userManager = userManager;
	}
	
	/*
	 * A quick note about this action.  This will cause an IllegalStateException to be
	 * thrown out of xwork/struts2.  I'm not exactly sure why it happens but I think it's 
	 * because struts2 is attempting to send down another header or more data after the download is complete.
	 * There's an actual stream result type for struts2 which we should probably move to at some point
	 */
	// XXX - move this to a stream result type
	public String doFileDownload() {
		byte[] buffer = new byte[1024 * 8];
		int readBytes, totalRead = 0;
		
		String fileName = new File(getDownloadPath()).getName();
			
		try {
			UserBean user = userManager.getUser(getSessionUser().getUniqueID());
			
			File downloadFile = downloadManager.getFile(getDownloadPath(), user);
			
			
			if (downloadFile.exists()) {
				InputStream fileIn = new FileInputStream(downloadFile);
				getServletResponse().setContentType("application/octet-stream");
				getServletResponse().setHeader( "Content-Disposition:", "inline; filename=\"" + fileName + "\"");
				getServletResponse().setContentLength((int)downloadFile.length());
	
				OutputStream respOut = getServletResponse().getOutputStream();
				
				while((readBytes = fileIn.read(buffer)) != -1) {
					respOut.write(buffer, 0, readBytes);
					totalRead += readBytes;
				}
				
				respOut.close();
				fileIn.close();
				return SUCCESS;
			}
			
		} catch(Exception e) {
			
			logger.error("could not download file", e);
			
		}
		
		addActionError("File download failed");
		
		return ERROR;
	}

	public String getDownloadPath() {
		return downloadPath;
	}
	
	@RequiredStringValidator( key="error.filerequired", message="" )
	public void setDownloadPath(String downloadPath) {
		this.downloadPath = downloadPath;
	}
}
