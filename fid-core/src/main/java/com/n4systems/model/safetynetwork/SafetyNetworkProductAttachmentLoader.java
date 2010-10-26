package com.n4systems.model.safetynetwork;

import com.n4systems.model.product.AssetAttachment;
import com.n4systems.model.security.OpenSecurityFilter;
import com.n4systems.model.security.SecurityFilter;
import com.n4systems.persistence.loaders.SecurityFilteredLoader;
import com.n4systems.util.persistence.QueryBuilder;
import com.n4systems.util.persistence.WhereClauseFactory;

import javax.persistence.EntityManager;

public class SafetyNetworkProductAttachmentLoader extends SecurityFilteredLoader<AssetAttachment> {
	private Long attachmentId;
	private Long productNetworkId;

    public SafetyNetworkProductAttachmentLoader(SecurityFilter filter) {
        super(filter);
    }

    @Override
    protected AssetAttachment load(EntityManager em, SecurityFilter filter) {
        // This will throw an exception if we're not allowed to see this asset
        new SafetyNetworkProductLoader(filter).setProductId(productNetworkId).load();

        QueryBuilder<AssetAttachment> builder = new QueryBuilder<AssetAttachment>(AssetAttachment.class, new OpenSecurityFilter());
        builder.addWhere(WhereClauseFactory.create("id", attachmentId));
        builder.addWhere(WhereClauseFactory.create("asset.networkId", productNetworkId));

        AssetAttachment attachment = builder.getSingleResult(em);
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
