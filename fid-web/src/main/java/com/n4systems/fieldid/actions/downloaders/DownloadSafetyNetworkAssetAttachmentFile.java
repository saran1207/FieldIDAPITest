package com.n4systems.fieldid.actions.downloaders;

import com.n4systems.ejb.PersistenceManager;
import com.n4systems.model.product.AssetAttachment;

public class DownloadSafetyNetworkAssetAttachmentFile extends DownloadAssetAttachmentFile {
	private static final long serialVersionUID = 1L;
	
	private Long assetNetworkId;
	
	public DownloadSafetyNetworkAssetAttachmentFile(PersistenceManager persistenceManager) {
		super(persistenceManager);
	}
	
	@Override
	protected AssetAttachment loadProductAttachment() {
		return getLoaderFactory().createSafetyNetworkProductAttachmentLoader().setAttachmentId(attachmentID).setProductNetworkId(assetNetworkId).load();
	}
	
	@Override
	public void setUniqueID(Long uniqueID) {
		this.assetNetworkId = uniqueID;
	}

}
