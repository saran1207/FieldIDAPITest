package com.n4systems.fieldid.ws.v1.resources.savedEvent;

import com.n4systems.fieldid.ws.v1.resources.event.ApiCriteriaResult;
import com.n4systems.fieldid.ws.v1.resources.eventtype.criteria.ApiCriteria;

public class ApiCriteriaWithResult extends ApiCriteria {
	private ApiCriteriaResult result;

	protected ApiCriteriaWithResult(String criteriaType) {
		super(criteriaType);
	}
	
	public ApiCriteriaResult getResult() {
		return result;
	}

	public void setResult(ApiCriteriaResult result) {
		this.result = result;
	}
}
