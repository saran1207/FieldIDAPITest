package com.n4systems.model.assetstatus;

import com.n4systems.model.AssetStatus;
import com.n4systems.model.security.SecurityFilter;
import com.n4systems.persistence.loaders.SecurityFilteredLoader;
import com.n4systems.util.persistence.QueryBuilder;

import javax.persistence.EntityManager;

public class AssetStatusFilteredLoader extends SecurityFilteredLoader<AssetStatus> {
	private Long id;

	public AssetStatusFilteredLoader(SecurityFilter filter) {
		super(filter);
	}

	@Override
	protected AssetStatus load(EntityManager em, SecurityFilter filter) {
		QueryBuilder<AssetStatus> builder = new QueryBuilder<AssetStatus>(AssetStatus.class, filter);
		builder.addSimpleWhere("id", id);
		
		AssetStatus assetStatus = builder.getSingleResult(em);
	    return assetStatus;
	}

	public AssetStatusFilteredLoader setId(Long id) {
		this.id = id;
		return this;
	}
}
