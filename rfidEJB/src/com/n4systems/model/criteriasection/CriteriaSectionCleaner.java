package com.n4systems.model.criteriasection;

import java.util.ArrayList;
import java.util.List;

import com.n4systems.model.Criteria;
import com.n4systems.model.CriteriaSection;
import com.n4systems.model.Tenant;
import com.n4systems.model.api.Cleaner;
import com.n4systems.model.api.EntityWithTenantCleaner;
import com.n4systems.model.criteria.CriteriaCleaner;

public class CriteriaSectionCleaner extends EntityWithTenantCleaner<CriteriaSection> {
	private final Cleaner<Criteria> criteriaCleaner;
	
	public CriteriaSectionCleaner(Tenant newTenant, Cleaner<Criteria> criteriaCleaner) {
		super(newTenant);
		this.criteriaCleaner = criteriaCleaner;
	}
	
	public CriteriaSectionCleaner(Tenant newTenant) {
		this(newTenant, new CriteriaCleaner(newTenant));
	}
	
	@Override
	public void clean(CriteriaSection section) {
		super.clean(section);
		
		cleanCriteria(section);
	}

	private void cleanCriteria(CriteriaSection section) {
		// we want to create a new list of criteria, rather then removing old ones to avoid ConcurrentModification while we iterate
		List<Criteria> cleanedCriteria = new ArrayList<Criteria>();
		for (Criteria criteria: section.getCriteria()) {
			// there's no need to copy the retired criteria
			if (!criteria.isRetired()) {
				criteriaCleaner.clean(criteria);
				
				cleanedCriteria.add(criteria);
			}
		}
		
		section.setCriteria(cleanedCriteria);
	}
}
