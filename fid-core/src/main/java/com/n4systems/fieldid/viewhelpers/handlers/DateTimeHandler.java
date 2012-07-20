package com.n4systems.fieldid.viewhelpers.handlers;

import com.n4systems.fieldid.service.download.TableGenerationContext;
import com.n4systems.fieldid.service.download.WebOutputHandler;
import com.n4systems.util.DateTimeDefinition;
import com.n4systems.util.FieldIdDateFormatter;

import java.util.Date;
import java.util.TimeZone;

public class DateTimeHandler extends WebOutputHandler implements DateTimeDefinition {
	
	public DateTimeHandler(TableGenerationContext action) {
		super(action);
	}

	public TimeZone getTimeZone() {
		return contextProvider.getTimeZone();
	}

	public String getDateTimeFormat() {
		return contextProvider.getDateTimeFormat();
	}

	public String getDateFormat() {
		return contextProvider.getDateFormat();
	}

	public String handleWeb(Long entityId, Object cell) {
		String cellString = "";
		if (cell instanceof Date) {
			cellString = new FieldIdDateFormatter((Date)cell, this, true, true).format();
		} 
		
		return cellString;
	}
	
	public Object handleExcel(Long entityId, Object value) {
		return value;
	}

}
