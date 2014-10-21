package com.n4systems.fieldid.ws.v1.resources.event;

import java.util.ArrayList;
import java.util.List;

public class ApiCriteriaSectionResult {
	private Long sectionId;
    private Boolean hidden;
	private List<ApiCriteriaResult> criteria = new ArrayList<>();

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

    public Boolean isHidden() {
        return hidden;
    }

    public void setHidden(Boolean hidden) {
        this.hidden = hidden;
    }
}
