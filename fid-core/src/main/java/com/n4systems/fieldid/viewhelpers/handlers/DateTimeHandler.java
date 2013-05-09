package com.n4systems.fieldid.viewhelpers.handlers;

import com.n4systems.fieldid.service.download.TableGenerationContext;
import com.n4systems.fieldid.service.download.WebOutputHandler;
import com.n4systems.util.FieldIdDateFormatter;
import com.n4systems.util.time.DateUtil;

import java.util.Date;

public class DateTimeHandler extends WebOutputHandler {
	
	public DateTimeHandler(TableGenerationContext action) {
		super(action);
	}

	public String handleWeb(Long entityId, Object cell) {
		String cellString = "";
		if (cell instanceof Date) {
			cellString = new FieldIdDateFormatter((Date)cell, contextProvider, true, !DateUtil.isMidnight((Date) cell)).format();
		} 
		
		return cellString;
	}
	
	public Object handleExcel(Long entityId, Object value) {
		return value;
	}

}
