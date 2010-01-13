package com.n4systems.fieldid.selenium.datatypes;

public class ProductSelectDisplayColumns {
	boolean serialNumber = true;
	boolean rfidNumber = false;
	boolean customerName = true;
	boolean division = false;
	boolean organization = false;
	boolean referenceNumber = true;
	boolean productTypeGroup = false;
	boolean productType = true;
	boolean productStatus = true;
	boolean lastInspectionDate = true;
	boolean networkLastInspectionDate = false;
	boolean dateIdentified = false;
	boolean identifiedBy = false;
	boolean modifiedBy = false;
	boolean comments = false;
	boolean description = false;
	boolean location = false;
	boolean safetyNetwork = false;
	boolean orderDescription = true;
	boolean orderNumber = false;
	boolean purchaseOrder = false;
	
	public String toString() {
		StringBuffer s = new StringBuffer();
		
		s.append("Product Information\n");
		s.append("-------------------\n");
		s.append("               Serial Number: ").append(serialNumber).append("\n");
		s.append("                 RFID Number: ").append(rfidNumber).append("\n");
		s.append("               Customer Name: ").append(customerName).append("\n");
		s.append("                    Division: ").append(division).append("\n");
		s.append("                Organization: ").append(organization).append("\n");
		s.append("            Reference Number: ").append(referenceNumber).append("\n");
		s.append("          Product Type Group: ").append(productTypeGroup).append("\n");
		s.append("                Product Type: ").append(productType).append("\n");
		s.append("              Product Status: ").append(productStatus).append("\n");
		s.append("        Last Inspection Date: ").append(lastInspectionDate).append("\n");
		s.append("Network Last Inspection Date: ").append(networkLastInspectionDate).append("\n");
		s.append("             Date Identified: ").append(dateIdentified).append("\n");
		s.append("               Identified By: ").append(identifiedBy).append("\n");
		s.append("                 Modified By: ").append(modifiedBy).append("\n");
		s.append("                    Comments: ").append(comments).append("\n");
		s.append("                 Description: ").append(description).append("\n");
		s.append("                    Location: ").append(location).append("\n");
		s.append("              Safety Network: ").append(safetyNetwork).append("\n");
		s.append("Order Information\n");
		s.append("-----------------\n");
		s.append("           Order Description: ").append(orderDescription).append("\n");
		s.append("                Order Number: ").append(orderNumber).append("\n");
		s.append("              Purchase Order: ").append(purchaseOrder).append("\n");
		
		return s.toString();
	}

	public void setSerialNumber(boolean b) {
		serialNumber = b;
	}
	
	public void setrfidNumber(boolean b) {
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
	
	public void setLastInspectionDate(boolean b) {
		lastInspectionDate = b;
	}
	
	public void setNetworkLastInspectionDate(boolean b) {
		networkLastInspectionDate = b;
	}
	
	public void setDateIdentified(boolean b) {
		dateIdentified = b;
	}
	
	public void setIdentifiedBy(boolean b) {
		identifiedBy = b;
	}
	
	public void setModifiedBy(boolean b) {
		modifiedBy = b;
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
	
	public void setSafetyNetwork(boolean b) {
		safetyNetwork = b;
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
	
	public boolean getLastInspectionDate() {
		return lastInspectionDate;
	}
	
	public boolean getNetworkLastInspectionDate() {
		return networkLastInspectionDate;
	}
	
	public boolean getDateIdentified() {
		return dateIdentified;
	}
	
	public boolean getIdentifiedBy() {
		return identifiedBy;
	}
	
	public boolean getModifiedBy() {
		return modifiedBy;
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
	
	public boolean getSafetyNetwork() {
		return safetyNetwork;
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
		serialNumber = b;
		rfidNumber = b;
		customerName = b;
		division = b;
		organization = b;
		referenceNumber = b;
		productTypeGroup = b;
		productType = b;
		productStatus = b;
		lastInspectionDate = b;
		networkLastInspectionDate = b;
		dateIdentified = b;
		identifiedBy = b;
		modifiedBy = b;
		comments = b;
		description = b;
		location = b;
		safetyNetwork = b;
		orderDescription = b;
		orderNumber = b;
		purchaseOrder = b;
	}
}
