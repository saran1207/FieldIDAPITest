package com.n4systems.fieldid.viewhelpers.handlers;

import com.n4systems.fieldid.service.download.TableGenerationContext;
import com.n4systems.fieldid.service.download.WebOutputHandler;
import com.n4systems.model.Asset;
import com.n4systems.model.security.SecurityLevel;

public class EventIdentifierHandler extends WebOutputHandler {

	public EventIdentifierHandler(TableGenerationContext action) {
		super(action);
	}
	
	public String handleWeb(Long entityId, Object value) {
		Asset asset = (Asset)value;
		
		SecurityLevel level = asset.getSecurityLevel(contextProvider.getOwner());
		
		// build the asset info link for local assets, just show the identifier for network assets
		String identifier;
		if (level.isLocal()) {
            String absoluteUrl = getAbsoluteUrl();
            absoluteUrl += "/fieldid/w/assetSummary?uniqueID=" + asset.getId();
			identifier = String.format("<a class=\"identifierLink\" href='%s'>%s</a>", absoluteUrl, asset.getIdentifier());
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
