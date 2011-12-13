package com.n4systems.fieldid.ws.v1.resources.eventtype.attributevalues;

import java.util.Date;

public class ApiDateTimeAttributeValue extends ApiAttributeValue {
	private Date date;
	
	public Date getDate() {
		return date;
	}

	public void setDate(Date value) {
		this.date = value;
	}
	
}
