package com.n4systems.fieldid.ws.v2.resources.setupdata.eventbook;

import com.n4systems.fieldid.ws.v2.resources.model.ApiReadWriteModelWithOwner;

public class ApiEventBook extends ApiReadWriteModelWithOwner {
	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
}
