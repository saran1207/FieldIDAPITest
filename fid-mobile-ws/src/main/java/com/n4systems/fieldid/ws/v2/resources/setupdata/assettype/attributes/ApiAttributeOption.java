package com.n4systems.fieldid.ws.v2.resources.setupdata.assettype.attributes;

import com.n4systems.fieldid.ws.v2.resources.model.ApiReadonlyModel;

public class ApiAttributeOption extends ApiReadonlyModel {
	private Long weight;
	private String value;

	public Long getWeight() {
		return weight;
	}

	public void setWeight(Long weight) {
		this.weight = weight;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

}
