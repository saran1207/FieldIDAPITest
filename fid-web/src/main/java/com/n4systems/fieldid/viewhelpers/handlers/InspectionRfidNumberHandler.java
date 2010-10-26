package com.n4systems.fieldid.viewhelpers.handlers;

import com.n4systems.fieldid.actions.api.AbstractAction;
import com.n4systems.model.Asset;
import com.n4systems.model.security.SecurityLevel;

public class InspectionRfidNumberHandler extends WebOutputHandler {

	public InspectionRfidNumberHandler(AbstractAction action) {
		super(action);
	}
	
	public String handleWeb(Long entityId, Object value) {
		Asset asset = (Asset)value;
		
		if (asset.getRfidNumber() == null) {
			// if the rfid is null, there's no point doing any more work
			return "";
		}
		
		SecurityLevel level = asset.getSecurityLevel(action.getSecurityFilter().getOwner());
		
		// build the asset info link for local products, just show the serial for network products
		String rfidNumber;
		if (level.isLocal()) { 
			rfidNumber = String.format("<a href='asset.action?uniqueID=%d'>%s</a>", asset.getId(), asset.getRfidNumber());
		} else {
			rfidNumber = asset.getRfidNumber();
		}
		
		return rfidNumber;
	}

	public Object handleExcel(Long entityId, Object value) {
		Asset product = (Asset)value;
		
		return (product.getRfidNumber() != null) ? product.getRfidNumber() : "";
	}
	
}
