package com.n4systems.fieldid.ws.v1.resources.eventtype.criteria;

public class ApiDateFieldCriteria extends ApiCriteria {
	private boolean includeTime = false;

	public ApiDateFieldCriteria(boolean includeTime) {
		this();
		this.includeTime = includeTime;
	}
	
	public ApiDateFieldCriteria() {
		setCriteriaType("DATEFIELD");
	}

	public void setIncludeTime(boolean includeTime) {
		this.includeTime = includeTime;
	}

	public boolean isIncludeTime() {
		return includeTime;
	}
}
