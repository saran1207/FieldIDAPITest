package com.n4systems.fieldid.ws.v2.resources.setupdata.person;

import com.n4systems.fieldid.ws.v2.resources.model.ApiReadonlyModelWithOwner;

public class ApiPerson extends ApiReadonlyModelWithOwner {
	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
