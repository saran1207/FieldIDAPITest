package com.n4systems.model;

import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

@Entity
@Table(name = "numberfield_criteriaresults")
@PrimaryKeyJoinColumn(name="id")
public class NumberFieldCriteriaResult extends CriteriaResult {

	private Double value;

	public Double getValue() {
		return value;
	}

	public void setValue(Double value) {
		this.value = value;
	}
	
	public String getFormattedValue() {
		return value==null ? "" : String.format("%." + ((NumberFieldCriteria) getCriteria()).getDecimalPlaces() + "f", value);
	}

	@Override
	public String getResultString() {
		return getFormattedValue();
	}
	
}
