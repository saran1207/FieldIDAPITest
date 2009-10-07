package com.n4systems.fieldid.viewhelpers.handlers;

import com.n4systems.fieldid.actions.api.AbstractAction;


public class ProductLinkHandler implements OutputHandler {
	
	public String handle(AbstractAction action, Long entityId, Object value) {
		// create a link to the product
		return "<a href=\"product.action?uniqueID=" + entityId + "\" >" + (String)value + "</a>";
		
	}
	
	public boolean isLabel() {
		return false;
	}
}
