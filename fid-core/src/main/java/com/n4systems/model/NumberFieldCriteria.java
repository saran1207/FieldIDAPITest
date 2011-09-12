package com.n4systems.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

@Entity
@Table(name="numberfield_criteria")
@PrimaryKeyJoinColumn(name="id")
public class NumberFieldCriteria extends Criteria {
	
	@Column(nullable=false)
	private Integer decimalPlaces = 0;

	@Override
	public CriteriaType getCriteriaType() {
		return CriteriaType.NUMBER_FIELD;
	}
	
	public Integer getDecimalPlaces() {
		return decimalPlaces;
	}

	public void setDecimalPlaces(Integer decimalPlaces) {
		this.decimalPlaces = decimalPlaces;
	}

}
