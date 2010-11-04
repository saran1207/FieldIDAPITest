package com.n4systems.fieldid.viewhelpers.handlers;

import java.util.Date;

import com.n4systems.ejb.EventManager;
import com.n4systems.fieldid.actions.api.AbstractAction;
import com.n4systems.util.ServiceLocator;

public class LastEventDateHandler extends DateTimeHandler {

	private final EventManager eventManager;
		
	public LastEventDateHandler(AbstractAction action) {
		super(action);
		eventManager = ServiceLocator.getEventManager();
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
		Date lastDate = eventManager.findLastEventDate(entityId);
		return lastDate;
	}
	
	@Override
	public Object handleExcel(Long entityId, Object value) {
		return getLastDate(entityId);
	}
}
