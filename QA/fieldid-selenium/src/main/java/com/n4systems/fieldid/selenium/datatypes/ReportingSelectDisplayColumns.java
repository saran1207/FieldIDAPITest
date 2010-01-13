package com.n4systems.fieldid.selenium.datatypes;

public class ReportingSelectDisplayColumns {
	boolean inspectionDate = false;
	boolean inspectionType = true;
	boolean customerName = true;
	boolean result = true;
	boolean inspectionBook = false;
	boolean inspector = false;
	boolean division = false;
	boolean organization = false;
	boolean comments = false;
	boolean location = false;
	boolean peakLoad = false;
	boolean testDuration = false;
	boolean peakLoadDuration = false;
	boolean serialNumber = true;
	boolean rfidNumber = false;
	boolean referenceNumber = true;
	boolean productTypeGroup = false;
	boolean productType = true;
	boolean productStatus = true;
	boolean dateIdentified = false;
	boolean identifiedBy = false;
	boolean description = false;
	boolean partNumber = false;
	boolean orderDescription = true;
	boolean orderNumber = false;
	boolean purchaseOrder = false;
	
	public void setSerialNumber(boolean b) {
		serialNumber = b;
	}
	
	public void setRFIDNumber(boolean b) {
		rfidNumber = b;
	}
	
	public void setCustomerName(boolean b) {
		customerName = b;
	}
	
	public void setDivision(boolean b) {
		division = b;
	}
	
	public void setOrganization(boolean b) {
		organization = b;
	}
	
	public void setReferenceNumber(boolean b) {
		referenceNumber = b;
	}
	
	public void setProductTypeGroup(boolean b) {
		productTypeGroup = b;
	}
	
	public void setProductType(boolean b) {
		productType = b;
	}
	
	public void setProductStatus(boolean b) {
		productStatus = b;
	}
	
	public void setDateIdentified(boolean b) {
		dateIdentified = b;
	}
	
	public void setIdentifiedBy(boolean b) {
		identifiedBy = b;
	}
	
	public void setComments(boolean b) {
		comments = b;
	}
	
	public void setDescription(boolean b) {
		description = b;
	}
	
	public void setLocation(boolean b) {
		location = b;
	}
	
	public void setOrderDescription(boolean b) {
		orderDescription = b;
	}
	
	public void setOrderNumber(boolean b) {
		orderNumber = b;
	}
	
	public void setPurchaseOrder(boolean b) {
		purchaseOrder = b;
	}
	
	public boolean getSerialNumber() {
		return serialNumber;
	}
	
	public boolean getrfidNumber() {
		return rfidNumber;
	}
	
	public boolean getCustomerName() {
		return customerName;
	}
	
	public boolean getDivision() {
		return division;
	}
	
	public boolean getOrganization() {
		return organization;
	}
	
	public boolean getReferenceNumber() {
		return referenceNumber;
	}
	
	public boolean getProductTypeGroup() {
		return productTypeGroup;
	}
	
	public boolean getProductType() {
		return productType;
	}
	
	public boolean getProductStatus() {
		return productStatus;
	}
	
	public boolean getDateIdentified() {
		return dateIdentified;
	}
	
	public boolean getIdentifiedBy() {
		return identifiedBy;
	}
	
	public boolean getComments() {
		return comments;
	}
	
	public boolean getDescription() {
		return description;
	}
	
	public boolean getLocation() {
		return location;
	}
	
	public boolean getOrderDescription() {
		return orderDescription;
	}
	
	public boolean getOrderNumber() {
		return orderNumber;
	}
	
	public boolean getPurchaseOrder() {
		return purchaseOrder;
	}

	public void setAll(boolean b) {
		inspectionDate = b;
		inspectionType = b;
		customerName = b;
		result = b;
		inspectionBook = b;
		inspector = b;
		division = b;
		organization = b;
		comments = b;
		location = b;
		peakLoad = b;
		testDuration = b;
		peakLoadDuration = b;
		serialNumber = b;
		rfidNumber = b;
		referenceNumber = b;
		productTypeGroup = b;
		productType = b;
		productStatus = b;
		dateIdentified = b;
		identifiedBy = b;
		description = b;
		partNumber = b;
		orderDescription = b;
		orderNumber = b;
		purchaseOrder = b;
	}
}
