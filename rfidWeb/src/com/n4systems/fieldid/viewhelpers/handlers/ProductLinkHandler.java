package com.n4systems.fieldid.viewhelpers.handlers;


public class ProductLinkHandler implements OutputHandler {
	public String handle(Long entityId, Object value) {
		// create a link to the product
		return "<a href=\"product.action?uniqueID=" + entityId + "\" >" + (String)value + "</a>";
		
	}
	
	public boolean isLabel() {
		return false;
	}
}
