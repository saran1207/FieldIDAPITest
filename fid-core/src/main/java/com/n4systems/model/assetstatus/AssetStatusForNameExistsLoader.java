package com.n4systems.model.assetstatus;

import javax.persistence.EntityManager;


import com.n4systems.model.AssetStatus;
import com.n4systems.model.security.SecurityFilter;
import com.n4systems.persistence.loaders.SecurityFilteredLoader;
import com.n4systems.util.persistence.QueryBuilder;
import com.n4systems.util.persistence.WhereClauseFactory;
import com.n4systems.util.persistence.WhereParameter.Comparator;

public class AssetStatusForNameExistsLoader extends SecurityFilteredLoader<Boolean> {
	
	protected String name;
	protected Long id;
	
	public AssetStatusForNameExistsLoader(SecurityFilter filter) {
		super(filter);
	}

	@Override
	protected Boolean load(EntityManager em, SecurityFilter filter) {
		QueryBuilder<AssetStatus> builder = new QueryBuilder<AssetStatus>(AssetStatus.class, filter);
		builder.addWhere(WhereClauseFactory.create("name", name));
		if(id != null) {
			builder.addWhere(WhereClauseFactory.create(Comparator.NE, "id", id));
		}
		Boolean exists = builder.entityExists(em);
		return exists;
	}

	public AssetStatusForNameExistsLoader setName(String name) {
		this.name = name;
		return this;
	}
	
	public AssetStatusForNameExistsLoader setId(Long id) {
		this.id = id;
		return this;
	}
}
