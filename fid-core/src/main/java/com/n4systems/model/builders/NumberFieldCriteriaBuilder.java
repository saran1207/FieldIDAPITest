package com.n4systems.model.builders;

import com.n4systems.model.NumberFieldCriteria;

public class NumberFieldCriteriaBuilder extends CriteriaBuilder<NumberFieldCriteria> {

	public NumberFieldCriteriaBuilder() {
		this("aNumberField", false, false);
	}
	
	public NumberFieldCriteriaBuilder(String text, boolean retired, boolean required) {
		super(text, retired, required);
	}
	
	public NumberFieldCriteriaBuilder(String text) {
		super(text, false, false);
	}


	@Override
	public NumberFieldCriteria createObject() {
		NumberFieldCriteria criteria = assignAbstractFields(new NumberFieldCriteria());
		return criteria;
	}
	
	public static NumberFieldCriteriaBuilder aNumberFieldCriteria() {
		return new NumberFieldCriteriaBuilder(null, false, false);
	}

	public NumberFieldCriteriaBuilder withDisplayText(String text) {
		return makeBuilder(new NumberFieldCriteriaBuilder(text, retired, required));
	}
	
}
