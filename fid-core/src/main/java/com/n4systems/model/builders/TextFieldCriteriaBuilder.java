package com.n4systems.model.builders;

import com.n4systems.model.TextFieldCriteria;

public class TextFieldCriteriaBuilder extends CriteriaBuilder<TextFieldCriteria> {

    public TextFieldCriteriaBuilder(String text, boolean retired, boolean required) {
        super(text, retired, required);
    }

    public TextFieldCriteriaBuilder(String text) {
    	this(text, false, false);
	}

	public static TextFieldCriteriaBuilder aTextFieldCriteria() {
        return new TextFieldCriteriaBuilder(null, false, false);
    }

    public TextFieldCriteriaBuilder withDisplayText(String text) {
        return makeBuilder(new TextFieldCriteriaBuilder(text, retired, required));
    }

    @Override
    public TextFieldCriteria createObject() {
        return super.assignAbstractFields(new TextFieldCriteria());
    }

}
