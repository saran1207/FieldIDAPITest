package com.n4systems.model.producttype;

import javax.persistence.EntityManager;

import com.n4systems.model.AutoAttributeCriteria;
import com.n4systems.model.security.SecurityFilter;
import com.n4systems.persistence.loaders.SecurityFilteredLoader;
import com.n4systems.util.persistence.QueryBuilder;

public class AutoAttributeCriteriaByProductTypeIdLoader extends SecurityFilteredLoader<AutoAttributeCriteria> {
	private Long productTypeId;

	public AutoAttributeCriteriaByProductTypeIdLoader(SecurityFilter filter) {
		super(filter);
	}

	@Override
	protected AutoAttributeCriteria load(EntityManager em, SecurityFilter filter) {
		QueryBuilder<AutoAttributeCriteria> builder = new QueryBuilder<AutoAttributeCriteria>(AutoAttributeCriteria.class, filter);
		builder.addSimpleWhere("productType.id", productTypeId);
		builder.addFetch("inputs");
		
		AutoAttributeCriteria aaCriteria = builder.getSingleResult(em);
		return aaCriteria;
	}

	public void setProductTypeId(Long productTypeId) {
		this.productTypeId = productTypeId;
	}
}
