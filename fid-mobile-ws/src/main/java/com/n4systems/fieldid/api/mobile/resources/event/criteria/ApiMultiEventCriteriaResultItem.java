package com.n4systems.fieldid.api.mobile.resources.event.criteria;

// NOTE: To support edit, we could add the collecton of ids for recommendations, 
// deficiencies here. Right now we don't store multievents locally as of 2013.2.0
public class ApiMultiEventCriteriaResultItem {
	private String sid;
	
	public String getSid() {
		return sid;
	}

	public void setSid(String sid) {
		this.sid = sid;
	}	
}
