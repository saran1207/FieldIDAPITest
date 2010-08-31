package com.n4systems.subscription.netsuite.model;

import com.n4systems.subscription.Response;

public class BaseResponse implements Response {

	private String result;
	private String function;
	private String details;

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public String getFunction() {
		return function;
	}

	public void setFunction(String function) {
		this.function = function;
	}

	public String getDetails() {
		return details;
	}

	public void setDetails(String details) {
		this.details = details;
	}
	
	public boolean requestRespondedWithSuccess() {
		return result.equals("OK");
	}
}
