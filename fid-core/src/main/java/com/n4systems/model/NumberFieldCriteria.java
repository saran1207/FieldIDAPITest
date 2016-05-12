package com.n4systems.model;

import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

@Entity
@Table(name="numberfield_criteria")
@PrimaryKeyJoinColumn(name="id")
@Cacheable
@org.hibernate.annotations.Cache(region = "SetupDataCache", usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
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
