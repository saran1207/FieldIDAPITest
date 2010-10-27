package com.n4systems.model.safetynetwork;

import javax.persistence.EntityManager;

import com.n4systems.model.FileAttachment;
import com.n4systems.persistence.loaders.Loader;

public class SafetyNetworkAttachmentLoader extends Loader<FileAttachment> {
	private final AssetTypeFileAttachmentIdByAssetNetworkIdExistsLoader canLoadAttachmentLoader;
	private Long id;
	private Long assetNetworkId;
	
	public SafetyNetworkAttachmentLoader() {
		this(new AssetTypeFileAttachmentIdByAssetNetworkIdExistsLoader());
	}
	
	public SafetyNetworkAttachmentLoader(AssetTypeFileAttachmentIdByAssetNetworkIdExistsLoader canLoadAttachmentLoader) {
		this.canLoadAttachmentLoader = canLoadAttachmentLoader;
	}

	@Override
	protected FileAttachment load(EntityManager em) {
		canLoadAttachmentLoader.setAttachmentId(id);
		canLoadAttachmentLoader.setNetworkId(assetNetworkId);
		
		if (!canLoadAttachmentLoader.load(em)) {
			return null;
		}
		
		FileAttachment attachment = em.find(FileAttachment.class, id);
		return attachment;
	}

	public SafetyNetworkAttachmentLoader setId(Long id) {
		this.id = id;
		return this;
	}
	
	public SafetyNetworkAttachmentLoader setAssetNetworkId(Long assetNetworkId) {
		this.assetNetworkId = assetNetworkId;
		return this;
	}
}
