package com.n4systems.fieldid.viewhelpers.handlers;

import com.n4systems.fieldid.actions.api.AbstractAction;
import com.n4systems.fieldid.utils.WebContextProvider;


public class AssetLinkHandler extends WebOutputHandler {
	
	protected AssetLinkHandler(WebContextProvider action) {
		super(action);
	}

	public String handleWeb(Long entityId, Object value) {
		// create a link to the asset
		return "<a href=\"/fieldid/asset.action?uniqueID=" + entityId + "\" >" + (String)value + "</a>";
	}

	public Object handleExcel(Long entityId, Object value) {
		return value;
	}

}
