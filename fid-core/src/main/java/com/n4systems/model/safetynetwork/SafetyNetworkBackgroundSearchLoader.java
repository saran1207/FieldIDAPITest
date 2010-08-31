package com.n4systems.model.safetynetwork;

import java.util.List;

import com.n4systems.model.Product;
import com.n4systems.model.security.SecurityFilter;

public class SafetyNetworkBackgroundSearchLoader {
	
	private final SafetyNetworkSmartSearchLoader networkSmartSearchLoader;
	private String serialNumber;
	private String rfidNumber;
	private String refNumber;
	
	public SafetyNetworkBackgroundSearchLoader(SecurityFilter filter) {
		networkSmartSearchLoader = new SafetyNetworkSmartSearchLoader(filter);
	}
	
	public Product load() {
		Product product = searchByRfidNumber();
		
		if (product == null) {
			product = searchBySerialNumber();
		}
		
		if (product == null) {
			product = searchByRefNumber();
		}
		
		return product;
	}
	
	private Product searchByRfidNumber() {
		Product product = null;
		if (rfidNumber != null && rfidNumber.length() > 0) {
			List<Product> products = networkSmartSearchLoader.useOnlyRfidNumber().setSearchText(rfidNumber).load();
			product = pullProductFromList(products);
		}
		return product;
	}
	
	private Product searchBySerialNumber() {
		Product product = null;
		if (serialNumber != null && serialNumber.length() > 0) {
			List<Product> products = networkSmartSearchLoader.useOnlySerialNumber().setSearchText(serialNumber).load();
			product = pullProductFromList(products);
		}
		return product;
	}
	
	private Product searchByRefNumber() {
		Product product = null;
		if (refNumber != null && refNumber.length() > 0) {
			List<Product> products = networkSmartSearchLoader.useOnlyRefNumber().setSearchText(refNumber).load();
			product = pullProductFromList(products);
		}
		return product;
	}
	
	private Product pullProductFromList(List<Product> products) {
		Product product = null;
		
		if (products.size() > 0) {
			product = products.get(0);
		}
		
		return product;		
	}
	
	public SafetyNetworkBackgroundSearchLoader setVendorOrgId(Long vendorOrgId) {
		networkSmartSearchLoader.setVendorOrgId(vendorOrgId);
		return this;
	}

	public SafetyNetworkBackgroundSearchLoader setSerialNumber(String serialNumber) {
		this.serialNumber = serialNumber;
		return this;
	}

	public SafetyNetworkBackgroundSearchLoader setRfidNumber(String rfidNumber) {
		this.rfidNumber = rfidNumber;
		return this;
	}

	public SafetyNetworkBackgroundSearchLoader setRefNumber(String refNumber) {
		this.refNumber = refNumber;
		return this;
	}
}
