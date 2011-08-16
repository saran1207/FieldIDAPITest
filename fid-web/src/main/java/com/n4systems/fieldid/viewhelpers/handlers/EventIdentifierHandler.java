package com.n4systems.fieldid.viewhelpers.handlers;

import com.n4systems.fieldid.utils.WebContextProvider;
import com.n4systems.model.Asset;
import com.n4systems.model.security.SecurityLevel;

public class EventIdentifierHandler extends WebOutputHandler {

	public EventIdentifierHandler(WebContextProvider action) {
		super(action);
	}
	
	public String handleWeb(Long entityId, Object value) {
		Asset asset = (Asset)value;
		
		SecurityLevel level = asset.getSecurityLevel(contextProvider.getSecurityFilter().getOwner());
		
		// build the asset info link for local assets, just show the identifier for network assets
		String identifier;
		if (level.isLocal()) { 
			identifier = String.format("<a class=\"identifierLink\" href='/fieldid/asset.action?uniqueID=%d'>%s</a>", asset.getId(), asset.getIdentifier());
		} else {
			identifier = asset.getIdentifier();
		}
		
		return identifier;
	}

	public Object handleExcel(Long entityId, Object value) {
		Asset asset = (Asset)value;
		
		return asset.getIdentifier();
	}
	
}
