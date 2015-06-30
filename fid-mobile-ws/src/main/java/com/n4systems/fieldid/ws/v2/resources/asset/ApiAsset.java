package com.n4systems.fieldid.ws.v2.resources.asset;

import com.n4systems.fieldid.ws.v2.resources.asset.attributevalues.ApiAttributeValue;
import com.n4systems.fieldid.ws.v2.resources.model.ApiReadWriteModelWithOwner;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public class ApiAsset extends ApiReadWriteModelWithOwner {
	private String identifier;
	private String rfidNumber;
	private String customerRefNumber;
	private String purchaseOrder;
	private String comments;
	private Date identified;
	private Date lastEventDate;
	private Long typeId;
	private Long assetStatusId;
	private Long identifiedById;
	private Long assignedUserId;
	private BigDecimal gpsLatitude;
	private BigDecimal gpsLongitude;
	private Long predefinedLocationId;
	private String freeformLocation;
	private String orderNumber;
	private byte[] image;
	private ApiSubAsset masterAsset;
	private List<ApiAttributeValue> attributeValues;
	private List<ApiSubAsset> subAssets;

	public String getIdentifier() {
		return identifier;
	}

	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}

	public String getRfidNumber() {
		return rfidNumber;
	}

	public void setRfidNumber(String rfidNumber) {
		this.rfidNumber = rfidNumber;
	}

	public String getCustomerRefNumber() {
		return customerRefNumber;
	}

	public void setCustomerRefNumber(String customerRefNumber) {
		this.customerRefNumber = customerRefNumber;
	}

	public String getPurchaseOrder() {
		return purchaseOrder;
	}

	public void setPurchaseOrder(String purchaseOrder) {
		this.purchaseOrder = purchaseOrder;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public Date getIdentified() {
		return identified;
	}

	public void setIdentified(Date identified) {
		this.identified = identified;
	}

	public Date getLastEventDate() {
		return lastEventDate;
	}

	public void setLastEventDate(Date lastEventDate) {
		this.lastEventDate = lastEventDate;
	}

	public Long getTypeId() {
		return typeId;
	}

	public void setTypeId(Long typeId) {
		this.typeId = typeId;
	}

	public Long getAssetStatusId() {
		return assetStatusId;
	}

	public void setAssetStatusId(Long assetStatusId) {
		this.assetStatusId = assetStatusId;
	}

	public Long getIdentifiedById() {
		return identifiedById;
	}

	public void setIdentifiedById(Long identifiedById) {
		this.identifiedById = identifiedById;
	}

	public Long getAssignedUserId() {
		return assignedUserId;
	}

	public void setAssignedUserId(Long assignedUserId) {
		this.assignedUserId = assignedUserId;
	}

	public BigDecimal getGpsLatitude() {
		return gpsLatitude;
	}

	public void setGpsLatitude(BigDecimal gpsLatitude) {
		this.gpsLatitude = gpsLatitude;
	}

	public BigDecimal getGpsLongitude() {
		return gpsLongitude;
	}

	public void setGpsLongitude(BigDecimal gpsLongitude) {
		this.gpsLongitude = gpsLongitude;
	}

	public Long getPredefinedLocationId() {
		return predefinedLocationId;
	}

	public void setPredefinedLocationId(Long predefinedLocationId) {
		this.predefinedLocationId = predefinedLocationId;
	}

	public String getFreeformLocation() {
		return freeformLocation;
	}

	public void setFreeformLocation(String freeformLocation) {
		this.freeformLocation = freeformLocation;
	}

	public String getOrderNumber() {
		return orderNumber;
	}

	public void setOrderNumber(String orderNumber) {
		this.orderNumber = orderNumber;
	}

	public byte[] getImage() {
		return image;
	}

	public void setImage(byte[] image) {
		this.image = image;
	}
	
	public ApiSubAsset getMasterAsset() {
		return masterAsset;
	}
	
	public void setMasterAsset(ApiSubAsset masterAsset) {
		this.masterAsset = masterAsset;
	}
	
	public List<ApiSubAsset> getSubAssets() {
		return subAssets;
	}

	public void setSubAssets(List<ApiSubAsset> subAssets) {
		this.subAssets = subAssets;
	}

	public List<ApiAttributeValue> getAttributeValues() {
		return attributeValues;
	}

	public void setAttributeValues(List<ApiAttributeValue> attributeValues) {
		this.attributeValues = attributeValues;
	}

}
