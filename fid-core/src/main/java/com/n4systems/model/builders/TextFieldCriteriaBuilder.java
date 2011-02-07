package com.n4systems.model.builders;

import com.n4systems.model.TextFieldCriteria;

public class TextFieldCriteriaBuilder extends CriteriaBuilder<TextFieldCriteria> {

    public TextFieldCriteriaBuilder(String text, boolean retired) {
        super(text, retired);
    }

    public static TextFieldCriteriaBuilder aTextFieldCriteria() {
        return new TextFieldCriteriaBuilder(null, false);
    }

    public TextFieldCriteriaBuilder withText(String text) {
        return makeBuilder(new TextFieldCriteriaBuilder(text, retired));
    }

    @Override
    public TextFieldCriteria createObject() {
        return super.assignAbstractFields(new TextFieldCriteria());
    }

}
