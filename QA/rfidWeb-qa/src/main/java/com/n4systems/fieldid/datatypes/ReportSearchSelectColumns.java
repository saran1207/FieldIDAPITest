package com.n4systems.fieldid.datatypes;

public class ReportSearchSelectColumns {

	boolean inspectionDate = true;
	boolean inspectionType = true;
	boolean customerName = true;
	boolean result = true;
	boolean inspectionBook = true;
	boolean inspector = true;
	boolean division = true;
	boolean organization = true;
//	boolean charge = true;
	boolean comments = true;
	boolean location = true;
	boolean peakLoad = true;
	boolean testDuration = true;
	boolean peakLoadDuration = true;
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
	
	public ReportSearchSelectColumns() {
		super();
	}
	
	public void setInspectionDate(boolean b) {
		this.inspectionDate = b;
	}

	public void setInspectionType(boolean b) {
		this.inspectionType = b;
	}

	public void setCustomerName(boolean b) {
		this.customerName = b;
	}

	public void setResult(boolean b) {
		this.result = b;
	}

	public void setInspectionBook(boolean b) {
		this.inspectionBook = b;
	}

	public void setInspector(boolean b) {
		this.inspector = b;
	}

	public void setDivision(boolean b) {
		this.division = b;
	}

	public void setOrganization(boolean b) {
		this.organization = b;
	}

//	public void setCharge(boolean b) {
//		this.charge = b;
//	}

	public void setComments(boolean b) {
		this.comments = b;
	}

	public void setLocation(boolean b) {
		this.location = b;
	}

	public void setPeakLoad(boolean b) {
		this.peakLoad = b;
	}

	public void setTestDuration(boolean b) {
		this.testDuration = b;
	}

	public void setPeakLoadDuration(boolean b) {
		this.peakLoadDuration = b;
	}

	public void setSerialNumber(boolean b) {
		this.serialNumber = b;
	}

	public void setRFIDNumber(boolean b) {
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

	public boolean getInspectionDate() {
		return inspectionDate;
	}

	public boolean getInspectionType() {
		return inspectionType;
	}

	public boolean getCustomerName() {
		return customerName;
	}

	public boolean getResult() {
		return result;
	}

	public boolean getInspectionBook() {
		return inspectionBook;
	}

	public boolean getInspector() {
		return inspector;
	}

	public boolean getDivision() {
		return division;
	}

	public boolean getOrganization() {
		return organization;
	}

//	public boolean getCharge() {
//		return charge;
//	}

	public boolean getComments() {
		return comments;
	}

	public boolean getLocation() {
		return location;
	}

	public boolean getPeakLoad() {
		return peakLoad;
	}

	public boolean getTestDuration() {
		return testDuration;
	}

	public boolean getPeakLoadDuration() {
		return peakLoadDuration;
	}

	public boolean getSerialNumber() {
		return serialNumber;
	}

	public boolean getRFIDNumber() {
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
		inspectionDate = true;
		inspectionType = true;
		customerName = true;
		result = true;
		inspectionBook = true;
		inspector = true;
		division = true;
		organization = true;
//		charge = true;
		comments = true;
		location = true;
		peakLoad = true;
		testDuration = true;
		peakLoadDuration = true;
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
		inspectionDate = false;
		inspectionType = false;
		customerName = false;
		result = false;
		inspectionBook = false;
		inspector = false;
		division = false;
		organization = false;
//		charge = false;
		comments = false;
		location = false;
		peakLoad = false;
		testDuration = false;
		peakLoadDuration = false;
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
