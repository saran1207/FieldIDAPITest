package com.n4systems.netsuite.model;

public abstract class AbstractResponse {

	private String result;
	private String function;

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
}
