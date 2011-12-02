package com.n4systems.fieldid.ws.v1.resources.model;

import java.util.Date;

public class DateParam extends Date {
	
	public DateParam(String timestamp) {
		setTime(Long.parseLong(timestamp));
	}
	
}