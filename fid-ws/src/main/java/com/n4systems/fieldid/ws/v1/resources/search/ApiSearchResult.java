package com.n4systems.fieldid.ws.v1.resources.search;

import java.util.Date;

import com.n4systems.fieldid.ws.v1.resources.model.ApiReadWriteModel;

public class ApiSearchResult extends ApiReadWriteModel {
	private String assetTypeName;
	private String identifier;
	private String referenceNumber;
	private String assetStatus;
	private String description;
	private String internalOwnerName;
	private String customerOwnerName;
	private String divisionOwnerName;
	private String location;
	private Long predefinedLocationId;
	private Date nextEventDate;
	private byte[] image;

	public String getAssetTypeName() {
		return assetTypeName;
	}

	public void setAssetTypeName(String assetTypeName) {
		this.assetTypeName = assetTypeName;
	}

	public String getIdentifier() {
		return identifier;
	}

	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}

	public String getReferenceNumber() {
		return referenceNumber;
	}

	public void setReferenceNumber(String referenceNumber) {
		this.referenceNumber = referenceNumber;
	}

	public String getAssetStatus() {
		return assetStatus;
	}

	public void setAssetStatus(String assetStatus) {
		this.assetStatus = assetStatus;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getInternalOwnerName() {
		return internalOwnerName;
	}

	public void setInternalOwnerName(String internalOwnerName) {
		this.internalOwnerName = internalOwnerName;
	}

	public String getCustomerOwnerName() {
		return customerOwnerName;
	}

	public void setCustomerOwnerName(String customerOwnerName) {
		this.customerOwnerName = customerOwnerName;
	}

	public String getDivisionOwnerName() {
		return divisionOwnerName;
	}

	public void setDivisionOwnerName(String divisionOwnerName) {
		this.divisionOwnerName = divisionOwnerName;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}
	
	public Long getPredefinedLocationId() {
		return predefinedLocationId;
	}

	public void setPredefinedLocationId(Long predefinedLocationId) {
		this.predefinedLocationId = predefinedLocationId;
	}

	public Date getNextEventDate() {
		return nextEventDate;
	}

	public void setNextEventDate(Date nextEventDate) {
		this.nextEventDate = nextEventDate;
	}

	public byte[] getImage() {
		return image;
	}

	public void setImage(byte[] image) {
		this.image = image;
	}
}
