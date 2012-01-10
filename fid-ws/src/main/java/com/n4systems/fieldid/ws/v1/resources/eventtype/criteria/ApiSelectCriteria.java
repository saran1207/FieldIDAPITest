package com.n4systems.fieldid.ws.v1.resources.eventtype.criteria;

import java.util.ArrayList;
import java.util.List;

public class ApiSelectCriteria extends ApiCriteria {
	private List<String> options = new ArrayList<String>();

	public ApiSelectCriteria(List<String> options) {
		this();
		this.options = options;
	}

	public ApiSelectCriteria() {
		setCriteriaType("SELECTBOX");
	}

	public List<String> getOptions() {
		return options;
	}

	public void setOptions(List<String> options) {
		this.options = options;
	}
}
