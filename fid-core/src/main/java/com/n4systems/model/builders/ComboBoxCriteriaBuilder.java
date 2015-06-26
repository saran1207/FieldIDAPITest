package com.n4systems.model.builders;

import com.n4systems.model.ComboBoxCriteria;

public class ComboBoxCriteriaBuilder extends CriteriaBuilder<ComboBoxCriteria> {

    public ComboBoxCriteriaBuilder(String text, boolean retired, boolean required) {
        super(text, retired, required);
    }

    public static ComboBoxCriteriaBuilder aComboBoxCriteria() {
        return new ComboBoxCriteriaBuilder("aComboBox", false, false);
    }

    public ComboBoxCriteriaBuilder withDisplayText(String text) {
        return makeBuilder(new ComboBoxCriteriaBuilder(text, retired, required));
    }

    @Override
    public ComboBoxCriteria createObject() {
        return super.assignAbstractFields(new ComboBoxCriteria());
    }

}
