package com.n4systems.fieldid.datatypes;

public class ReportingSearchCriteria {

	String rfidNumber = null;
	String serialNumber = null;
	String eventTypeGroup = null;
	String inspector = null;
	String orderNumber = null;
	String purchaseOrder = null;
	String jobSite = null;
	String assignedTo = null;
	String customer = null;
	String division = null;
	String inspectionBook = null;
	String referenceNumber = null;
	String productType = null;
	String productStatus = null;
	String job = null;
	String fromDate = null;
	String toDate = null;
	Owner owner = null;
	
	public ReportingSearchCriteria() {
	}
	
	public ReportingSearchCriteria(String serialNumber) {
		this.serialNumber = serialNumber;
	}
	
	public void setOwner(Owner o) {
		owner = o;
		if(owner != null) {
			this.customer = owner.getCustomer();
			this.division = owner.getDivision();
		}
	}
	
	public Owner getOwner() {
		return owner;
	}

	public void setRFIDNumber(String s) {
		this.rfidNumber = s;
	}
	
	public void setSerialNumber(String s) {
		this.serialNumber = s;
	}
	
	public void setEventTypeGroup(String s) {
		this.eventTypeGroup = s;
	}
	
	public void setInspector(String s) {
		this.inspector = s;
	}
	
	public void setOrderNumber(String s) {
		this.orderNumber = s;
	}
	
	public void setPurchaseOrder(String s) {
		this.purchaseOrder = s;
	}
	
	public void setJobSite(String s) {
		this.jobSite = s;
	}
	
	public void setAssignedTo(String s) {
		this.assignedTo = s;
	}
	
	public void setCustomer(String s) {
		this.customer = s;
	}
	
	public void setDivision(String s) {
		this.division = s;
	}
	
	public void setInspectionBook(String s) {
		this.inspectionBook = s;
	}
	
	public void setReferenceNumber(String s) {
		this.referenceNumber = s;
	}
	
	public void setProductType(String s) {
		this.productType = s;
	}
	
	public void setProductStatus(String s) {
		this.productStatus = s;
	}
	
	public void setJob(String s) {
		this.job = s;
	}
	
	public void setFromDate(String s) {
		this.fromDate = s;
	}
	
	public void setToDate(String s) {
		this.toDate = s;
	}

	public String getRFIDNumber() {
		return rfidNumber;
	}
	
	public String getSerialNumber() {
		return serialNumber;
	}
	
	public String getEventTypeGroup() {
		return eventTypeGroup;
	}
	
	public String getInspector() {
		return inspector;
	}
	
	public String getOrderNumber() {
		return orderNumber;
	}
	
	public String getPurchaseOrder() {
		return purchaseOrder;
	}
	
	public String getJobSite() {
		return jobSite;
	}
	
	public String getAssignedTo() {
		return assignedTo;
	}
	
	public String getCustomer() {
		return customer;
	}
	
	public String getDivision() {
		return division;
	}
	
	public String getInspectionBook() {
		return inspectionBook;
	}
	
	public String getReferenceNumber() {
		return referenceNumber;
	}
	
	public String getProductType() {
		return productType;
	}
	
	public String getProductStatus() {
		return productStatus;
	}
	
	public String getJob() {
		return job;
	}
	
	public String getFromDate() {
		return fromDate;
	}
	
	public String getToDate() {
		return toDate;
	}
}
