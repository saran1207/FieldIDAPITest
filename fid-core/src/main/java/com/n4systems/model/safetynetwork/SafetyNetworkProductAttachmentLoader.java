package com.n4systems.model.safetynetwork;

import com.n4systems.model.product.ProductAttachment;
import com.n4systems.model.security.OpenSecurityFilter;
import com.n4systems.model.security.SecurityFilter;
import com.n4systems.persistence.loaders.SecurityFilteredLoader;
import com.n4systems.util.persistence.QueryBuilder;
import com.n4systems.util.persistence.WhereClauseFactory;

import javax.persistence.EntityManager;

public class SafetyNetworkProductAttachmentLoader extends SecurityFilteredLoader<ProductAttachment> {
	private Long attachmentId;
	private Long productNetworkId;

    public SafetyNetworkProductAttachmentLoader(SecurityFilter filter) {
        super(filter);
    }

    @Override
    protected ProductAttachment load(EntityManager em, SecurityFilter filter) {
        // This will throw an exception if we're not allowed to see this product
        new SafetyNetworkProductLoader(filter).setProductId(productNetworkId).load();

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
