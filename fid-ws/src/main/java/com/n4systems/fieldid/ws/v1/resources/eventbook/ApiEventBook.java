package com.n4systems.fieldid.ws.v1.resources.eventbook;

import com.n4systems.fieldid.ws.v1.resources.model.ApiReadWriteModelWithOwner;

public class ApiEventBook extends ApiReadWriteModelWithOwner {
	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
}
