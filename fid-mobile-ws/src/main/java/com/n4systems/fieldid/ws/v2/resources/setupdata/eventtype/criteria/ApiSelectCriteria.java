package com.n4systems.fieldid.ws.v2.resources.setupdata.eventtype.criteria;

import java.util.ArrayList;
import java.util.List;

public class ApiSelectCriteria extends ApiCriteria {
	private List<String> options;

	public ApiSelectCriteria(List<String> options) {
		super("SELECTBOX");
		this.options = options;
	}

	public ApiSelectCriteria() {
		this(new ArrayList<>());
	}

	public List<String> getOptions() {
		return options;
	}

	public void setOptions(List<String> options) {
		this.options = options;
	}
}
