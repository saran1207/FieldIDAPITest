package com.n4systems.model.utils;

import java.util.TimeZone;


import com.n4systems.model.user.User;
import com.n4systems.util.DateTimeDefinition;

public class DateTimeDefiner implements DateTimeDefinition {

	private String dateFormat;
	private TimeZone timeZone;

	public DateTimeDefiner(String dateFormat, TimeZone timeZone) {
		super();
		this.dateFormat = dateFormat;
		this.timeZone = timeZone;
	}

	public DateTimeDefiner(User user) {
		this(user.getOwner().getPrimaryOrg().getDateFormat(), user.getTimeZone());
	}

	public String getDateFormat() {
		return dateFormat;
	}

	public String getDateTimeFormat() {
		return dateFormat + " h:mm a";
	}

	public TimeZone getTimeZone() {
		return timeZone;
	}

}
