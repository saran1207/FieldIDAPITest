package com.n4systems.webservice.server.bundles;

import java.io.Serializable;

public class WebServiceStatusBundle implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private WebServiceStatus status;
	private String message;
	
	public WebServiceStatusBundle(WebServiceStatus status, String message) {
		this.status = status;
		this.message = message;
	}
	
	public WebServiceStatusBundle(WebServiceStatus status) {
		this(status, null);
	}
	
	public WebServiceStatusBundle() {}

	public WebServiceStatus getStatus() {
		return status;
	}

	public void setStatus(WebServiceStatus status) {
		this.status = status;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}
