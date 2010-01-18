package com.n4systems.fieldid.selenium.datatypes;

import java.util.ArrayList;
import java.util.List;

public class Product {
	String serialNumber;
	String rfidNumber;
	String referenceNumber;
	SafetyNetworkRegistration registration;
	boolean published = false;
	Owner owner;
	String location;
	String productStatus;
	String purchaseOrder;
	String identified;
	String productType;
	/**
	 * TODO:
	 * Add support for attributes
	 * 	text fields
	 *	select lists
	 *	combo boxes
	 *	units of measure
	 */
	String comments;
	List<Attachment> attachments = new ArrayList<Attachment>();
	
	public void setSerialNumber(String s) {
		this.serialNumber = s;
	}
	
	public void setRFIDNumber(String s) {
		this.rfidNumber = s;
	}
	
	public void setReferenceNumber(String s) {
		this.referenceNumber = s;
	}

	public void setSafetyNetworkRegistration(SafetyNetworkRegistration registration) {
		this.registration = registration;
	}

	public void setPublished(boolean b) {
		this.published = b;
	}
	
	public void setOwner(Owner owner) {
		this.owner = owner;
	}

	public void setLocation(String location) {
		this.location = location;
	}
	
	public void setProductStatus(String s) {
		this.productStatus = s;
	}

	public void setPurchaseOrder(String s) {
		this.purchaseOrder = s;
	}
	
	public void setIdentified(String s) {
		this.identified = s;
	}

	public void setProductType(String s) {
		this.productType = s;
	}
	
	public void setComments(String s) {
		this.comments = s;
	}

	public void addAttachment(Attachment a) {
		attachments.add(a);
	}

	public String getSerialNumber() {
		return serialNumber;
	}
	
	public String getRFIDNumber() {
		return rfidNumber;
	}
	
	public String getReferenceNumber() {
		return referenceNumber;
	}

	public SafetyNetworkRegistration getSafetyNetworkRegistration() {
		return registration;
	}

	public boolean getPublished() {
		return published;
	}
	
	public Owner getOwner() {
		return owner;
	}

	public String getLocation() {
		return location;
	}
	
	public String getProductStatus() {
		return productStatus;
	}

	public String getPurchaseOrder() {
		return purchaseOrder;
	}
	
	public String getIdentified() {
		return identified;
	}

	public String getProductType() {
		return productType;
	}
	
	public String getComments() {
		return comments;
	}

	public List<Attachment> getAttachments() {
		return attachments;
	}
}
