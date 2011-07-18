package com.n4systems.fieldid.viewhelpers.handlers;

import com.n4systems.fieldid.utils.WebContextProvider;
import com.n4systems.model.Asset;
import com.n4systems.model.security.SecurityLevel;

public class EventSerialNumberHandler extends WebOutputHandler {

	public EventSerialNumberHandler(WebContextProvider action) {
		super(action);
	}
	
	public String handleWeb(Long entityId, Object value) {
		Asset asset = (Asset)value;
		
		SecurityLevel level = asset.getSecurityLevel(contextProvider.getSecurityFilter().getOwner());
		
		// build the asset info link for local assets, just show the serial for network assets
		String serialNumber;
		if (level.isLocal()) { 
			serialNumber = String.format("<a href='asset.action?uniqueID=%d'>%s</a>", asset.getId(), asset.getSerialNumber());
		} else {
			serialNumber = asset.getSerialNumber();
		}
		
		return serialNumber;
	}

	public Object handleExcel(Long entityId, Object value) {
		Asset asset = (Asset)value;
		
		return asset.getSerialNumber();
	}
	
}
