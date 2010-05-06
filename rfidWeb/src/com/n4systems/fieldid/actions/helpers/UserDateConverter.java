package com.n4systems.fieldid.actions.helpers;

import java.util.Date;
import java.util.TimeZone;

import rfid.web.helper.SessionUser;

import com.n4systems.util.DateHelper;

public class UserDateConverter {

	private final SessionUser sessionUser;
	
	public UserDateConverter(SessionUser sessionUser) {
		super();
		this.sessionUser = sessionUser;
	}

	public String convertDate(Date date) {
		return DateHelper.date2String(getDateFormat(), date);
	}
	
	public Date convertDate(String date) {
		return DateHelper.string2Date(getDateFormat(), date);
	}
	
	public Date convertToEndOfDay(String date) {
		Date day = convertDate(date);
		return (day != null) ? DateHelper.getEndOfDay(day) : null;
	}
	
	public String convertDateTime(Date date) {
		return DateHelper.date2String(getDateTimeFormat(), date, getTimeZone());
	}
	
	public Date convertDateTime(String date) {
		return DateHelper.string2DateTime(getDateTimeFormat(), date, getTimeZone());
	}
	
	
	public boolean isValidDate(String date, boolean usingTime) {
		// blank dates are ok
		if(date == null || date.trim().length() == 0) {
			return true;
		}
		String format = ( usingTime ) ? getDateTimeFormat() : getDateFormat();
		return  DateHelper.isDateValid(format, date);
	}

	private String getDateFormat() {
		return sessionUser.getDateFormat();
	}
	
	private String getDateTimeFormat() {
		return sessionUser.getDateTimeFormat();
	}

	private TimeZone getTimeZone() {
		return sessionUser.getTimeZone();
	}
}
