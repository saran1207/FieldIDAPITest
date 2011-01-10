package com.n4systems.model.builders;

import com.n4systems.model.TextFieldCriteria;

public class TextFieldCriteriaBuilder extends CriteriaBuilder<TextFieldCriteria> {

    public TextFieldCriteriaBuilder(String text, boolean retired) {
        super(text, retired);
    }

    @Override
    public TextFieldCriteria createObject() {
        return super.assignAbstractFields(new TextFieldCriteria());
    }

}
