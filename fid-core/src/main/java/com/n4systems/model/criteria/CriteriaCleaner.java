package com.n4systems.model.criteria;

import com.n4systems.model.ComboBoxCriteria;
import com.n4systems.model.Criteria;
import com.n4systems.model.SelectCriteria;
import com.n4systems.model.Tenant;
import com.n4systems.model.api.Cleaner;
import com.n4systems.model.api.EntityWithTenantCleaner;
import com.n4systems.model.criteriarules.CriteriaRule;
import com.n4systems.model.criteriarules.CriteriaRuleCleaner;
import com.n4systems.util.ListHelper;

import java.util.ArrayList;
import java.util.List;

public class CriteriaCleaner extends EntityWithTenantCleaner<Criteria> {
	private final Cleaner<CriteriaRule> criteriaRuleCleaner;

	public CriteriaCleaner(Tenant newTenant, Cleaner<CriteriaRule> criteriaRuleCleaner) {
		super(newTenant);
		this.criteriaRuleCleaner = criteriaRuleCleaner;
	}

	public CriteriaCleaner(Tenant newTenant) {
		this(newTenant, new CriteriaRuleCleaner(newTenant));
	}

	@Override
	public void clean(Criteria criteria) {
		super.clean(criteria);
		
		cleanRecommendations(criteria);
		cleanDeficiencies(criteria);

        if (criteria instanceof SelectCriteria) {
            copySelectOptions((SelectCriteria) criteria);
        }
        if (criteria instanceof ComboBoxCriteria) {
            copyComboOptions((ComboBoxCriteria) criteria);
        }

		cleanCriteriaRules(criteria);
	}

    private void copyComboOptions(ComboBoxCriteria criteria) {
        // Required so the options aren't moved into the new criteria
        criteria.setOptions(ListHelper.copy(criteria.getOptions(), new ArrayList<String>()));
    }

    private void copySelectOptions(SelectCriteria criteria) {
        // Required so the options aren't moved into the new criteria
        criteria.setOptions(ListHelper.copy(criteria.getOptions(), new ArrayList<String>()));
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

	private void cleanCriteriaRules(Criteria criteria) {
		List<CriteriaRule> cleanedRules = new ArrayList<>();

		for(CriteriaRule criteriaRule : criteria.getRules()) {
			criteriaRuleCleaner.clean(criteriaRule);
			cleanedRules.add(criteriaRule);
		}

		criteria.setRules(cleanedRules);
	}
}
