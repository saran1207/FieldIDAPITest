package com.n4systems.model.safetynetwork;

import java.util.List;

import javax.persistence.EntityManager;

import com.n4systems.model.product.ProductAttachment;
import com.n4systems.model.security.OpenSecurityFilter;
import com.n4systems.persistence.loaders.Loader;
import com.n4systems.util.persistence.QueryBuilder;

public class SafetyNetworkProductAttachmentListLoader extends Loader<List<ProductAttachment>> {
	private Long productId;
	private Long networkId;
	
	@Override
	protected List<ProductAttachment> load(EntityManager em) {
		QueryBuilder<ProductAttachment> builder = new QueryBuilder<ProductAttachment>(ProductAttachment.class, new OpenSecurityFilter());
		builder.addSimpleWhere("product.id", productId);
		builder.addSimpleWhere("product.networkId", networkId);
		
		List<ProductAttachment> attachments = builder.getResultList(em);
		return attachments;
	}

	public SafetyNetworkProductAttachmentListLoader setProductId(Long productId) {
		this.productId = productId;
		return this;
	}

	public SafetyNetworkProductAttachmentListLoader setNetworkId(Long networkId) {
		this.networkId = networkId;
		return this;
	}

}
