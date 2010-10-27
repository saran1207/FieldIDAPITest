package com.n4systems.model.safetynetwork;

import com.n4systems.model.asset.AssetAttachment;
import com.n4systems.model.security.OpenSecurityFilter;
import com.n4systems.model.security.SecurityFilter;
import com.n4systems.persistence.loaders.SecurityFilteredLoader;
import com.n4systems.util.persistence.QueryBuilder;
import com.n4systems.util.persistence.WhereClauseFactory;

import javax.persistence.EntityManager;

public class SafetyNetworkAssetAttachmentLoader extends SecurityFilteredLoader<AssetAttachment> {
	private Long attachmentId;
	private Long assetNetworkId;

    public SafetyNetworkAssetAttachmentLoader(SecurityFilter filter) {
        super(filter);
    }

    @Override
    protected AssetAttachment load(EntityManager em, SecurityFilter filter) {
        // This will throw an exception if we're not allowed to see this asset
        new SafetyNetworkAssetLoader(filter).setAssetId(assetNetworkId).load();

        QueryBuilder<AssetAttachment> builder = new QueryBuilder<AssetAttachment>(AssetAttachment.class, new OpenSecurityFilter());
        builder.addWhere(WhereClauseFactory.create("id", attachmentId));
        builder.addWhere(WhereClauseFactory.create("asset.networkId", assetNetworkId));

        AssetAttachment attachment = builder.getSingleResult(em);
        return attachment;
    }

	public SafetyNetworkAssetAttachmentLoader setAttachmentId(Long attachmentId) {
		this.attachmentId = attachmentId;
		return this;
	}

	public SafetyNetworkAssetAttachmentLoader setAssetNetworkId(Long assetNetworkId) {
		this.assetNetworkId = assetNetworkId;
		return this;
	}
}
