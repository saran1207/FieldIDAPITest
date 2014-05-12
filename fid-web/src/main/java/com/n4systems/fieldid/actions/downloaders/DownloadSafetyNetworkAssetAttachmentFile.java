package com.n4systems.fieldid.actions.downloaders;

import com.n4systems.ejb.PersistenceManager;
import com.n4systems.model.asset.AssetAttachment;
import com.n4systems.reporting.PathHandler;

import java.io.File;
import java.io.FileNotFoundException;

public class DownloadSafetyNetworkAssetAttachmentFile extends AbstractDownloadAction {
	private static final long serialVersionUID = 1L;

    protected Long attachmentID;
	private Long assetNetworkId;
    private AssetAttachment attachment;

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
    protected String onFileNotFoundException(FileNotFoundException e) {
        addActionError(getText("error.no_asset_attached_file", attachment.getFileName()));
        return MISSING;
    }

    @Override
    public File getFile() {
        return PathHandler.getAssetAttachmentFile(attachment);
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
