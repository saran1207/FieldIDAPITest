package com.n4systems.fieldid.viewhelpers.handlers;

import com.n4systems.fieldid.service.download.TableGenerationContext;
import com.n4systems.fieldid.service.download.WebOutputHandler;


public class AssetLinkHandler extends WebOutputHandler {
	
	public AssetLinkHandler(TableGenerationContext action) {
		super(action);
	}

	public String handleWeb(Long entityId, Object value) {
		// create a link to the asset
		return "<a class=\"identifierLink\" href=\"/fieldid/asset.action?uniqueID=" + entityId + "\" >" + value + "</a>";
	}

	public Object handleExcel(Long entityId, Object value) {
		return value;
	}

}
