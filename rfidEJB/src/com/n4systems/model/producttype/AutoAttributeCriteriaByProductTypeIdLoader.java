package com.n4systems.model.producttype;

import com.n4systems.ejb.PersistenceManager;
import com.n4systems.model.AutoAttributeCriteria;
import com.n4systems.persistence.loaders.legacy.SecuredLoader;
import com.n4systems.util.SecurityFilter;
import com.n4systems.util.persistence.QueryBuilder;

// TODO: Update this class to extend FilteredLoader
public class AutoAttributeCriteriaByProductTypeIdLoader extends SecuredLoader<AutoAttributeCriteria> {
	private Long productTypeId;

	public AutoAttributeCriteriaByProductTypeIdLoader(PersistenceManager pm, SecurityFilter filter) {
		super(pm, filter);
	}

	public AutoAttributeCriteriaByProductTypeIdLoader(SecurityFilter filter) {
		super(filter);
	}

	@Override
	protected AutoAttributeCriteria load(PersistenceManager pm, SecurityFilter filter) {
		QueryBuilder<AutoAttributeCriteria> builder = new QueryBuilder<AutoAttributeCriteria>(AutoAttributeCriteria.class, filter.prepareFor(AutoAttributeCriteria.class));
		builder.addSimpleWhere("productType.id", productTypeId);
		builder.addFetch("inputs");
		
		AutoAttributeCriteria aaCriteria = pm.find(builder);
		return aaCriteria;
	}

	public void setProductTypeId(Long productTypeId) {
		this.productTypeId = productTypeId;
	}
}
