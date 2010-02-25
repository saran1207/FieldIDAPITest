package com.n4systems.model.producttype;

import javax.persistence.EntityManager;

import com.n4systems.model.ProductType;
import com.n4systems.model.security.SecurityFilter;
import com.n4systems.persistence.loaders.SecurityFilteredLoader;
import com.n4systems.util.persistence.QueryBuilder;
import com.n4systems.util.persistence.WhereClauseFactory;

public class ProductTypeByNameLoader extends SecurityFilteredLoader<ProductType> {
	private String name;
	
	public ProductTypeByNameLoader(SecurityFilter filter) {
		super(filter);
	}

	@Override
	protected ProductType load(EntityManager em, SecurityFilter filter) {
		QueryBuilder<ProductType> builder = new QueryBuilder<ProductType>(ProductType.class, filter);
		builder.addWhere(WhereClauseFactory.create("name", name));
		
		ProductType type = builder.getSingleResult(em);
		return type;
	}

	public ProductTypeByNameLoader setName(String name) {
		this.name = name;
		return this;
	}
}
