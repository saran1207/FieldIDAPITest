package com.n4systems.model.builders;

import com.n4systems.model.Criteria;

public abstract class CriteriaBuilder<K extends Criteria> extends EntityWithTenantBuilder<K> {

    protected final String text;
    protected boolean retired;
    private String instructions;

    public CriteriaBuilder(String text, boolean retired) {
        this.text = text;
        this.retired = retired;
    }

    @Override
    protected K assignAbstractFields(K criteria) {
        K crit = super.assignAbstractFields(criteria);
        crit.setDisplayText(text);
        crit.setRetired(retired);
        crit.setInstructions(instructions);
        return crit;
    }

    public CriteriaBuilder<K> withInstructions(String instructions) {
        this.instructions = instructions;
        return this;
    }
}
