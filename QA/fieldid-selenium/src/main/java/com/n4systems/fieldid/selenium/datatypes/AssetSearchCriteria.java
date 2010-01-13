package com.n4systems.fieldid.selenium.datatypes;

public class AssetSearchCriteria {
	String rfidNumber;
	String serialNumber;
	String orderNumber;
	String purchaseOrder;
	String referenceNumber;
	String productStatus;
	String productTypeGroup;
	String productType;
	String location;
	Owner owner;
	String fromDate;
	String toDate;
	
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
	
	public String getProductStatus() {
		return this.productStatus;
	}
	
	public String getProductTypeGroup() {
		return this.productTypeGroup;
	}
	
	public String getProductType() {
		return this.productType;
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
	
	public void setProductStatus(String s) {
		this.productStatus = s;
	}
	
	public void setProductTypeGroup(String s) {
		this.productTypeGroup = s;
	}
	
	public void setProductType(String s) {
		this.productType = s;
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
}
