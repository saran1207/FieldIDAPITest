package com.n4systems.subscription.netsuite.client;

import com.n4systems.subscription.netsuite.model.NetSuiteValidatePromoCodeResponse;

public class ValidatePromoCodeClient extends AbstractNetsuiteClient<NetSuiteValidatePromoCodeResponse>{

	private String code;
	
	public ValidatePromoCodeClient() {
		super(NetSuiteValidatePromoCodeResponse.class, "validatepromocode");
	}

	@Override
	protected void addRequestParameters() {
		addRequestParameter("code", code);
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
}
