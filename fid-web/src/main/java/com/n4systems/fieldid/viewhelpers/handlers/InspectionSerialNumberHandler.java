package com.n4systems.fieldid.viewhelpers.handlers;

import com.n4systems.fieldid.actions.api.AbstractAction;
import com.n4systems.model.Asset;
import com.n4systems.model.security.SecurityLevel;

public class InspectionSerialNumberHandler extends WebOutputHandler {

	public InspectionSerialNumberHandler(AbstractAction action) {
		super(action);
	}
	
	public String handleWeb(Long entityId, Object value) {
		Asset asset = (Asset)value;
		
		SecurityLevel level = asset.getSecurityLevel(action.getSecurityFilter().getOwner());
		
		// build the asset info link for local products, just show the serial for network products
		String serialNumber;
		if (level.isLocal()) { 
			serialNumber = String.format("<a href='asset.action?uniqueID=%d'>%s</a>", asset.getId(), asset.getSerialNumber());
		} else {
			serialNumber = asset.getSerialNumber();
		}
		
		return serialNumber;
	}

	public Object handleExcel(Long entityId, Object value) {
		Asset product = (Asset)value;
		
		return product.getSerialNumber();
	}
	
}
