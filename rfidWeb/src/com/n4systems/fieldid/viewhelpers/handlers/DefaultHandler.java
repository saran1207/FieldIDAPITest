package com.n4systems.fieldid.viewhelpers.handlers;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * The default CustomizableSearchAction OutputHandler.  Provides handling of Dates and other special types.
 */
public class DefaultHandler implements OutputHandler {
	private String dateFormat; 
	
	public DefaultHandler(String dateFormat) {
		this.dateFormat = dateFormat;
	}
	
	public String handle(Long entityId, Object cell) {
		String cellString;
		if (cell instanceof Date) {
			cellString = (new SimpleDateFormat(dateFormat)).format((Date)cell);
		} else {
			cellString = String.valueOf(cell);
		}
		
		return cellString;
	}
	
	public boolean isLabel() {
		return false;
	}

}
