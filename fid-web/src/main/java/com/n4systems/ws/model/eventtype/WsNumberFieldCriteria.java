package com.n4systems.ws.model.eventtype;

public class WsNumberFieldCriteria extends WsCriteria {
	
	private Integer decimalPlaces = 0;
	
	public WsNumberFieldCriteria() {
		setCriteriaType("NUMBERFIELD");
	}

	public void setDecimalPlaces(Integer decimalPlaces) {
		this.decimalPlaces = decimalPlaces;
	}

	public Integer getDecimalPlaces() {
		return decimalPlaces;
	}
}
