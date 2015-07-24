package com.n4systems.fieldid.ws.v2.resources.customerdata.event.multi;

import com.n4systems.fieldid.ws.v2.resources.customerdata.event.ApiCriteriaImage;

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
