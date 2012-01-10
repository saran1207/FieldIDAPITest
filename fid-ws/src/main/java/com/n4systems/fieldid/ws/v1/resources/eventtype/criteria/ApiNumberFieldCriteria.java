package com.n4systems.fieldid.ws.v1.resources.eventtype.criteria;

public class ApiNumberFieldCriteria extends ApiCriteria {
	private Integer decimalPlaces = 0;

	public ApiNumberFieldCriteria(Integer decimalPlaces) {
		this();
		this.decimalPlaces = decimalPlaces;
	}

	public ApiNumberFieldCriteria() {
		setCriteriaType("NUMBERFIELD");
	}

	public void setDecimalPlaces(Integer decimalPlaces) {
		this.decimalPlaces = decimalPlaces;
	}

	public Integer getDecimalPlaces() {
		return decimalPlaces;
	}
}
