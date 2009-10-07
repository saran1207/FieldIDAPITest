package com.n4systems.fieldid.viewhelpers.handlers;

import com.n4systems.fieldid.actions.api.AbstractAction;
import com.n4systems.model.api.DisplayEnum;


public class EnumHandler implements OutputHandler {

	public String handle(AbstractAction action, Long entityId, Object value) {
		DisplayEnum label = (DisplayEnum)value;
		
		return label.getLabel();
	}

	public boolean isLabel() {
		return true;
	}
	
	
}
