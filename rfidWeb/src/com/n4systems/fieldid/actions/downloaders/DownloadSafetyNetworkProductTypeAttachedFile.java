package com.n4systems.fieldid.actions.downloaders;

import com.n4systems.ejb.PersistenceManager;
import com.n4systems.model.FileAttachment;

public class DownloadSafetyNetworkProductTypeAttachedFile extends DownloadProductTypeAttachedFile {
	private static final long serialVersionUID = 1L;
	
	private Long productNetworkId;
	
	public DownloadSafetyNetworkProductTypeAttachedFile(PersistenceManager persistenceManager) {
		super(persistenceManager);
	}

	@Override
	protected FileAttachment loadFileAttachment() {
		return getLoaderFactory().createSafetyNetworkAttachmentLoader().setId(attachmentID).setProductNetworkId(productNetworkId).load();
	}
	
	protected Long loadProductTypeId() {
		return getLoaderFactory().createProductTypeByAttachmentLoader().setAttachmentId(attachmentID).load();
	}
	
	@Override
	protected boolean initializeDownload() {
		productTypeId = loadProductTypeId();
		
		if(productTypeId == null) {
			addActionError(getText("error.noproducttypeattachedfile"));
			setFailActionResult(MISSING);
			return false;
		}
		
		return super.initializeDownload();
	}

	@Override
	public void setUniqueID(Long uniqueID) {
		productNetworkId = uniqueID;
	}

}
