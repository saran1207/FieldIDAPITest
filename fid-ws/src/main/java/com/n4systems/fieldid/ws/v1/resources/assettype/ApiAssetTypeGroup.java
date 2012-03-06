package com.n4systems.fieldid.ws.v1.resources.assettype;

import com.n4systems.fieldid.ws.v1.resources.model.ApiReadonlyModel;

public class ApiAssetTypeGroup extends ApiReadonlyModel {
	private String name;
	private Long weight;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long getWeight() {
		return weight;
	}

	public void setWeight(Long weight) {
		this.weight = weight;
	}

}
