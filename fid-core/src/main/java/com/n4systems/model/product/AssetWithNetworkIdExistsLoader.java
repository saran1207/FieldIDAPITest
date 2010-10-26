package com.n4systems.model.product;

import javax.persistence.EntityManager;

import com.n4systems.model.Asset;
import com.n4systems.model.security.SecurityFilter;
import com.n4systems.persistence.loaders.SecurityFilteredLoader;
import com.n4systems.util.persistence.QueryBuilder;
import com.n4systems.util.persistence.WhereClauseFactory;

public class AssetWithNetworkIdExistsLoader extends SecurityFilteredLoader<Boolean> {

	private Long networkId;
	
	public AssetWithNetworkIdExistsLoader(SecurityFilter filter) {
		super(filter);
	}

	@Override
	public Boolean load(EntityManager em, SecurityFilter filter) {
		QueryBuilder<Boolean> builder = new QueryBuilder<Boolean>(Asset.class, filter);
		builder.addWhere(WhereClauseFactory.create("networkId", networkId));
		
		Boolean exists = builder.entityExists(em);
		return exists;
	}

	public AssetWithNetworkIdExistsLoader setNetworkId(Long networkId) {
		this.networkId = networkId;
		return this;
	}
}
