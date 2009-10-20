package com.n4systems.fieldid.datatypes;

public class Product {

	String serialNumber = null;
	String rfidNumber = null;
	String purchaseOrder = null;
	String jobSite = null;			// If tenant has job sites, this can be set
	String assignedTo = null;		// If tenant has job sites, this can be set
	String customer = null;			// If tenant doesn't have job sites, this can be set
	String division = null;			// If tenant doesn't have job sites, this can be set
	Owner owner = null;				// As of 2009.7 the concept of customer/division or jobsite no longer exist. use this instead
	String location = null;
	String productStatus = null;
	String referenceNumber = null;
	String identified = null;		// Must be in the format MM/DD/YY
	String orderNumber = null;		// If tenant doesn't have integration, this can be set
	String productType = null;
	String comments = null;
	String commentTemplate = null;
	boolean published = false;
	
	public String toString() {
		StringBuffer s = new StringBuffer(serialNumber);
		s.append(",");	s.append((rfidNumber == null) ? "" : rfidNumber);
		s.append(",");	s.append(published ? "Published" : "Not Published");
		s.append(",");	s.append((purchaseOrder == null) ? "" : purchaseOrder);
		s.append(",");	s.append(owner);
		s.append(",");	s.append((location == null) ? "" : location);
		s.append(",");	s.append((productStatus == null) ? "" : productStatus);
		s.append(",");	s.append((referenceNumber == null) ? "" : referenceNumber);
		s.append(",");	s.append((identified == null) ? "" : identified);
		s.append(",");	s.append((orderNumber == null) ? "" : orderNumber);
		s.append(",");	s.append((productType == null) ? "" : productType);
		s.append(",");	s.append((comments == null) ? "" : comments);
		
		return s.toString();
	}
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
	
	public void setOwner(Owner owner) {
		this.owner = owner;
	}
	
	public Owner getOwner() {
		return this.owner;
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
		if(owner != null) {
			owner.setCustomer(customer);
		}
	}
	
	public String getCustomer() {
		if(owner != null) {
			return owner.getCustomer();
		}
		return this.customer;
	}
	
	public void setDivision(String division) {
		this.division = division;
		if(owner != null) {
			owner.setDivision(division);
		}
	}
	
	public String getDivision() {
		if(owner != null) {
			return owner.getDivision();
		}
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
	
	// if string contains Not, e.g. Not Published, set false
	// other wise all input will be true.
	public void setPublished(String s) {
		published = s.contains("Not") ? false : true;
	}
	
	public void setPublished(boolean b) {
		published = b;
	}
	
	public boolean getPublished() {
		return this.published;
	}
}
