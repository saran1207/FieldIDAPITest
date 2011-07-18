package com.n4systems.fieldid.viewhelpers.handlers;

import com.n4systems.fieldid.utils.WebContextProvider;
import com.n4systems.util.DateTimeDefinition;
import com.n4systems.util.FieldidDateFormatter;

import java.util.Date;
import java.util.TimeZone;

public class DateTimeHandler extends WebOutputHandler implements DateTimeDefinition {
	
	public DateTimeHandler(WebContextProvider action) {
		super(action);
	}

	public TimeZone getTimeZone() {
		return contextProvider.getSessionUser().getTimeZone();
	}

	public String getDateTimeFormat() {
		return contextProvider.getSessionUser().getDateTimeFormat();
	}

	public String getDateFormat() {
		return contextProvider.getSessionUser().getDateFormat();
	}

	public String handleWeb(Long entityId, Object cell) {
		String cellString = "";
		if (cell instanceof Date) {
			cellString = new FieldidDateFormatter((Date)cell, this, true, true).format();
		} 
		
		return cellString;
	}
	
	public Object handleExcel(Long entityId, Object value) {
		return value;
	}

}
