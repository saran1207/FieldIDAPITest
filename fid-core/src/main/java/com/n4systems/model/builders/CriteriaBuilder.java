package com.n4systems.model.builders;

import com.n4systems.model.Criteria;

public abstract class CriteriaBuilder<K extends Criteria> extends EntityWithTenantBuilder<K> {

    protected final String text;
    protected boolean retired;
    protected boolean required;
    private String instructions;

    public CriteriaBuilder(String text, boolean retired, boolean required) {
        this.text = text;
        this.retired = retired;
        this.required = required;
    }

    @Override
    protected K assignAbstractFields(K criteria) {
        K crit = super.assignAbstractFields(criteria);
        crit.setDisplayText(text);
        crit.setRetired(retired);
        crit.setRequired(required);
        crit.setInstructions(instructions);
        return crit;
    }

    public CriteriaBuilder<K> withInstructions(String instructions) {
        this.instructions = instructions;
        return this;
    }
}
