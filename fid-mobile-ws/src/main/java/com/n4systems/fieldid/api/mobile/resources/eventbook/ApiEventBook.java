package com.n4systems.fieldid.api.mobile.resources.eventbook;

import com.n4systems.fieldid.api.mobile.resources.model.ApiReadWriteModelWithOwner;

public class ApiEventBook extends ApiReadWriteModelWithOwner {
	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
}
