package com.n4systems.fieldid.ws.v1.resources.event.criteria;

import java.util.List;

public class ApiMultiEventCriteriaImage {
	private ApiCriteriaImage criteriaImageTemplate;
	private List<String> criteriaResultIds;
	
	public ApiCriteriaImage getCriteriaImageTemplate() {
		return criteriaImageTemplate;
	}
	
	public void setCriteriaImageTemplate(ApiCriteriaImage criteriaImageTemplate) {
		this.criteriaImageTemplate = criteriaImageTemplate;
	}
	
	public List<String> getCriteriaResultIds() {
		return criteriaResultIds;
	}

	public void setCriteriaResultIds(List<String> criteriaResultIds) {
		this.criteriaResultIds = criteriaResultIds;
	}
}
