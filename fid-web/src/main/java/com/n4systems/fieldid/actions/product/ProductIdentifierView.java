package com.n4systems.fieldid.actions.product;

import java.io.Serializable;

public class ProductIdentifierView implements Serializable {
	private static final long serialVersionUID = 1L;

	private String serialNumber;
	private String rfidNumber;
	private String referenceNumber;
	
	public ProductIdentifierView() {}
	
	public ProductIdentifierView(String serialNumber) {
		this.serialNumber = serialNumber;
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
