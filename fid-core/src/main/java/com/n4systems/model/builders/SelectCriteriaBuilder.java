package com.n4systems.model.builders;

import com.google.common.collect.Lists;
import com.n4systems.model.SelectCriteria;

public class SelectCriteriaBuilder extends CriteriaBuilder<SelectCriteria> {

    private String[] options;

	public SelectCriteriaBuilder(String text, boolean retired) {
        super(text, retired);
    }

    public SelectCriteriaBuilder(String text) {
        super(text, false);
    }

    public static SelectCriteriaBuilder aSelectCriteria() {
        return new SelectCriteriaBuilder(null, false);
    }

    public SelectCriteriaBuilder withDisplayText(String text) {
        return makeBuilder(new SelectCriteriaBuilder(text, retired));
    }
    
    public SelectCriteriaBuilder withOptions(String... options) { 
    	this.options = options;
    	return this;
    }

    @Override
    public SelectCriteria createObject() {
        SelectCriteria selectCriteria = super.assignAbstractFields(new SelectCriteria());
        selectCriteria.setOptions(options != null ? Lists.newArrayList(options) : null);
        return selectCriteria;
    }

}
