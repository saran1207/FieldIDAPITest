package com.n4systems.model.product;

import java.util.List;

import javax.persistence.EntityManager;

import com.n4systems.exceptions.InvalidArgumentException;
import com.n4systems.model.Asset;
import com.n4systems.model.security.SecurityFilter;
import com.n4systems.persistence.loaders.ListLoader;
import com.n4systems.util.persistence.QueryBuilder;

public class ProductAttachmentListLoader extends ListLoader<ProductAttachment> {
	private Asset asset;

	public ProductAttachmentListLoader(SecurityFilter filter) {
		super(filter);
	}
	
	@Override
	protected List<ProductAttachment> load(EntityManager em, SecurityFilter filter) {
		if (asset == null) {
			throw new InvalidArgumentException("you must have an asset to load asset attachments");
		}
		QueryBuilder<ProductAttachment> builder = new QueryBuilder<ProductAttachment>(ProductAttachment.class, filter);
		builder.addSimpleWhere("asset", asset).addOrder("id");
		
		return builder.getResultList(em);
		
	}

	public ProductAttachmentListLoader setProduct(Asset asset) {
		this.asset = asset;
		return this;
	}
}
