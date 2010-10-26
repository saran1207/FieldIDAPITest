package com.n4systems.fieldid.actions.downloaders;

import java.io.File;
import java.io.FileNotFoundException;

import com.n4systems.ejb.PersistenceManager;
import com.n4systems.model.product.AssetAttachment;
import com.n4systems.reporting.PathHandler;

public class DownloadAssetAttachmentFile extends AbstractDownloadAction {
	private static final long serialVersionUID = 1L;

	protected Long attachmentID;
	private AssetAttachment attachment;
	
	public DownloadAssetAttachmentFile(PersistenceManager persistenceManager) {
		super(persistenceManager);
	}
	
	protected AssetAttachment loadProductAttachment() {
		return getLoaderFactory().createFilteredIdLoader(AssetAttachment.class).setId(attachmentID).load();
	}
	
	@Override
	protected boolean initializeDownload() {
		attachment = loadProductAttachment();

		if (attachment == null) {
			addActionError(getText("error.no_asset_attached_file"));
			setFailActionResult(MISSING);
			return false;
		}
		
		return true;
	}

	@Override
	protected String onFileNotFoundException(FileNotFoundException e) {
		addActionError(getText("error.no_asset_attached_file", attachment.getFileName()));
		return MISSING;
	}

	@Override
	public File getFile() {
		return PathHandler.getProductAttachmentFile(attachment);
	}

	@Override
	public String getFileName() {
		return attachment.getFileName();
	}

	public void setAttachmentID(Long attachmentID) {
		this.attachmentID = attachmentID;
	}
	
	// required by common/_attachedFilesList.ftl but not used here
	public void setUniqueID(Long uniqueID) {}

}
