package com.n4systems.fieldid.ws.v1.resources.eventtype.criteria;

public class ApiNumberFieldCriteria extends ApiCriteria {
	private Integer decimalPlaces = 0;

	public ApiNumberFieldCriteria(Integer decimalPlaces) {
		super("NUMBERFIELD");
		this.decimalPlaces = decimalPlaces;
	}

	public ApiNumberFieldCriteria() {
		this(0);
	}

	public void setDecimalPlaces(Integer decimalPlaces) {
		this.decimalPlaces = decimalPlaces;
	}

	public Integer getDecimalPlaces() {
		return decimalPlaces;
	}
}
