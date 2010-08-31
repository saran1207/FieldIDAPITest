package com.n4systems.webservice.dto;

public class RequestResponse {

	private ResponseStatus status;
	private String message;

	public RequestResponse() {
		status = ResponseStatus.OK;
	}
	
	public ResponseStatus getStatus() {
		return status;
	}

	public void setStatus(ResponseStatus status) {
		this.status = status;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}
