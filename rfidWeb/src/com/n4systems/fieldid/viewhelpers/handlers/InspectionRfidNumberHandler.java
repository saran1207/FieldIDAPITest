package com.n4systems.fieldid.viewhelpers.handlers;

import com.n4systems.fieldid.actions.api.AbstractAction;
import com.n4systems.model.Product;
import com.n4systems.model.security.SecurityLevel;

public class InspectionRfidNumberHandler extends WebOutputHandler {

	public InspectionRfidNumberHandler(AbstractAction action) {
		super(action);
	}
	
	public String handleWeb(Long entityId, Object value) {
		Product product = (Product)value;
		
		if (product.getRfidNumber() == null) {
			// if the rfid is null, there's no point doing any more work
			return "";
		}
		
		SecurityLevel level = product.getSecurityLevel(action.getSecurityFilter().getOwner());
		
		// build the product info link for local products, just show the serial for network products
		String rfidNumber;
		if (level.isLocal()) { 
			rfidNumber = String.format("<a href='product.action?uniqueID=%d'>%s</a>", product.getId(), product.getRfidNumber());
		} else {
			rfidNumber = product.getRfidNumber();
		}
		
		return rfidNumber;
	}

	public Object handleExcel(Long entityId, Object value) {
		Product product = (Product)value;
		
		return (product.getRfidNumber() != null) ? product.getRfidNumber() : "";
	}
	
}
