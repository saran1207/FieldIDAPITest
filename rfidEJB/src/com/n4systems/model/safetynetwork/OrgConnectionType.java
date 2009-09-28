package com.n4systems.model.safetynetwork;

public enum OrgConnectionType {
	CUSTOMER, VENDOR;
	
	public boolean isCustomer() {
		return (this == CUSTOMER);
	}
	
	public boolean isVendor() {
		return !(this == VENDOR);
	}
	
	public OrgConnectionType getOppositeType() {
		return (this == CUSTOMER) ? VENDOR : CUSTOMER;
	}
}
