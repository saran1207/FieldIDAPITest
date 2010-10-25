package com.n4systems.fieldid.selenium.datatypes;

public class AssetSearchCriteria {
	private String rfidNumber;
	private String serialNumber;
	private String orderNumber;
	private String purchaseOrder;
	private String assignedTo;
	private String referenceNumber;
	private String assetStatus;
	private String assetTypeGroup;
	private String assetType;
	private String location;
	private Owner owner;
	private String fromDate;
	private String toDate;
	
	public AssetSearchCriteria() {
	}

	public String getRFIDNumber() {
		return this.rfidNumber;
	}

	public String getSerialNumber() {
		return this.serialNumber;
	}
	
	public String getOrderNumber() {
		return this.orderNumber;
	}
	
	public String getPurchaseOrder() {
		return this.purchaseOrder;
	}
	
	public String getReferenceNumber() {
		return this.referenceNumber;
	}
	
	public String getAssetStatus() {
		return this.assetStatus;
	}
	
	public String getAssetTypeGroup() {
		return this.assetTypeGroup;
	}
	
	public String getAssetType() {
		return this.assetType;
	}
	
	public String getLocation() {
		return this.location;
	}
	
	public Owner getOwner() {
		return this.owner;
	}
	
	public String getFromDate() {
		return this.fromDate;
	}
	
	public String getToDate() {
		return this.toDate;
	}

	public void setRFIDNumber(String s) {
		this.rfidNumber = s;
	}

	public void setSerialNumber(String s) {
		this.serialNumber = s;
	}
	
	public void setOrderNumber(String s) {
		this.orderNumber = s;
	}
	
	public void setPurchaseOrder(String s) {
		this.purchaseOrder = s;
	}
	
	public void setReferenceNumber(String s) {
		this.referenceNumber = s;
	}
	
	public void setAssetStatus(String s) {
		this.assetStatus = s;
	}
	
	public void setAssetTypeGroup(String s) {
		this.assetTypeGroup = s;
	}
	
	public void setAssetType(String s) {
		this.assetType = s;
	}
	
	public void setLocation(String s) {
		this.location = s;
	}
	
	public void setOwner(Owner o) {
		this.owner = o;
	}
	
	public void setFromDate(String s) {
		this.fromDate = s;
	}
	
	public void setToDate(String s) {
		this.toDate = s;
	}

	public String getAssignedTo() {
		return assignedTo;
	}

	public void setAssignedTo(String assignedTo) {
		this.assignedTo = assignedTo;
	}
}
