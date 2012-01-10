package com.n4systems.fieldid.ws.v1.resources.eventtype;

import java.util.ArrayList;
import java.util.List;

import com.n4systems.fieldid.ws.v1.resources.eventtype.criteria.ApiCriteria;
import com.n4systems.fieldid.ws.v1.resources.model.ApiReadonlyModel;

public class ApiCriteriaSection extends ApiReadonlyModel {
	private String title;
	private List<ApiCriteria> criteria = new ArrayList<ApiCriteria>();

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

}
