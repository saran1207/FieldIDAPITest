package com.n4systems.fieldid.api.mobile.resources.model;

public class ApiReadonlyModelWithName extends ApiReadonlyModel{
	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
