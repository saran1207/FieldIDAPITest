package com.n4systems.model.safetynetwork;

import com.n4systems.model.asset.AssetAttachment;
import com.n4systems.model.security.OpenSecurityFilter;
import com.n4systems.persistence.loaders.Loader;
import com.n4systems.util.persistence.QueryBuilder;

import javax.persistence.EntityManager;
import java.util.List;

public class SafetyNetworkAssetAttachmentListLoader extends Loader<List<AssetAttachment>> {
	private Long assetId;
	private Long networkId;
	
	@Override
	public List<AssetAttachment> load(EntityManager em) {
		QueryBuilder<AssetAttachment> builder = new QueryBuilder<AssetAttachment>(AssetAttachment.class, new OpenSecurityFilter());
		builder.addSimpleWhere("asset.id", assetId);
		builder.addSimpleWhere("asset.networkId", networkId);
		
		List<AssetAttachment> attachments = builder.getResultList(em);
		return attachments;
	}

	public SafetyNetworkAssetAttachmentListLoader setAssetId(Long assetId) {
		this.assetId = assetId;
		return this;
	}

	public SafetyNetworkAssetAttachmentListLoader setNetworkId(Long networkId) {
		this.networkId = networkId;
		return this;
	}

}
