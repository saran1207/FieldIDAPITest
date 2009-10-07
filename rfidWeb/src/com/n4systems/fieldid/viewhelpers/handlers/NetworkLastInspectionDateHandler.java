package com.n4systems.fieldid.viewhelpers.handlers;

import java.util.Date;

import com.n4systems.fieldid.actions.api.AbstractAction;
import com.n4systems.model.product.LastInspectionDateLoader;

public class NetworkLastInspectionDateHandler extends DateTimeHandler {

	private LastInspectionDateLoader lastDateLoader = new LastInspectionDateLoader();	
	
	@Override
	public String handle(AbstractAction action, Long entityId, Object cell) {
		Long networkId = (Long)cell;
		
		Date lastDate = lastDateLoader.setNetworkId(networkId).load();
		
		return super.handle(action, entityId, lastDate);
	}

	public boolean isLabel() {
		return false;
	}

}
