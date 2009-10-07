package com.n4systems.fieldid.viewhelpers.handlers;

import java.util.Date;

import com.n4systems.ejb.InspectionManager;
import com.n4systems.fieldid.actions.api.AbstractAction;
import com.n4systems.util.ServiceLocator;

public class LastInspectionDateHandler extends DateTimeHandler {

	private final InspectionManager inspectionManager;
		
	public LastInspectionDateHandler() {
		inspectionManager = ServiceLocator.getInspectionManager();
	}

	public String handle(AbstractAction action, Long entityId, Object value) {
		String outputDate = "";
		
		Date lastDate = inspectionManager.findLastInspectionDate(entityId);
		if (lastDate != null) {
			return super.handle(action, entityId, lastDate);
		}
		
		return outputDate;
	}
	
	public boolean isLabel() {
		return false;
	}

	
}
