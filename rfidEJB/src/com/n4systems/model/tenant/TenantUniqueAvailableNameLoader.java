package com.n4systems.model.tenant;

import javax.persistence.EntityManager;

import com.n4systems.model.Tenant;
import com.n4systems.persistence.loaders.NonFilteredUniqueValueAvailableLoader;
import com.n4systems.util.persistence.QueryBuilder;
import com.n4systems.util.persistence.WhereParameter.Comparator;

public class TenantUniqueAvailableNameLoader extends NonFilteredUniqueValueAvailableLoader {

	
	@Override
	protected Boolean load(EntityManager em) {
		QueryBuilder<Long> countQuery = new QueryBuilder<Long>(Tenant.class);
		countQuery.setCountSelect().addSimpleWhere("name", getUniqueName()); 
		if (isIdGiven()) {
			countQuery.addWhere(Comparator.NE, "id", "id", getId());
		}
		return countQuery.getSingleResult(em) == 0;
	}

}
