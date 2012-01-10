package com.n4systems.fieldid.ws.v1.resources.eventtype.criteria;

import com.n4systems.fieldid.ws.v1.resources.model.ApiReadonlyModel;

public class ApiScore extends ApiReadonlyModel {
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
