package com.n4systems.fieldid.ws.v1.resources.eventtype;

import com.n4systems.fieldid.ws.v1.resources.eventtype.criteria.ApiCriteria;
import com.n4systems.fieldid.ws.v1.resources.model.ApiReadonlyModel;

import java.util.ArrayList;
import java.util.List;

public class ApiCriteriaSection extends ApiReadonlyModel {
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
