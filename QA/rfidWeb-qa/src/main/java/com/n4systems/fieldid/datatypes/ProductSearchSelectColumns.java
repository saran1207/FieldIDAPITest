package com.n4systems.fieldid.datatypes;

public class ProductSearchSelectColumns {

	// Everything defaults to true
	boolean serialNumber = true;
	boolean rfidNumber = true;
	boolean customerName = true;
	boolean division = true;
	boolean organization = true;
	boolean referenceNumber = true;
	boolean productType = true;
	boolean productStatus = true;
	boolean lastInspectionDate = true;
	boolean dateIdentified = true;
	boolean identifiedBy = true;
	boolean description = true;
	boolean location = true;
	boolean safetyNetwork = true;
	boolean orderDescription = true;
	boolean orderNumber = true;
	boolean purchaseOrder = true;

	public ProductSearchSelectColumns() {
		super();
	}
	
	public void setSerialNumber(boolean b) {
		this.serialNumber = b;
	}
	
	public void setRFIDNumber(boolean b) {
		this.rfidNumber = b;
	}
	
	public void setCustomerName(boolean b) {
		this.customerName = b;
	}
	
	public void setDivision(boolean b) {
		this.division = b;
	}
	
	public void setOrganization(boolean b) {
		this.organization = b;
	}
	
	public void setReferenceNumber(boolean b) {
		this.referenceNumber = b;
	}
	
	public void setProductType(boolean b) {
		this.productType = b;
	}
	
	public void setProductStatus(boolean b) {
		this.productStatus = b;
	}
	
	public void setLastInspectionDate(boolean b) {
		this.lastInspectionDate = b;
	}
	
	public void setDateIdentified(boolean b) {
		this.dateIdentified = b;
	}
	
	public void setIdentifiedBy(boolean b) {
		this.identifiedBy = b;
	}
	
	public void setDescription(boolean b) {
		this.description = b;
	}
	
	public void setLocation(boolean b) {
		this.location = b;
	}
	
	public void setSafetyNetwork(boolean b) {
		this.safetyNetwork = b;
	}
	
	public void setOrderDescription(boolean b) {
		this.orderDescription = b;	
	}
	
	public void setOrderNumber(boolean b) {
		this.orderNumber = b;	
	}
	
	public void setPurchaseOrder(boolean b) {
		this.purchaseOrder = b;
	}
	
	public boolean getSerialNumber() {
		return this.serialNumber;
	}
	
	public boolean getRFIDNumber() {
		return this.rfidNumber;
	}
	
	public boolean getCustomerName() {
		return this.customerName;
	}
	
	public boolean getDivision() {
		return this.division;
	}
	
	public boolean getOrganization() {
		return this.organization;
	}
	
	public boolean getReferenceNumber() {
		return this.referenceNumber;
	}
	
	public boolean getProductType() {
		return this.productType;
	}
	
	public boolean getProductStatus() {
		return this.productStatus;
	}
	
	public boolean getLastInspectionDate() {
		return this.lastInspectionDate;
	}
	
	public boolean getDateIdentified() {
		return this.dateIdentified;
	}
	
	public boolean getIdentifiedBy() {
		return this.identifiedBy;
	}
	
	public boolean getDescription() {
		return this.description;
	}
	
	public boolean getLocation() {
		return this.location;
	}
	
	public boolean getSafetyNetwork() {
		return this.safetyNetwork;
	}
	
	public boolean getOrderDescription() {
		return this.orderDescription;	
	}
	
	public boolean getOrderNumber() {
		return this.orderNumber;	
	}
	
	public boolean getPurchaseOrder() {
		return this.purchaseOrder;
	}
	
	public void setAllOn() {
		this.serialNumber = true;
		this.rfidNumber = true;
		this.customerName = true;
		this.division = true;
		this.organization = true;
		this.referenceNumber = true;
		this.productType = true;
		this.productStatus = true;
		this.lastInspectionDate = true;
		this.dateIdentified = true;
		this.identifiedBy = true;
		this.description = true;
		this.location = true;
		this.safetyNetwork = true;
		this.orderDescription = true;
		this.orderNumber = true;
		this.purchaseOrder = true;
	}
	
	public void setAllOff() {
		this.serialNumber = false;
		this.rfidNumber = false;
		this.customerName = false;
		this.division = false;
		this.organization = false;
		this.referenceNumber = false;
		this.productType = false;
		this.productStatus = false;
		this.lastInspectionDate = false;
		this.dateIdentified = false;
		this.identifiedBy = false;
		this.description = false;
		this.location = false;
		this.safetyNetwork = false;
		this.orderDescription = false;
		this.orderNumber = false;
		this.purchaseOrder = false;
	}
}
