package com.n4systems.model.builders;

import com.n4systems.model.DateFieldCriteria;

public class DateFieldCriteriaBuilder extends CriteriaBuilder<DateFieldCriteria> {
	

	public DateFieldCriteriaBuilder() {
        this("aDateField", false);
	}
	
	public DateFieldCriteriaBuilder(String text, boolean retired) {
		super(text, retired);
	}

	public DateFieldCriteriaBuilder(String text) {
		super(text, false);
	}

	@Override
	public DateFieldCriteria createObject() {
		DateFieldCriteria criteria = assignAbstractFields(new DateFieldCriteria());
		return criteria;
	}

	public static DateFieldCriteriaBuilder aDateFieldCriteria() {
		return new DateFieldCriteriaBuilder(null, false);
	}
	
	public DateFieldCriteriaBuilder withDisplayText(String text) {
		return makeBuilder(new DateFieldCriteriaBuilder(text, retired));
	}

}
