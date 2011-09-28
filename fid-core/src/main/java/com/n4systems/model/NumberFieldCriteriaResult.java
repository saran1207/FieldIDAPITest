package com.n4systems.model;

import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

@Entity
@Table(name = "numberfield_criteriaresults")
@PrimaryKeyJoinColumn(name="id")
public class NumberFieldCriteriaResult extends CriteriaResult {

	private Float value;

	public Float getValue() {
		return value;
	}

	public void setValue(Float value) {
		this.value = value;
	}
	
	public String getFormattedValue() {
		return String.format("%." + ((NumberFieldCriteria) getCriteria()).getDecimalPlaces() + "f", value);
	}

	@Override
	public String getResultString() {
		return getFormattedValue();
	}
	
}
