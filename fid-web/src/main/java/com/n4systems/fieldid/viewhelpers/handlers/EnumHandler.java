package com.n4systems.fieldid.viewhelpers.handlers;

import com.n4systems.fieldid.actions.api.AbstractAction;
import com.n4systems.model.api.DisplayEnum;


public class EnumHandler extends WebOutputHandler {

	public EnumHandler(AbstractAction action) {
		super(action);
	}

	public String handleWeb(Long entityId, Object value) {
		DisplayEnum label = (DisplayEnum)value;
		
		return action.getText(label.getLabel());
	}

	public Object handleExcel(Long entityId, Object value) {
		return handleWeb(entityId, value);
	}
	
}
