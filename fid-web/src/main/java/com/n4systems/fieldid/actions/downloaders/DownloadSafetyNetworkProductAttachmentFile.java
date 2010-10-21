package com.n4systems.fieldid.actions.downloaders;

import com.n4systems.ejb.PersistenceManager;
import com.n4systems.model.product.ProductAttachment;

public class DownloadSafetyNetworkProductAttachmentFile extends DownloadProductAttachmentFile {
	private static final long serialVersionUID = 1L;
	
	private Long assetNetworkId;
	
	public DownloadSafetyNetworkProductAttachmentFile(PersistenceManager persistenceManager) {
		super(persistenceManager);
	}
	
	@Override
	protected ProductAttachment loadProductAttachment() {
		return getLoaderFactory().createSafetyNetworkProductAttachmentLoader().setAttachmentId(attachmentID).setProductNetworkId(assetNetworkId).load();
	}
	
	@Override
	public void setUniqueID(Long uniqueID) {
		this.assetNetworkId = uniqueID;
	}

}
