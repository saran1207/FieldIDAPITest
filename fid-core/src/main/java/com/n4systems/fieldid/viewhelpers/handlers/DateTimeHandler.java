package com.n4systems.fieldid.viewhelpers.handlers;

import com.n4systems.fieldid.service.download.TableGenerationContext;
import com.n4systems.fieldid.service.download.WebOutputHandler;
import com.n4systems.util.DateTimeDefinition;
import com.n4systems.util.FieldIdDateFormatter;

import java.util.Date;
import java.util.TimeZone;

public class DateTimeHandler extends WebOutputHandler {
	
	public DateTimeHandler(TableGenerationContext action) {
		super(action);
	}

	public String handleWeb(Long entityId, Object cell) {
		String cellString = "";
		if (cell instanceof Date) {
			cellString = new FieldIdDateFormatter((Date)cell, contextProvider, true, true).format();
		} 
		
		return cellString;
	}
	
	public Object handleExcel(Long entityId, Object value) {
		return value;
	}

}
