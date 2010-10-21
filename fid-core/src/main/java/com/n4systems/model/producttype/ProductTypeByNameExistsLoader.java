package com.n4systems.model.producttype;

import javax.persistence.EntityManager;

import com.n4systems.model.AssetType;
import com.n4systems.model.security.SecurityFilter;
import com.n4systems.persistence.loaders.SecurityFilteredLoader;
import com.n4systems.util.persistence.QueryBuilder;
import com.n4systems.util.persistence.WhereClauseFactory;

public class ProductTypeByNameExistsLoader extends SecurityFilteredLoader<Boolean> {
	private String name;
	
	public ProductTypeByNameExistsLoader(SecurityFilter filter) {
		super(filter);
	}

	@Override
	protected Boolean load(EntityManager em, SecurityFilter filter) {
		QueryBuilder<AssetType> builder = new QueryBuilder<AssetType>(AssetType.class, filter);
		builder.addWhere(WhereClauseFactory.create("name", name));
		
		Boolean exists = builder.entityExists(em);
		return exists;
	}


	public ProductTypeByNameExistsLoader setName(String name) {
		this.name = name;
		return this;
	}
}
