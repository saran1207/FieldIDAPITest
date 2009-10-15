package com.n4systems.fieldid.viewhelpers.handlers;

import java.util.Date;

import com.n4systems.ejb.InspectionManager;
import com.n4systems.fieldid.actions.api.AbstractAction;
import com.n4systems.util.ServiceLocator;

public class LastInspectionDateHandler extends DateTimeHandler {

	private final InspectionManager inspectionManager;
		
	public LastInspectionDateHandler(AbstractAction action) {
		super(action);
		inspectionManager = ServiceLocator.getInspectionManager();
	}

	public String handleWeb(Long entityId, Object value) {
		String outputDate = "";
		
		Date lastDate = getLastDate(entityId);
		if (lastDate != null) {
			return super.handleWeb(entityId, lastDate);
		}
		
		return outputDate;
	}

	private Date getLastDate(Long entityId) {
		Date lastDate = inspectionManager.findLastInspectionDate(entityId);
		return lastDate;
	}
	
	@Override
	public Object handleExcel(Long entityId, Object value) {
		return getLastDate(entityId);
	}
}
