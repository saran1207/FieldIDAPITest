package com.n4systems.model.utils;

import java.util.Calendar;
import java.util.Date;

public class PlainDate extends Date {

	private static final long serialVersionUID = 1L;

	public PlainDate() {
		this(new Date());
	}

	public PlainDate(Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.set(Calendar.HOUR_OF_DAY, 0);  
		c.set(Calendar.MINUTE, 0);  
		c.set(Calendar.SECOND, 0);  
		c.set(Calendar.MILLISECOND, 0);
		setTime(c.getTimeInMillis());
	}

}
