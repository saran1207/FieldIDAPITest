package com.n4systems.fieldid.actions.downloaders;

import com.n4systems.ejb.PersistenceManager;
import com.n4systems.model.asset.AssetAttachment;
import org.apache.log4j.Logger;

import java.io.IOException;


public class DownloadAssetAttachmentFile extends AbstractS3DownloadAction {
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

    protected byte[] getFileBytes() {
        try {
            return s3Service.downloadAssetAttachmentBytes(attachment);
        } catch (IOException e) {
            logger.error("Asset Attachment File with ID " + attachment.getId() + " not found!!", e);
            addActionError(getText("error.no_asset_attached_file", attachment.getFileName()));
            setFailActionResult(MISSING);
            return null;
        }
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
