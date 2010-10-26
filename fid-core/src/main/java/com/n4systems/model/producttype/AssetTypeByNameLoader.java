package com.n4systems.model.producttype;

import javax.persistence.EntityManager;

import com.n4systems.model.AssetType;
import com.n4systems.model.security.SecurityFilter;
import com.n4systems.persistence.loaders.SecurityFilteredLoader;
import com.n4systems.util.persistence.QueryBuilder;
import com.n4systems.util.persistence.WhereClauseFactory;

public class AssetTypeByNameLoader extends SecurityFilteredLoader<AssetType> {
	private String name;
	
	public AssetTypeByNameLoader(SecurityFilter filter) {
		super(filter);
	}

	@Override
	protected AssetType load(EntityManager em, SecurityFilter filter) {
		QueryBuilder<AssetType> builder = new QueryBuilder<AssetType>(AssetType.class, filter);
		builder.addWhere(WhereClauseFactory.create("name", name));
		
		AssetType type = builder.getSingleResult(em);
		return type;
	}

	public AssetTypeByNameLoader setName(String name) {
		this.name = name;
		return this;
	}
}
