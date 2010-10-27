package com.n4systems.fieldid.actions.downloaders;

import com.n4systems.ejb.PersistenceManager;
import com.n4systems.model.FileAttachment;

public class DownloadSafetyNetworkAssetTypeAttachedFile extends DownloadAssetTypeAttachedFile {
	private static final long serialVersionUID = 1L;
	
	private Long assetNetworkId;
	
	public DownloadSafetyNetworkAssetTypeAttachedFile(PersistenceManager persistenceManager) {
		super(persistenceManager);
	}

	@Override
	protected FileAttachment loadFileAttachment() {
		return getLoaderFactory().createSafetyNetworkAttachmentLoader().setId(attachmentID).setAssetNetworkId(assetNetworkId).load();
	}
	
	protected Long loadAssetTypeId() {
		return getLoaderFactory().createAssetTypeByAttachmentLoader().setAttachmentId(attachmentID).load();
	}
	
	@Override
	protected boolean initializeDownload() {
		assetTypeId = loadAssetTypeId();
		
		if(assetTypeId == null) {
			addActionError(getText("error.noassettypeattachedfile"));
			setFailActionResult(MISSING);
			return false;
		}
		
		return super.initializeDownload();
	}

	@Override
	public void setUniqueID(Long uniqueID) {
		assetNetworkId = uniqueID;
	}

}
