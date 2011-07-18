package com.n4systems.fieldid.viewhelpers.handlers;

import com.n4systems.fieldid.utils.WebContextProvider;
import com.n4systems.model.api.DisplayEnum;


public class EnumHandler extends WebOutputHandler {

	public EnumHandler(WebContextProvider action) {
		super(action);
	}

	public String handleWeb(Long entityId, Object value) {
		DisplayEnum label = (DisplayEnum)value;
		
		return contextProvider.getText(label.getLabel());
	}

	public Object handleExcel(Long entityId, Object value) {
		return handleWeb(entityId, value);
	}
	
}
