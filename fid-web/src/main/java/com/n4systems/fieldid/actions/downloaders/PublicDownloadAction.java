package com.n4systems.fieldid.actions.downloaders;


import java.io.File;
import java.io.FileNotFoundException;

import org.apache.struts2.interceptor.validation.SkipValidation;

import com.n4systems.ejb.PersistenceManager;
import com.n4systems.fieldid.actions.api.AbstractCrud;

public class PublicDownloadAction extends DownloadLinkAction{

	private Long fileId;
	private String downloadUrl;
	
	public PublicDownloadAction(PersistenceManager persistenceManager) {
		super(persistenceManager);
	}
	
	@SkipValidation
	public String doShow(){
		//checkIfExpired();
		downloadUrl="hurf"+fileId;
		
		return SUCCESS;
	}


	public Long getFileId() {
		return fileId;
	}

	public void setFileId(Long fileId) {
		this.fileId = fileId;
	}

	public String getDownloadUrl() {
		return downloadUrl;
	}

	@Override
	public File getFile() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getFileName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected boolean initializeDownload() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	protected String onFileNotFoundException(FileNotFoundException e) {
		// TODO Auto-generated method stub
		return null;
	}

	

}
