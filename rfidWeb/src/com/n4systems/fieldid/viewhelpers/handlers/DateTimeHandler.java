package com.n4systems.fieldid.viewhelpers.handlers;

import java.util.Date;
import java.util.TimeZone;

import com.n4systems.fieldid.actions.api.AbstractAction;
import com.n4systems.util.DateTimeDefinition;
import com.n4systems.util.FieldidDateFormatter;

public class DateTimeHandler extends WebOutputHandler implements DateTimeDefinition {
	
	public DateTimeHandler(AbstractAction action) {
		super(action);
	}

	public TimeZone getTimeZone() {
		return action.getSessionUser().getTimeZone();
	}

	public String getDateTimeFormat() {
		return action.getSessionUser().getDateTimeFormat();
	}

	public String getDateFormat() {
		return action.getSessionUser().getDateFormat();
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
