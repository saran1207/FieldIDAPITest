package com.n4systems.fieldid.selenium.datatypes;

public class AssetSearchDisplayColumns {
	
	private boolean serialNumber;
	
	private boolean referenceNumber;
	
	private boolean rfidNumber;
	
	private boolean jobSiteName;
	
	private boolean division;
	
	private boolean location;
	
	private boolean organization;
	
	private boolean productTypeGroup;
	
	private boolean productType;
	
	private boolean productStatus;
	
	private boolean dateIdentified;
	
	private boolean lastInspectionDate;
	
	private boolean networkLastInspectionDate;
	
	private boolean assignedTo;
	
	private boolean identifiedBy;
	
	private boolean modifiedBy;
	
	private boolean comments;
	
	private boolean description;
	
	private boolean safetyNetwork;
	
	private boolean orderDescription;
	
	private boolean orderNumber;
	
	private boolean purchaseOrder;

	public boolean isSerialNumber() {
		return serialNumber;
	}

	public void setSerialNumber(boolean serialNumber) {
		this.serialNumber = serialNumber;
	}

	public boolean isReferenceNumber() {
		return referenceNumber;
	}

	public void setReferenceNumber(boolean referenceNumber) {
		this.referenceNumber = referenceNumber;
	}

	public boolean isRfidNumber() {
		return rfidNumber;
	}

	public void setRfidNumber(boolean rfidNumber) {
		this.rfidNumber = rfidNumber;
	}

	public boolean isJobSiteName() {
		return jobSiteName;
	}

	public void setJobSiteName(boolean jobSiteName) {
		this.jobSiteName = jobSiteName;
	}

	public boolean isDivision() {
		return division;
	}

	public void setDivision(boolean division) {
		this.division = division;
	}

	public boolean isLocation() {
		return location;
	}

	public void setLocation(boolean location) {
		this.location = location;
	}

	public boolean isOrganization() {
		return organization;
	}

	public void setOrganization(boolean organization) {
		this.organization = organization;
	}

	public boolean isProductTypeGroup() {
		return productTypeGroup;
	}

	public void setProductTypeGroup(boolean productTypeGroup) {
		this.productTypeGroup = productTypeGroup;
	}

	public boolean isProductType() {
		return productType;
	}

	public void setProductType(boolean productType) {
		this.productType = productType;
	}

	public boolean isProductStatus() {
		return productStatus;
	}

	public void setProductStatus(boolean productStatus) {
		this.productStatus = productStatus;
	}

	public boolean isDateIdentified() {
		return dateIdentified;
	}

	public void setDateIdentified(boolean dateIdentified) {
		this.dateIdentified = dateIdentified;
	}

	public boolean isLastInspectionDate() {
		return lastInspectionDate;
	}

	public void setLastInspectionDate(boolean lastInspectionDate) {
		this.lastInspectionDate = lastInspectionDate;
	}

	public boolean isNetworkLastInspectionDate() {
		return networkLastInspectionDate;
	}

	public void setNetworkLastInspectionDate(boolean networkLastInspectionDate) {
		this.networkLastInspectionDate = networkLastInspectionDate;
	}

	public boolean isAssignedTo() {
		return assignedTo;
	}

	public void setAssignedTo(boolean assignedTo) {
		this.assignedTo = assignedTo;
	}

	public boolean isIdentifiedBy() {
		return identifiedBy;
	}

	public void setIdentifiedBy(boolean identifiedBy) {
		this.identifiedBy = identifiedBy;
	}

	public boolean isModifiedBy() {
		return modifiedBy;
	}

	public void setModifiedBy(boolean modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	public boolean isComments() {
		return comments;
	}

	public void setComments(boolean comments) {
		this.comments = comments;
	}

	public boolean isDescription() {
		return description;
	}

	public void setDescription(boolean description) {
		this.description = description;
	}

	public boolean isSafetyNetwork() {
		return safetyNetwork;
	}

	public void setSafetyNetwork(boolean safetyNetwork) {
		this.safetyNetwork = safetyNetwork;
	}

	public boolean isOrderDescription() {
		return orderDescription;
	}

	public void setOrderDescription(boolean orderDescription) {
		this.orderDescription = orderDescription;
	}

	public boolean isOrderNumber() {
		return orderNumber;
	}

	public void setOrderNumber(boolean orderNumber) {
		this.orderNumber = orderNumber;
	}

	public boolean isPurchaseOrder() {
		return purchaseOrder;
	}

	public void setPurchaseOrder(boolean purchaseOrder) {
		this.purchaseOrder = purchaseOrder;
	}

	public void selectAllColumns() {
		serialNumber = true;	
		referenceNumber = true;	
		rfidNumber = true;	
		jobSiteName = true;	
		division = true;	
		location = true;	
		organization = true;	
		productTypeGroup = true;	
		productType = true;	
		productStatus = true;	
		dateIdentified = true;	
		lastInspectionDate = true;	
		networkLastInspectionDate = true;	
		assignedTo = true;	
		identifiedBy = true;	
		modifiedBy = true;	
		comments = true;	
		description = true;	
		safetyNetwork = true;	
		orderDescription = true;	
		orderNumber = true;	
		purchaseOrder = true;
	}
}
