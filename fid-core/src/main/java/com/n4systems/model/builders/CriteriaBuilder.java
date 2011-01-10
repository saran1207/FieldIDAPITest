package com.n4systems.model.builders;

import com.n4systems.model.Criteria;

public abstract class CriteriaBuilder<K extends Criteria> extends EntityWithTenantBuilder<K> {

    protected final String text;
    protected final boolean retired;

    public CriteriaBuilder(String text, boolean retired) {
        this.text = text;
        this.retired = retired;
    }

    @Override
    protected K assignAbstractFields(K criteria) {
        K crit = super.assignAbstractFields(criteria);
        crit.setDisplayText(text);
        crit.setRetired(retired);
        return crit;
    }

}
