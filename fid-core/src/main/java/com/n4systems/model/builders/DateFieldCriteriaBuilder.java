package com.n4systems.model.builders;

import com.n4systems.model.DateFieldCriteria;

public class DateFieldCriteriaBuilder extends CriteriaBuilder<DateFieldCriteria> {
	

	public DateFieldCriteriaBuilder() {
        this("aDateField", false, false);
	}
	
	public DateFieldCriteriaBuilder(String text, boolean retired, boolean required) {
		super(text, retired, required);
	}

	public DateFieldCriteriaBuilder(String text) {
		super(text, false, false);
	}

	@Override
	public DateFieldCriteria createObject() {
		DateFieldCriteria criteria = assignAbstractFields(new DateFieldCriteria());
		return criteria;
	}

	public static DateFieldCriteriaBuilder aDateFieldCriteria() {
		return new DateFieldCriteriaBuilder(null, false, false);
	}
	
	public DateFieldCriteriaBuilder withDisplayText(String text) {
		return makeBuilder(new DateFieldCriteriaBuilder(text, retired, required));
	}

}
