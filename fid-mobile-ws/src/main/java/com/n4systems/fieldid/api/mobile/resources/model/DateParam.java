package com.n4systems.fieldid.api.mobile.resources.model;

import java.util.Date;

public class DateParam extends Date {
	
	public DateParam(String timestamp) {
		setTime(Long.parseLong(timestamp));
	}
	
}