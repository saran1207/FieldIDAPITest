package com.n4systems.model.criteriarules;

import com.n4systems.model.Tenant;
import com.n4systems.model.api.EntityWithTenantCleaner;

public class CriteriaRuleCleaner extends EntityWithTenantCleaner<CriteriaRule> {

    public CriteriaRuleCleaner(Tenant newTenant) {
        super(newTenant);
    }

    @Override
    public void clean(CriteriaRule rule) {
        super.clean(rule);
    }
}
