package com.n4systems.model.criteria;

import java.util.ArrayList;

import com.n4systems.model.ComboBoxCriteria;
import com.n4systems.model.Criteria;
import com.n4systems.model.SelectCriteria;
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

        if (criteria instanceof SelectCriteria) {
            copySelectOptions((SelectCriteria) criteria);
        }
        if (criteria instanceof ComboBoxCriteria) {
            copyComboOptions((ComboBoxCriteria) criteria);
        }
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
}
