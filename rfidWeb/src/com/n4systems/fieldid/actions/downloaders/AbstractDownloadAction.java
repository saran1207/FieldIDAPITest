package com.n4systems.fieldid.actions.downloaders;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import javax.activation.FileTypeMap;

import com.n4systems.ejb.PersistenceManager;
import com.n4systems.fieldid.actions.api.AbstractAction;

public abstract class AbstractDownloadAction extends AbstractAction {
	private static final long serialVersionUID = 1L;
	
	private String failActionResult = ERROR;
	private String successActionResult = SUCCESS;
	private InputStream fileStream;
	private String fileSize;
	
	public AbstractDownloadAction(PersistenceManager persistenceManager) {
		super(persistenceManager);
	}

	/**
	 * Allows extending actions to initialize their fields.  Called immediately
	 * in the doDownload() action.  If initialization has succeeded, return true for 
	 * the download to proceed.  Returning false will cause the download action to 
	 * fail, returning a result of {@link AbstractDownloadAction#failActionResult}.
	 * 
	 * @return True if the download is able to proceed.  False otherwise.
	 */
	protected abstract boolean initializeDownload();
	
	/**
	 * Called when a FileNotFoundException is thrown while opening the InputStream
	 * 
	 * @param e FileNotFoundException
	 * @return String to be used for the action result
	 */
	protected abstract String onFileNotFoundException(FileNotFoundException e);
	
	/** @return String filename to be used for content disposition */
	public abstract String getFileName();
	
	/** @return File path to the download file */
	public abstract File getFile();
	
	public String doDownload() {
		/*
		 * this should be called asap to ensure the implemented methods
		 * have values
		 */ 
		if (!initializeDownload()) { 
			return failActionResult;
		}
		
		File downloadFile = getFile();
		fileSize = String.valueOf(downloadFile.length());
		
		try {
			fileStream = new FileInputStream(downloadFile);
		} catch(FileNotFoundException e) {
			return onFileNotFoundException(e);
		}
		
		return successActionResult;
	}
	
	protected void setSuccessActionResult(String defaultActionResult) {
		this.successActionResult = defaultActionResult;
	}
	
	protected void setFailActionResult(String failActionResult) {
		this.failActionResult = failActionResult;
	}

	public String getFileSize() {
		return fileSize;
	}

	public String getContentType() {
		return FileTypeMap.getDefaultFileTypeMap().getContentType(getFileName());
	}
	
	public InputStream getFileStream() {
		return fileStream;
	}

}
