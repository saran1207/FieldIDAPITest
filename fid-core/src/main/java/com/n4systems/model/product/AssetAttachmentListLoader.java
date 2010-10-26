package com.n4systems.model.product;

import java.util.List;

import javax.persistence.EntityManager;

import com.n4systems.exceptions.InvalidArgumentException;
import com.n4systems.model.Asset;
import com.n4systems.model.security.SecurityFilter;
import com.n4systems.persistence.loaders.ListLoader;
import com.n4systems.util.persistence.QueryBuilder;

public class AssetAttachmentListLoader extends ListLoader<AssetAttachment> {
	private Asset asset;

	public AssetAttachmentListLoader(SecurityFilter filter) {
		super(filter);
	}
	
	@Override
	protected List<AssetAttachment> load(EntityManager em, SecurityFilter filter) {
		if (asset == null) {
			throw new InvalidArgumentException("you must have an asset to load asset attachments");
		}
		QueryBuilder<AssetAttachment> builder = new QueryBuilder<AssetAttachment>(AssetAttachment.class, filter);
		builder.addSimpleWhere("asset", asset).addOrder("id");
		
		return builder.getResultList(em);
		
	}

	public AssetAttachmentListLoader setProduct(Asset asset) {
		this.asset = asset;
		return this;
	}
}
