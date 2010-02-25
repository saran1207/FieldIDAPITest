package com.n4systems.model.autoattribute;

import javax.persistence.EntityManager;

import com.n4systems.model.AutoAttributeCriteria;
import com.n4systems.model.ProductType;
import com.n4systems.model.security.SecurityFilter;
import com.n4systems.persistence.loaders.SecurityFilteredLoader;
import com.n4systems.util.persistence.QueryBuilder;
import com.n4systems.util.persistence.WhereClauseFactory;

public class AutoAttributeByProductTypeLoader extends SecurityFilteredLoader<AutoAttributeCriteria> {
	private ProductType type;
	
	public AutoAttributeByProductTypeLoader(SecurityFilter filter) {
		super(filter);
	}

	@Override
	protected AutoAttributeCriteria load(EntityManager em, SecurityFilter filter) {
		QueryBuilder<AutoAttributeCriteria> builder = new QueryBuilder<AutoAttributeCriteria>(AutoAttributeCriteria.class, filter);
		builder.addWhere(WhereClauseFactory.create("productType", type));
		builder.addPostFetchPaths("inputs", "outputs");
		
		
		AutoAttributeCriteria criteria = builder.getSingleResult(em);
		return criteria;
	}

	public AutoAttributeByProductTypeLoader setType(ProductType type) {
		this.type = type;
		return this;
	}

}
