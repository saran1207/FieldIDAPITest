package com.n4systems.fieldid.viewhelpers.handlers;

import com.n4systems.fieldid.service.download.TableGenerationContext;
import com.n4systems.util.ServiceLocator;

import java.util.Date;

public class NetworkLastEventDateHandler extends DateTimeHandler {

	public NetworkLastEventDateHandler(TableGenerationContext action) {
		super(action);
	}
	
	@Override
	public String handleWeb(Long entityId, Object cell) {
		Long networkId = (Long)cell;

        Date lastDate = getLastDate(networkId);
		
		return super.handleWeb(entityId, lastDate);
	}
	

	@Override
	public Object handleExcel(Long entityId, Object value) {
		return getLastDate(entityId);
	}

	private Date getLastDate(Long networkId) {
		Date lastDate = ServiceLocator.getLastEventDateService().findNetworkLastEventDate(networkId);
		return lastDate;
	}

}
