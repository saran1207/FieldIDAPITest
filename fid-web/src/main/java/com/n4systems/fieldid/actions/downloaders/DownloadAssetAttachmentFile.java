package com.n4systems.fieldid.actions.downloaders;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import com.n4systems.ejb.PersistenceManager;
import com.n4systems.model.asset.AssetAttachment;
import com.n4systems.reporting.PathHandler;
import org.apache.log4j.Logger;


public class DownloadAssetAttachmentFile extends AbstractDownloadAction {
    private static final long serialVersionUID = 1L;
    private static final Logger logger = Logger.getLogger(DownloadAssetAttachmentFile.class);

	protected Long attachmentID;
	private AssetAttachment attachment;

    public DownloadAssetAttachmentFile(PersistenceManager persistenceManager) {
		super(persistenceManager);
	}
	
	protected AssetAttachment loadAssetAttachment() {
		return getLoaderFactory().createFilteredIdLoader(AssetAttachment.class).setId(attachmentID).load();
	}
	
	@Override
	protected boolean initializeDownload() {
		attachment = loadAssetAttachment();

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
        File assetAttachmentFile = s3Service.downloadAssetAttachmentFile(attachment);
        return assetAttachmentFile;
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
