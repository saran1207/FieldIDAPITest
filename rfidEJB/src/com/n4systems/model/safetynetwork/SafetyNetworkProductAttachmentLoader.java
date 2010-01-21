package com.n4systems.model.safetynetwork;

import javax.persistence.EntityManager;

import com.n4systems.model.product.ProductAttachment;
import com.n4systems.model.security.OpenSecurityFilter;
import com.n4systems.persistence.loaders.Loader;
import com.n4systems.util.persistence.QueryBuilder;
import com.n4systems.util.persistence.WhereClauseFactory;

public class SafetyNetworkProductAttachmentLoader extends Loader<ProductAttachment> {
	private Long attachmentId;
	private Long productNetworkId;

	@Override
	protected ProductAttachment load(EntityManager em) {
		QueryBuilder<ProductAttachment> builder = new QueryBuilder<ProductAttachment>(ProductAttachment.class, new OpenSecurityFilter());
		builder.addWhere(WhereClauseFactory.create("id", attachmentId));
		builder.addWhere(WhereClauseFactory.create("product.networkId", productNetworkId));
		
		ProductAttachment attachment = builder.getSingleResult(em);
		return attachment;
	}
	
	public SafetyNetworkProductAttachmentLoader setAttachmentId(Long attachmentId) {
		this.attachmentId = attachmentId;
		return this;
	}

	public SafetyNetworkProductAttachmentLoader setProductNetworkId(Long productNetworkId) {
		this.productNetworkId = productNetworkId;
		return this;
	}
}
