package com.n4systems.model.assetstatus;

import java.util.List;

import javax.persistence.EntityManager;


import com.n4systems.model.AssetStatus;
import com.n4systems.model.security.SecurityFilter;
import com.n4systems.persistence.loaders.ListLoader;
import com.n4systems.util.persistence.QueryBuilder;

public class AssetStatusListLoader extends ListLoader<AssetStatus> {

	private String[] postFetchFields = new String[0];

	public AssetStatusListLoader(SecurityFilter filter) {
		super(filter);
	}

	@Override
	protected List<AssetStatus> load(EntityManager em, SecurityFilter filter) {
		QueryBuilder<AssetStatus> builder = new QueryBuilder<AssetStatus>(AssetStatus.class, filter);
		builder.addOrder("name");
		builder.addPostFetchPaths(postFetchFields);
		
		List<AssetStatus> assetStatuses = builder.getResultList(em);

		return assetStatuses;
	}

	public AssetStatusListLoader setPostFetchFields(String...fields) {
		this.postFetchFields = fields;
		return this;
	}

}
