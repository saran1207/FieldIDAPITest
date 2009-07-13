package com.n4systems.fieldid.datatypes;

public class Product {

	String serialNumber = null;
	String rfidNumber = null;
	String purchaseOrder = null;
	String jobSite = null;			// If tenant has job sites, this can be set
	String assignedTo = null;		// If tenant has job sites, this can be set
	String customer = null;			// If tenant doesn't have job sites, this can be set
	String division = null;			// If tenant doesn't have job sites, this can be set
	String location = null;
	String productStatus = null;
	String referenceNumber = null;
	String identified = null;		// Must be in the format MM/DD/YY
	String orderNumber = null;		// If tenant doesn't have integration, this can be set
	String productType = null;
	String comments = null;
	String commentTemplate = null;
	
	/**
	 * Must have a serial number and identified date.
	 *
	 * @param serialNumber
	 * @param identified
	 */
	public Product(String serialNumber, String identified) {
		this.serialNumber = serialNumber;
		this.identified = identified;
	}

	/**
	 * Use this if you are going to add the serial number
	 * later or if you want to the add product to fail.
	 * 
	 * @param identified
	 */
	public Product(String identified) {
		this.identified = identified;
	}
	
	public void setSerialNumber(String serialNumber) {
		this.serialNumber = serialNumber;
	}
	
	public String getSerialNumber() {
		return this.serialNumber;
	}
	
	public void setRFIDNumber(String rfidNumber) {
		this.rfidNumber = rfidNumber;
	}
	
	public String getRFIDNumber() {
		return this.rfidNumber;
	}
	
	public void setPurchaseOrder(String purchaseOrder) {
		this.purchaseOrder = purchaseOrder;
	}
	
	public String getPurchaseOrder() {
		return this.purchaseOrder;
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
	
	public void setLocation(String location) {
		this.location = location;
	}
	
	public String getLocation() {
		return this.location;
	}
	
	public void setProductStatus(String productStatus) {
		this.productStatus = productStatus;
	}
	
	public String getProductStatus() {
		return this.productStatus;
	}
	
	public void setReferenceNumber(String referenceNumber) {
		this.referenceNumber = referenceNumber;
	}
	
	public String getReferenceNumber() {
		return this.referenceNumber;
	}
	
	public void setIdentified(String identified) {
		this.identified = identified;
	}
	
	public String getIdentified() {
		return this.identified;
	}
	
	public void setOrderNumber(String orderNumber) {
		this.orderNumber = orderNumber;
	}
	
	public String getOrderNumber() {
		return this.orderNumber;
	}
	
	public void setProductType(String productType) {
		this.productType = productType;
	}
	
	public String getProductType() {
		return this.productType;
	}
	
	public void setComments(String comments) {
		this.comments = comments;
	}
	
	public String getComments() {
		return this.comments;
	}
	
	public void setCommentTemplate(String commentTemplate) {
		this.commentTemplate = commentTemplate;
	}
	
	public String getCommentTemplate() {
		return this.commentTemplate;
	}
}
