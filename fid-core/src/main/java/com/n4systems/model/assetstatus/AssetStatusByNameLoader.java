package com.n4systems.model.assetstatus;

import javax.persistence.EntityManager;

import rfid.ejb.entity.AssetStatus;

import com.n4systems.model.security.SecurityFilter;
import com.n4systems.persistence.loaders.SecurityFilteredLoader;
import com.n4systems.util.persistence.QueryBuilder;
import com.n4systems.util.persistence.WhereClauseFactory;

public class AssetStatusByNameLoader extends SecurityFilteredLoader<AssetStatus> {
	private String name;
	
	public AssetStatusByNameLoader(SecurityFilter filter) {
		super(filter);
	}

	@Override
	protected AssetStatus load(EntityManager em, SecurityFilter filter) {
		QueryBuilder<AssetStatus> builder = new QueryBuilder<AssetStatus>(AssetStatus.class, filter);
		builder.addWhere(WhereClauseFactory.create("name", name));
		
		AssetStatus status = builder.getSingleResult(em);
		return status;
	}

	public AssetStatusByNameLoader setName(String name) {
		this.name = name;
		return this;
	}
}
