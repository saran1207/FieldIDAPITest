package com.n4systems.fieldid.actions.downloaders;

import java.io.File;
import java.io.FileNotFoundException;

import com.n4systems.ejb.PersistenceManager;
import com.n4systems.model.FileAttachment;
import com.n4systems.reporting.PathHandler;

public class DownloadAssetTypeAttachedFile extends AbstractDownloadAction {
	private static final long serialVersionUID = 1L;
	
	protected Long assetTypeId;
	protected Long attachmentID;
	private FileAttachment attachment;
	
	public DownloadAssetTypeAttachedFile(PersistenceManager persistenceManager) {
		super(persistenceManager);
	}
	
	protected FileAttachment loadFileAttachment() {
		return getLoaderFactory().createFileAttachmentLoader().setId(attachmentID).load();
	}

	@Override
	protected boolean initializeDownload() {
		attachment = loadFileAttachment();

		if(attachment == null) {
			addActionError(getText("error.noassettypeattachedfile"));
			setFailActionResult(MISSING);
			return false;
		}

		return true;
	}

	@Override
	protected String onFileNotFoundException(FileNotFoundException e) {
		addActionError(getText("error.noassettypeattachedfile"));
		return MISSING;
	}
	
	@Override
	public File getFile() {
		return PathHandler.getAssetTypeAttachmentFile(attachment, assetTypeId);
	}
	
	@Override
	public String getFileName() {
		return attachment.getFileName();
	}
	
	public void setAttachmentID(Long attachmentID) {
		this.attachmentID = attachmentID;
	}
	
	public void setUniqueID(Long uniqueID) {
		assetTypeId = uniqueID;
	}
	
	// Required by the common/_attachedFilesList.ftl but not needed here
	public void setFileName() {}
	
}
