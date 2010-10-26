package com.n4systems.model.safetynetwork;

import java.util.List;

import javax.persistence.EntityManager;

import com.n4systems.model.product.AssetAttachment;
import com.n4systems.model.security.OpenSecurityFilter;
import com.n4systems.persistence.loaders.Loader;
import com.n4systems.util.persistence.QueryBuilder;

public class SafetyNetworkProductAttachmentListLoader extends Loader<List<AssetAttachment>> {
	private Long productId;
	private Long networkId;
	
	@Override
	protected List<AssetAttachment> load(EntityManager em) {
		QueryBuilder<AssetAttachment> builder = new QueryBuilder<AssetAttachment>(AssetAttachment.class, new OpenSecurityFilter());
		builder.addSimpleWhere("asset.id", productId);
		builder.addSimpleWhere("asset.networkId", networkId);
		
		List<AssetAttachment> attachments = builder.getResultList(em);
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
