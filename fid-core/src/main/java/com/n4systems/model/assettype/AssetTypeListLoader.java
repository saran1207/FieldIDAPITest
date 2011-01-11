package com.n4systems.model.assettype;

import java.util.List;

import javax.persistence.EntityManager;

import com.n4systems.model.AssetType;
import com.n4systems.model.security.SecurityFilter;
import com.n4systems.persistence.loaders.ListLoader;
import com.n4systems.util.persistence.QueryBuilder;

public class AssetTypeListLoader extends ListLoader<AssetType> {
	
	private String[] postFetchFields = new String[0];

	public AssetTypeListLoader(SecurityFilter filter) {
		super(filter);
	}

	@Override
	protected List<AssetType> load(EntityManager em, SecurityFilter filter) {
		QueryBuilder<AssetType> builder = new QueryBuilder<AssetType>(AssetType.class, filter);
		builder.addOrder("name");
		builder.addPostFetchPaths(postFetchFields);
		return builder.getResultList(em);
	}
	
	public AssetTypeListLoader setPostFetchFields(String...fields) {
		this.postFetchFields = fields;
		return this;
	}

}
