package com.n4systems.fieldid.viewhelpers.handlers;

import com.n4systems.fieldid.actions.api.AbstractAction;
import com.n4systems.model.Product;
import com.n4systems.model.security.SecurityLevel;

public class InspectionSerialNumberHandler implements OutputHandler {

	public InspectionSerialNumberHandler() {}
	
	public String handle(AbstractAction action, Long entityId, Object value) {
		Product product = (Product)value;
		
		SecurityLevel level = product.getSecurityLevel(action.getInternalOrg());
		
		// build the product info link for local products, just show the serial for network products
		String serialNumber;
		if (level.isLocal()) { 
			serialNumber = String.format("<a href='product.action?uniqueID=%d'>%s</a>", product.getId(), product.getSerialNumber());
		} else {
			serialNumber = product.getSerialNumber();
		}
		
		return serialNumber;
	}
	
	public boolean isLabel() {
		return false;
	}
	
}
