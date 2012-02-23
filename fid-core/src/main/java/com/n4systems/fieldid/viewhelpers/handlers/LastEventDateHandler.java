package com.n4systems.fieldid.viewhelpers.handlers;

import com.n4systems.ejb.EventManager;
import com.n4systems.fieldid.service.download.TableGenerationContext;
import com.n4systems.util.ServiceLocator;

import java.util.Date;

public class LastEventDateHandler extends DateTimeHandler {

	private final EventManager eventManager;
		
	public LastEventDateHandler(TableGenerationContext action) {
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
