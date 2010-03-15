package com.n4systems.fieldid.selenium.datatypes;

/**
 * This is used in Multi Add and can be used for Smart Search
 */
public class Identifier {
	private String serialNumber;
	private String rfidNumber;
	private String referenceNumber;
	
	public Identifier(String serialNumber, String rfidNumber, String referenceNumber) {
		this.serialNumber = serialNumber;
		this.rfidNumber = rfidNumber;
		this.referenceNumber = referenceNumber;
	}
	
	public String getSerialNumber() {
		return serialNumber;
	}
	
	public void setSerialNumber(String serialNumber) {
		this.serialNumber = serialNumber;
	}
	
	public String getRfidNumber() {
		return rfidNumber;
	}

	public void setRfidNumber(String rfidNumber) {
		this.rfidNumber = rfidNumber;
	}
	
	public String getReferenceNumber() {
		return referenceNumber;
	}
	
	public void setReferenceNumber(String referenceNumber) {
		this.referenceNumber = referenceNumber;
	}
}
