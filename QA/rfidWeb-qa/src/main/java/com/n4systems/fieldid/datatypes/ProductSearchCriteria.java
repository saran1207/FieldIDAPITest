package com.n4systems.fieldid.datatypes;

public class ProductSearchCriteria {
	
	String rfidNumber = null;
	String serialNumber = null;
	String orderNumber = null;
	String purchaseOrder = null;
	String customer = null;
	String division = null;
	String jobSite = null;
	String assignedTo = null;
	String location = null;
	String salesAgent = null;
	String referenceNumber = null;
	String productStatus = null;
	String productType = null;
	String fromDate = null;
	String toDate = null;

	public ProductSearchCriteria() {
	}
	
	public ProductSearchCriteria(String serialNumber) {
		this.serialNumber = serialNumber;
	}
	
	public void setRFIDNumber(String rfidNumber) {
		this.rfidNumber = rfidNumber;
	}
	
	public String getRFIDNumber() {
		return this.rfidNumber;
	}
	
	public void setSerialNumber(String serialNumber) {
		this.serialNumber = serialNumber;
	}
	
	public String getSerialNumber() {
		return this.serialNumber;
	}
	
	public void setOrderNumber(String orderNumber) {
		this.orderNumber = orderNumber;
	}
	
	public String getOrderNumber() {
		return this.orderNumber;
	}
	
	public void setPurchaseOrder(String purchaseOrder) {
		this.purchaseOrder = purchaseOrder;
	}
	
	public String getPurchaseOrder() {
		return this.purchaseOrder;
	}
	
	public void setCustomer(String customer) {
		this.customer = customer;
	}
	
	public String getCustomer() {
		return this.customer;
	}
	
	public void setDivision(String division) {
		this.division = division;
	}
	
	public String getDivision() {
		return this.division;
	}
	
	public void setJobSite(String jobSite) {
		this.jobSite = jobSite;
	}
	
	public String getJobSite() {
		return this.jobSite;
	}
	
	public void setAssignedTo(String assignedTo) {
		this.assignedTo = assignedTo;
	}
	
	public String getAssignedTo() {
		return this.assignedTo;
	}
	
	public void setLocation(String location) {
		this.location = location;
	}
	
	public String getLocation() {
		return this.location;
	}
	
	public void setSalesAgent(String salesAgent) {
		this.salesAgent = salesAgent;
	}
	
	public String getSalesAgent() {
		return this.salesAgent;
	}
	
	public void setReferenceNumber(String referenceNumber) {
		this.referenceNumber = referenceNumber;
	}
	
	public String getReferenceNumber() {
		return this.referenceNumber;
	}
	
	public void setProductStatus(String productStatus) {
		this.productStatus = productStatus;
	}
	
	public String getProductStatus() {
		return this.productStatus;
	}
	
	public void setProductType(String productType) {
		this.productType = productType;
	}
	
	public String getProductType() {
		return this.productType;
	}
	
	public void setFromDate(String fromDate) {
		this.fromDate = fromDate;
	}
	
	public String getFromDate() {
		return this.fromDate;
	}
	
	public void setToDate(String toDate) {
		this.toDate = toDate;
	}
	
	public String getToDate() {
		return this.toDate;
	}
}
