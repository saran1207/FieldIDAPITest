package com.n4systems.fieldid.ws.v1.resources.asset;


public class ApiAssetIdentifiers {
	private String identifier;
	private String rfidNumber;
	private String customerRefNumber;

	public String getIdentifier() {
		return identifier;
	}

	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}

	public String getRfidNumber() {
		return rfidNumber;
	}

	public void setRfidNumber(String rfidNumber) {
		this.rfidNumber = rfidNumber;
	}

	public String getCustomerRefNumber() {
		return customerRefNumber;
	}

	public void setCustomerRefNumber(String customerRefNumber) {
		this.customerRefNumber = customerRefNumber;
	}
}
