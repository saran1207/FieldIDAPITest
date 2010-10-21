package com.n4systems.fieldid.viewhelpers.handlers;

import com.n4systems.fieldid.actions.api.AbstractAction;


public class ProductLinkHandler extends WebOutputHandler {
	
	protected ProductLinkHandler(AbstractAction action) {
		super(action);
	}

	public String handleWeb(Long entityId, Object value) {
		// create a link to the asset
		return "<a href=\"product.action?uniqueID=" + entityId + "\" >" + (String)value + "</a>";
	}

	public Object handleExcel(Long entityId, Object value) {
		return value;
	}

}
