package com.n4systems.fieldid.datatypes;

public class ScheduleSearchSelectColumns {

	// everything defaults to true
	boolean customerName = true;
	boolean division = true;
	boolean location = true;
	boolean scheduledDate = true;
	boolean status = true;
	boolean daysPastDue = true;
	boolean inspectionType = true;
	boolean lastInspectionDate = true;
	boolean serialNumber = true;
	boolean rfidNumber = true;
	boolean referenceNumber = true;
	boolean productType = true;
	boolean productStatus = true;
	boolean dateIdentified = true;
	boolean identifiedBy = true;
	boolean description = true;
	boolean partNumber = true;
	boolean orderDescription = true;
	boolean orderNumber = true;
	boolean purchaseOrder = true;

	public ScheduleSearchSelectColumns() {
		super();
	}
	
	public void setCustomerName(boolean b) {
		this.customerName = b;
	}
	
	public void setDivision(boolean b) {
		this.division = b;
	}
	
	public void setLocation(boolean b) {
		this.location = b;
	}
	
	public void setScheduledDate(boolean b) {
		this.scheduledDate = b;
	}
	
	public void setStatus(boolean b) {
		this.status = b;
	}
	
	public void setDaysPastDue(boolean b) {
		this.daysPastDue = b;
	}
	
	public void setInspectionType(boolean b) {
		this.inspectionType = b;
	}
	
	public void setLastInspectionDate(boolean b) {
		this.lastInspectionDate = b;
	}
	
	public void setSerialNumber(boolean b) {
		this.serialNumber = b;
	}
	
	public void setRfidNumber(boolean b) {
		this.rfidNumber = b;
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
	
	public void setDateIdentified(boolean b) {
		this.dateIdentified = b;
	}
	
	public void setIdentifiedBy(boolean b) {
		this.identifiedBy = b;
	}
	
	public void setDescription(boolean b) {
		this.description = b;
	}
	
	public void setPartNumber(boolean b) {
		this.partNumber = b;
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

	public boolean getCustomerName() {
		return customerName;
	}
	
	public boolean getDivision() {
		return division;
	}
	
	public boolean getLocation() {
		return location;
	}
	
	public boolean getScheduledDate() {
		return scheduledDate;
	}
	
	public boolean getStatus() {
		return status;
	}
	
	public boolean getDaysPastDue() {
		return daysPastDue;
	}
	
	public boolean getInspectionType() {
		return inspectionType;
	}
	
	public boolean getLastInspectionDate() {
		return lastInspectionDate;
	}
	
	public boolean getSerialNumber() {
		return serialNumber;
	}
	
	public boolean getRfidNumber() {
		return rfidNumber;
	}
	
	public boolean getReferenceNumber() {
		return referenceNumber;
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
	
	public boolean getDescription() {
		return description;
	}
	
	public boolean getPartNumber() {
		return partNumber;
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

	public void setAllOn() {
		customerName = true;
		division = true;
		location = true;
		scheduledDate = true;
		status = true;
		daysPastDue = true;
		inspectionType = true;
		lastInspectionDate = true;
		serialNumber = true;
		rfidNumber = true;
		referenceNumber = true;
		productType = true;
		productStatus = true;
		dateIdentified = true;
		identifiedBy = true;
		description = true;
		partNumber = true;
		orderDescription = true;
		orderNumber = true;
		purchaseOrder = true;
	}
	
	public void setAllOff() {
		customerName = false;
		division = false;
		location = false;
		scheduledDate = false;
		status = false;
		daysPastDue = false;
		inspectionType = false;
		lastInspectionDate = false;
		serialNumber = false;
		rfidNumber = false;
		referenceNumber = false;
		productType = false;
		productStatus = false;
		dateIdentified = false;
		identifiedBy = false;
		description = false;
		partNumber = false;
		orderDescription = false;
		orderNumber = false;
		purchaseOrder = false;
	}
}
