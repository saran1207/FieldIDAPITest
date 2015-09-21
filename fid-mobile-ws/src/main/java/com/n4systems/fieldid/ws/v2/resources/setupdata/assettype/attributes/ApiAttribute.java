package com.n4systems.fieldid.ws.v2.resources.setupdata.assettype.attributes;

import com.n4systems.fieldid.ws.v2.resources.model.ApiReadOnlyModel;

public abstract class ApiAttribute extends ApiReadOnlyModel {
	private String name;
	private String type;
	private Long weight;
	private boolean required;
	
	protected ApiAttribute(String type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Long getWeight() {
		return weight;
	}

	public void setWeight(Long weight) {
		this.weight = weight;
	}

	public boolean isRequired() {
		return required;
	}

	public void setRequired(boolean required) {
		this.required = required;
	}

}
