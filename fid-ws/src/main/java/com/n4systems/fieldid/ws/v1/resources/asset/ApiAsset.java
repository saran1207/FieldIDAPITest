package com.n4systems.fieldid.ws.v1.resources.asset;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.n4systems.fieldid.ws.v1.resources.assetattachment.ApiAssetAttachment;
import com.n4systems.fieldid.ws.v1.resources.assettype.attributevalues.ApiAttributeValue;
import com.n4systems.fieldid.ws.v1.resources.event.ApiEvent;
import com.n4systems.fieldid.ws.v1.resources.eventhistory.ApiEventHistory;
import com.n4systems.fieldid.ws.v1.resources.eventschedule.ApiEventSchedule;
import com.n4systems.fieldid.ws.v1.resources.model.ApiReadWriteModelWithOwner;

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
	private String nonIntergrationOrderNumber;
	private byte[] image;
	private ApiSubAsset masterAsset;
	private List<ApiSubAsset> subAssets;
	private List<ApiAttributeValue> attributeValues;
	private List<ApiEventSchedule> schedules;
	private List<ApiAssetAttachment> attachments;
	private List<ApiEventHistory> eventHistory;
	private List<ApiEvent> events;

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

	public String getNonIntergrationOrderNumber() {
		return nonIntergrationOrderNumber;
	}

	public void setNonIntergrationOrderNumber(String nonIntergrationOrderNumber) {
		this.nonIntergrationOrderNumber = nonIntergrationOrderNumber;
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

	public List<ApiEventSchedule> getSchedules() {
		return schedules;
	}

	public void setSchedules(List<ApiEventSchedule> schedules) {
		this.schedules = schedules;
	}	

	public List<ApiAssetAttachment> getAttachments() {
		return attachments;
	}
	
	public void setAttachments(List<ApiAssetAttachment> attachments) {
		this.attachments = attachments;
	}
	
	public List<ApiEventHistory> getEventHistory() {
		return eventHistory;
	}

	public void setEventHistory(List<ApiEventHistory> eventHistory) {
		this.eventHistory = eventHistory;
	}
	
	public List<ApiEvent> getEvents() {
		return events;
	}

	public void setEvents(List<ApiEvent> events) {
		this.events = events;
	}	
}
