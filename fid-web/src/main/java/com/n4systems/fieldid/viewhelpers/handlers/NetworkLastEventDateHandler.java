package com.n4systems.fieldid.viewhelpers.handlers;

import com.n4systems.fieldid.utils.WebContextProvider;
import com.n4systems.model.asset.LastEventDateLoader;

import java.util.Date;

public class NetworkLastEventDateHandler extends DateTimeHandler {

	private LastEventDateLoader lastDateLoader = new LastEventDateLoader();
	
	public NetworkLastEventDateHandler(WebContextProvider action) {
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
		Date lastDate = lastDateLoader.setNetworkId(networkId).load();
		return lastDate;
	}

}
