package com.n4systems.model.criteria;

import java.util.ArrayList;

import com.n4systems.model.Criteria;
import com.n4systems.model.Tenant;
import com.n4systems.model.api.EntityWithTenantCleaner;
import com.n4systems.util.ListHelper;

public class CriteriaCleaner extends EntityWithTenantCleaner<Criteria> {

	public CriteriaCleaner(Tenant newTenant) {
		super(newTenant);
	}

	@Override
	public void clean(Criteria criteria) {
		super.clean(criteria);
		
		cleanRecommendations(criteria);
		cleanDeficiencies(criteria);
	}

	private void cleanRecommendations(Criteria criteria) {
		// Note: this is actually required so that hibernate does not move the old list to the new entity
		// when it's being copied. -mf
		criteria.setRecommendations(ListHelper.copy(criteria.getRecommendations(), new ArrayList<String>()));
	}
	
	private void cleanDeficiencies(Criteria criteria) {
		// Note: this is actually required so that hibernate does not move the old list to the new entity
		// when it's being copied. -mf
		criteria.setDeficiencies(ListHelper.copy(criteria.getDeficiencies(), new ArrayList<String>()));
	}
}
