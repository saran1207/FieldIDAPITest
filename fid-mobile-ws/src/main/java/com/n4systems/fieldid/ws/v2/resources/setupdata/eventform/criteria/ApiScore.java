package com.n4systems.fieldid.ws.v2.resources.setupdata.eventform.criteria;

import com.n4systems.fieldid.ws.v2.resources.model.ApiReadOnlyModel2;

public class ApiScore extends ApiReadOnlyModel2 {
	private String name;
	private Double value;
	private boolean notApplicable = false;

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setValue(Double value) {
		this.value = value;
	}

	public Double getValue() {
		return value;
	}

	public void setNotApplicable(boolean notApplicable) {
		this.notApplicable = notApplicable;
	}

	public boolean isNotApplicable() {
		return notApplicable;
	}
}
