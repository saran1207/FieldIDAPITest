package com.n4systems.fieldid.datatypes;

public class MassUpdateForm {
	
	String customerName = null;
	String division = null;
	String jobSite = null;
	String assignedTo = null;
	String productStatus = null;
	String purchaseOrder = null;
	String location = null;
	String identified = null;
	
	public MassUpdateForm() {
		super();
	}
	
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
	
	public void setDivision(String division) {
		this.division = division;
	}
	
	public void setJobSite(String jobSite) {
		this.jobSite = jobSite;
	}
	
	public void setAssignedTo(String assignedTo) {
		this.assignedTo = assignedTo;
	}
	
	public void setProductStatus(String productStatus) {
		this.productStatus = productStatus;
	}
	
	public void setPurchaseOrder(String purchaseOrder) {
		this.purchaseOrder = purchaseOrder;
	}
	
	public void setLocation(String location) {
		this.location = location;
	}
	
	public void setIdentified(String identified) {
		this.identified = identified;
	}

	public String getCustomerName() {
		return customerName;
	}
	
	public String getDivision() {
		return division;
	}
	
	public String getJobSite() {
		return jobSite;
	}
	
	public String getAssignedTo() {
		return assignedTo;
	}
	
	public String getProductStatus() {
		return productStatus;
	}
	
	public String getPurchaseOrder() {
		return purchaseOrder;
	}
	
	public String getLocation() {
		return location;
	}
	
	public String getIdentified() {
		return identified;
	}
}
