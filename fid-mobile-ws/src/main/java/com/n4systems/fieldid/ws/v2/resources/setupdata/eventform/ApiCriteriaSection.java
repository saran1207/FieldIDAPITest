package com.n4systems.fieldid.ws.v2.resources.setupdata.eventform;

import com.n4systems.fieldid.ws.v2.resources.model.ApiReadOnlyModel2;
import com.n4systems.fieldid.ws.v2.resources.setupdata.eventform.criteria.ApiCriteria;

import java.util.ArrayList;
import java.util.List;

public class ApiCriteriaSection extends ApiReadOnlyModel2 {
	private String title;
    private Boolean optional;
	private List<ApiCriteria> criteria = new ArrayList<>();

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public List<ApiCriteria> getCriteria() {
		return criteria;
	}

	public void setCriteria(List<ApiCriteria> criteria) {
		this.criteria = criteria;
	}

    public Boolean isOptional() {
        return optional;
    }

    public void setOptional(Boolean optional) {
        this.optional = optional;
    }
}
