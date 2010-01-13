package com.n4systems.fieldid.selenium.datatypes;

public class PrimaryOrganization extends Organization {
	String webSiteAddress;

	public PrimaryOrganization(String name) {
		super(name);
	}
	
	public void setWebSiteAddress(String s) {
		this.webSiteAddress = s;
	}
	
	public String getWebSiteAddress() {
		return webSiteAddress;
	}
}
