package com.n4systems.fieldid.ws.v1.resources.event;

import java.util.ArrayList;
import java.util.List;

public class ApiCriteriaSectionResult {
	private Long sectionId;
	private List<ApiCriteriaResult> criteria = new ArrayList<ApiCriteriaResult>();

	public Long getSectionId() {
		return sectionId;
	}

	public void setSectionId(Long sectionId) {
		this.sectionId = sectionId;
	}

	public List<ApiCriteriaResult> getCriteria() {
		return criteria;
	}

	public void setCriteria(List<ApiCriteriaResult> criteria) {
		this.criteria = criteria;
	}

}
