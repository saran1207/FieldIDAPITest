package com.n4systems.fieldid.actions.downloaders;

import com.n4systems.ejb.PersistenceManager;
import com.n4systems.model.asset.AssetAttachment;
import org.apache.log4j.Logger;

import java.io.IOException;

public class DownloadSafetyNetworkAssetAttachmentFile extends AbstractS3DownloadAction {
	private static final long serialVersionUID = 1L;

    protected Long attachmentID;
	private Long assetNetworkId;
    private AssetAttachment attachment;

    private static final Logger logger = Logger.getLogger(DownloadSafetyNetworkAssetAttachmentFile.class);

	public DownloadSafetyNetworkAssetAttachmentFile(PersistenceManager persistenceManager) {
		super(persistenceManager);
	}

	protected AssetAttachment loadAssetAttachment() {
		return getLoaderFactory().createSafetyNetworkAssetAttachmentLoader().setAttachmentId(attachmentID).setAssetNetworkId(assetNetworkId).load();
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
    protected byte[] getFileBytes() {
        try {
            return s3Service.downloadAssetAttachmentBytes(attachment);
        } catch (IOException e) {
            logger.error("AssetAttachment with ID " + attachment.getId() + " is missing from S3!!");
            addActionError(getText("error.no_asset_attached_file", attachment.getFileName()));
            setFailActionResult(MISSING);
            return null;
        }
    }

    @Override
    public String getFileName() {
        return attachment.getFileName();
    }

	public void setUniqueID(Long uniqueID) {
		this.assetNetworkId = uniqueID;
	}

    public void setAttachmentID(Long attachmentID) {
        this.attachmentID = attachmentID;
    }

}
