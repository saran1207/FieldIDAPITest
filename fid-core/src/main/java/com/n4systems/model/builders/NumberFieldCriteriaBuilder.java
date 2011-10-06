package com.n4systems.model.builders;

import com.n4systems.model.NumberFieldCriteria;

public class NumberFieldCriteriaBuilder extends CriteriaBuilder<NumberFieldCriteria> {

	public NumberFieldCriteriaBuilder() {
		this("aNumberField", false);
	}
	
	public NumberFieldCriteriaBuilder(String text, boolean retired) {
		super(text, retired);
	}
	
	public NumberFieldCriteriaBuilder(String text) {
		super(text, false);
	}


	@Override
	public NumberFieldCriteria createObject() {
		NumberFieldCriteria criteria = assignAbstractFields(new NumberFieldCriteria());
		return criteria;
	}
	
	public static NumberFieldCriteriaBuilder aNumberFieldCriteria() {
		return new NumberFieldCriteriaBuilder(null, false);
	}

	public NumberFieldCriteriaBuilder withDisplayText(String text) {
		return makeBuilder(new NumberFieldCriteriaBuilder(text, retired));
	}
	
}
