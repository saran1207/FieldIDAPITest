package com.n4systems.fieldid.ws.v1.resources.eventtype.attributevalues;

public class ApiComboBoxAttributeValue extends ApiAttributeValue {
	private Long optionId;
	private String text;
	
	public Long getOptionId() {
		return optionId;
	}

	public void setOptionId(Long optionId) {
		this.optionId = optionId;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

}
