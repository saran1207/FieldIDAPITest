package com.n4systems.fieldid.selenium.datatypes;

/**
 * This is used in Multi Add and can be used for Smart Search
 */
public class Identifier {
	private String identifier;
	private String rfidNumber;
	private String referenceNumber;
	
	public Identifier(String identifier, String rfidNumber, String referenceNumber) {
		this.identifier = identifier;
		this.rfidNumber = rfidNumber;
		this.referenceNumber = referenceNumber;
	}
	
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
	
	public String getReferenceNumber() {
		return referenceNumber;
	}
	
	public void setReferenceNumber(String referenceNumber) {
		this.referenceNumber = referenceNumber;
	}
}
