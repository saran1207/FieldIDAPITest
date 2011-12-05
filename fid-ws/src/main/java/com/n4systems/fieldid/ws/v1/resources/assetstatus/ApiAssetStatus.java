package com.n4systems.fieldid.ws.v1.resources.assetstatus;

import com.n4systems.fieldid.ws.v1.resources.model.ApiReadonlyModel;

public class ApiAssetStatus extends ApiReadonlyModel {
	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
