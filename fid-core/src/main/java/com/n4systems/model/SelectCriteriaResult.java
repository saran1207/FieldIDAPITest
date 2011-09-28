package com.n4systems.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

@Entity
@Table(name="select_criteriaresults")
@PrimaryKeyJoinColumn(name="id")
public class SelectCriteriaResult extends CriteriaResult {

	@Column
	private String value;

	public void setValue(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}

	@Override
	public String getResultString() {
		return value!=null ? getValue() : "";
	}
	
}
