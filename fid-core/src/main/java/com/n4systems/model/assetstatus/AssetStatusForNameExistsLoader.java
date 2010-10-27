package com.n4systems.model.assetstatus;

import javax.persistence.EntityManager;

import rfid.ejb.entity.AssetStatus;

import com.n4systems.model.security.SecurityFilter;
import com.n4systems.persistence.loaders.SecurityFilteredLoader;
import com.n4systems.util.persistence.QueryBuilder;
import com.n4systems.util.persistence.WhereClauseFactory;

public class AssetStatusForNameExistsLoader extends SecurityFilteredLoader<Boolean> {
	protected String name;
	
	public AssetStatusForNameExistsLoader(SecurityFilter filter) {
		super(filter);
	}

	@Override
	protected Boolean load(EntityManager em, SecurityFilter filter) {
		QueryBuilder<AssetStatus> builder = new QueryBuilder<AssetStatus>(AssetStatus.class, filter);
		builder.addWhere(WhereClauseFactory.create("name", name));
		
		Boolean exists = builder.entityExists(em);
		return exists;
	}

	public AssetStatusForNameExistsLoader setName(String name) {
		this.name = name;
		return this;
	}
}
