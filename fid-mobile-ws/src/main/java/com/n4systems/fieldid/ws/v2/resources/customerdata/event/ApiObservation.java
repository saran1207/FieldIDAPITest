package com.n4systems.fieldid.ws.v2.resources.customerdata.event;

public class ApiObservation {
	private String sid;
	private String text;
	private String state;

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getSid() {
		return sid;
	}
	
	public void setSid(String sid) {
		this.sid = sid;
	}
}
