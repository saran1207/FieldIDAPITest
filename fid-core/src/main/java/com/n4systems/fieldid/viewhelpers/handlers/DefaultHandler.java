package com.n4systems.fieldid.viewhelpers.handlers;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.n4systems.fieldid.service.download.TableGenerationContext;
import com.n4systems.fieldid.service.download.WebOutputHandler;

/**
 * The default CustomizableSearchAction OutputHandler.  Provides handling of Dates and other special types.
 */
public class DefaultHandler extends WebOutputHandler {

	public DefaultHandler(TableGenerationContext action) {
		super(action);
	}
	
	public String handleWeb(Long entityId, Object cell) {
		String cellString;
		if (cell instanceof Date) {
			cellString = (new SimpleDateFormat(contextProvider.getDateFormat())).format((Date)cell);
		} else {
			cellString = String.valueOf(cell);
		}
		
		return cellString;
	}

	public Object handleExcel(Long entityId, Object value) {
		return value;
	}

}
