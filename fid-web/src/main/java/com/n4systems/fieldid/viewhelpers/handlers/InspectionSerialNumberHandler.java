package com.n4systems.fieldid.viewhelpers.handlers;

import com.n4systems.fieldid.actions.api.AbstractAction;
import com.n4systems.model.Product;
import com.n4systems.model.security.SecurityLevel;

public class InspectionSerialNumberHandler extends WebOutputHandler {

	public InspectionSerialNumberHandler(AbstractAction action) {
		super(action);
	}
	
	public String handleWeb(Long entityId, Object value) {
		Product product = (Product)value;
		
		SecurityLevel level = product.getSecurityLevel(action.getSecurityFilter().getOwner());
		
		// build the product info link for local products, just show the serial for network products
		String serialNumber;
		if (level.isLocal()) { 
			serialNumber = String.format("<a href='product.action?uniqueID=%d'>%s</a>", product.getId(), product.getSerialNumber());
		} else {
			serialNumber = product.getSerialNumber();
		}
		
		return serialNumber;
	}

	public Object handleExcel(Long entityId, Object value) {
		Product product = (Product)value;
		
		return product.getSerialNumber();
	}
	
}
