package com.n4systems.ws.utils;

import java.util.Date;

public class DateParam extends Date {
	
	public DateParam(String timestamp) {
		setTime(Long.parseLong(timestamp));
	}
	
}
