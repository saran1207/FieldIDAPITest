package com.n4systems.fieldid.ws.v1.resources.assettype.attributevalues;

public class ApiAttributeValue {
	private Long attributeId;
	private Object value;
	
	public Long getAttributeId() {
		return attributeId;
	}

	public void setAttributeId(Long attributeId) {
		this.attributeId = attributeId;
	}

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}
	
}
