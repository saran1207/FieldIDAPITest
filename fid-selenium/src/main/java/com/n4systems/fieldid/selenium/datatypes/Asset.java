package com.n4systems.fieldid.selenium.datatypes;


public class Asset {
	String identifier;
	String rfidNumber;
	String referenceNumber;
	SafetyNetworkRegistration registration;
	boolean published = false;
	Owner owner;
	String location;
	String assetStatus;
	String purchaseOrder;
	String identified;
	String assetType;
	String nonIntegrationOrderNumber;
	
	String comments;
	
	public void setIdentifier(String s) {
		this.identifier = s;
	}
	
	public void setRFIDNumber(String s) {
		this.rfidNumber = s;
	}
	
	public void setReferenceNumber(String s) {
		this.referenceNumber = s;
	}

	public void setSafetyNetworkRegistration(SafetyNetworkRegistration registration) {
		this.registration = registration;
	}

	public void setPublished(boolean b) {
		this.published = b;
	}
	
	public void setOwner(Owner owner) {
		this.owner = owner;
	}

	public void setLocation(String location) {
		this.location = location;
	}
	
	public void setAssetStatus(String s) {
		this.assetStatus = s;
	}

	public void setPurchaseOrder(String s) {
		this.purchaseOrder = s;
	}
	
	public void setIdentified(String s) {
		this.identified = s;
	}

	public void setAssetType(String s) {
		this.assetType = s;
	}
	
	public void setComments(String s) {
		this.comments = s;
	}


	public String getIdentifier() {
		return identifier;
	}
	
	public String getRFIDNumber() {
		return rfidNumber;
	}
	
	public String getReferenceNumber() {
		return referenceNumber;
	}

	public SafetyNetworkRegistration getSafetyNetworkRegistration() {
		return registration;
	}

	public boolean getPublished() {
		return published;
	}
	
	public Owner getOwner() {
		return owner;
	}

	public String getLocation() {
		return location;
	}
	
	public String getAssetStatus() {
		return assetStatus;
	}

	public String getPurchaseOrder() {
		return purchaseOrder;
	}
	
	public String getIdentified() {
		return identified;
	}

	public String getAssetType() {
		return assetType;
	}
	
	public String getComments() {
		return comments;
	}

	public String getNonIntegrationOrderNumber() {
		return nonIntegrationOrderNumber;
	}

	public void setNonIntegrationOrderNumber(String nonIntegrationOrderNumber) {
		this.nonIntegrationOrderNumber = nonIntegrationOrderNumber;
	}

	
}
