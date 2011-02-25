package com.n4systems.model.builders;

import com.n4systems.model.SelectCriteria;

public class SelectCriteriaBuilder extends CriteriaBuilder<SelectCriteria> {

    public SelectCriteriaBuilder(String text, boolean retired) {
        super(text, retired);
    }

    public static SelectCriteriaBuilder aSelectCriteria() {
        return new SelectCriteriaBuilder(null, false);
    }

    public SelectCriteriaBuilder withDisplayText(String text) {
        return makeBuilder(new SelectCriteriaBuilder(text, retired));
    }

    @Override
    public SelectCriteria createObject() {
        return super.assignAbstractFields(new SelectCriteria());
    }

}
