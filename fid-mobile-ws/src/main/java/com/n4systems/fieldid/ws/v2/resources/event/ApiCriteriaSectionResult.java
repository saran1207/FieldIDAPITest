package com.n4systems.fieldid.ws.v2.resources.event;

import java.util.ArrayList;
import java.util.List;

public class ApiCriteriaSectionResult {
	private Long sectionId;
    private Boolean hidden;
	private List<ApiCriteriaResult> criteria = new ArrayList<>();

    public ApiCriteriaSectionResult() {
        //This forces a default value into the field so that - if it is not set by Jackson - it is set by SOMETHING and
        //prevents NullPointerExceptions down the road.
        hidden = Boolean.FALSE;
    }

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
