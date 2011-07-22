package com.n4systems.fieldid.viewhelpers.handlers;

import com.n4systems.fieldid.utils.WebContextProvider;
import com.n4systems.model.Asset;
import com.n4systems.model.security.SecurityLevel;

public class EventRfidNumberHandler extends WebOutputHandler {

	public EventRfidNumberHandler(WebContextProvider action) {
		super(action);
	}
	
	public String handleWeb(Long entityId, Object value) {
		Asset asset = (Asset)value;
		
		if (asset.getRfidNumber() == null) {
			// if the rfid is null, there's no point doing any more work
			return "";
		}
		
		SecurityLevel level = asset.getSecurityLevel(contextProvider.getSecurityFilter().getOwner());
		
		// build the asset info link for local assets, just show the serial for network assets
		String rfidNumber;
		if (level.isLocal()) { 
			rfidNumber = String.format("<a href='/fieldid/asset.action?uniqueID=%d'>%s</a>", asset.getId(), asset.getRfidNumber());
		} else {
			rfidNumber = asset.getRfidNumber();
		}
		
		return rfidNumber;
	}

	public Object handleExcel(Long entityId, Object value) {
		Asset asset = (Asset)value;
		
		return (asset.getRfidNumber() != null) ? asset.getRfidNumber() : "";
	}
	
}
