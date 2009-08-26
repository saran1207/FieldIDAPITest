package com.n4systems.subscription.netsuite.model;

public class NetSuiteValidatePromoCodeResponse extends AbstractResponse implements com.n4systems.subscription.ValidatePromoCodeResponse {

	private String details;

	public String getDetails() {
		return details;
	}

	public void setDetails(String details) {
		this.details = details;
	}
}
