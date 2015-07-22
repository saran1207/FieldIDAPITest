package com.n4systems.fieldid.ws.v2.resources.setupdata.person;

import com.n4systems.fieldid.ws.v2.resources.model.ApiReadOnlyModelWithOwner;

public class ApiPerson extends ApiReadOnlyModelWithOwner {
	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
