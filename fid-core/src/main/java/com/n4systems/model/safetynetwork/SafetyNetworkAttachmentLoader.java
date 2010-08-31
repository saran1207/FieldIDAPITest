package com.n4systems.model.safetynetwork;

import javax.persistence.EntityManager;

import com.n4systems.model.FileAttachment;
import com.n4systems.persistence.loaders.Loader;

public class SafetyNetworkAttachmentLoader extends Loader<FileAttachment> {
	private final ProductTypeFileAttachmentIdByProductNetworkIdExistsLoader canLoadAttachmentLoader;
	private Long id;
	private Long productNetworkId;
	
	public SafetyNetworkAttachmentLoader() {
		this(new ProductTypeFileAttachmentIdByProductNetworkIdExistsLoader());
	}
	
	public SafetyNetworkAttachmentLoader(ProductTypeFileAttachmentIdByProductNetworkIdExistsLoader canLoadAttachmentLoader) {
		this.canLoadAttachmentLoader = canLoadAttachmentLoader;
	}

	@Override
	protected FileAttachment load(EntityManager em) {
		canLoadAttachmentLoader.setAttachmentId(id);
		canLoadAttachmentLoader.setNetworkId(productNetworkId);
		
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
	
	public SafetyNetworkAttachmentLoader setProductNetworkId(Long productNetworkId) {
		this.productNetworkId = productNetworkId;
		return this;
	}
}
