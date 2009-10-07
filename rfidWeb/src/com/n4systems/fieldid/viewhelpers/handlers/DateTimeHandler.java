package com.n4systems.fieldid.viewhelpers.handlers;

import java.util.Date;
import java.util.TimeZone;

import com.n4systems.fieldid.actions.api.AbstractAction;
import com.n4systems.util.DateTimeDefinition;
import com.n4systems.util.FieldidDateFormatter;

public class DateTimeHandler implements OutputHandler, DateTimeDefinition {
	private TimeZone timeZone;
	private String dateTimeFormat;
	
	public DateTimeHandler() {
		this.timeZone = TimeZone.getDefault();
		this.dateTimeFormat = "yyyy-MM-dd HH:mm:ss a";
	}

	public String handle(AbstractAction action, Long entityId, Object cell) {
		String cellString = "";
		if (cell instanceof Date) {
			cellString = new FieldidDateFormatter((Date)cell, this, true, true).format();
		} 
		
		return cellString;
	}

	public boolean isLabel() {
		return false;
	}

	public TimeZone getTimeZone() {
		return timeZone;
	}

	public void setTimeZone(TimeZone timeZone) {
		this.timeZone = timeZone;
	}

	public String getDateTimeFormat() {
		return dateTimeFormat;
	}

	public void setDateTimeFormat(String dateTimeFormat) {
		this.dateTimeFormat = dateTimeFormat;
	}

	public String getDateFormat() {
		return null;
	}

	public String getDisplayDateFormat() {
		
		return null;
	}

	public String getDisplayDateTimeFormat() {
		return null;
	}

	public String getTimeZoneName() {
		
		return null;
	}

}
