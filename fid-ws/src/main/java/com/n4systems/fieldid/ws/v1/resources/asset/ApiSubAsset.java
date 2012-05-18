package com.n4systems.fieldid.ws.v1.resources.asset;

public class ApiSubAsset {
	private String sid;
	private String type;
	private String identifier;	
	
	public String getSid() {
		return sid;
	}
	
	public void setSid(String sid) {
		this.sid = sid;
	}
	
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
	public String getIdentifier() {
		return identifier;
	}

	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}		
}
