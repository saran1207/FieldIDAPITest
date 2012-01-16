package com.n4systems.fieldid.ws.v1.resources.eventtype.criteria;

import java.util.ArrayList;
import java.util.List;

public class ApiSelectCriteria extends ApiCriteria {
	private List<String> options;

	public ApiSelectCriteria(List<String> options) {
		super("SELECTBOX");
		this.options = options;
	}

	public ApiSelectCriteria() {
		this(new ArrayList<String>());
	}

	public List<String> getOptions() {
		return options;
	}

	public void setOptions(List<String> options) {
		this.options = options;
	}
}
