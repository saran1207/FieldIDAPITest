package com.n4systems.fieldid.ws.v1.resources.event;

import java.util.Date;
import java.util.List;

import com.n4systems.fieldid.ws.v1.resources.model.ApiReadWriteModelWithOwner;

public class ApiBaseEvent extends ApiReadWriteModelWithOwner{
	private Long typeId;
	private String assetId;
	private Date date;
	private String status;
	private Long eventStatusId;
	private Long assignedUserId;
	private Long performedById;
	private Long modifiedById;
	private String eventBookId;
	private Long assetStatusId;
	private Long predefinedLocationId;
	private String freeformLocation;
	private boolean printable;
	private String comments;
	private List<ApiEventAttribute> attributes;
	
	public Long getTypeId() {
		return typeId;
	}

	public void setTypeId(Long typeId) {
		this.typeId = typeId;
	}

	public String getAssetId() {
		return assetId;
	}

	public void setAssetId(String assetId) {
		this.assetId = assetId;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Long getAssignedUserId() {
		return assignedUserId;
	}

	public void setAssignedUserId(Long assignedUserId) {
		this.assignedUserId = assignedUserId;
	}

	public Long getPerformedById() {
		return performedById;
	}

	public void setPerformedById(Long performedById) {
		this.performedById = performedById;
	}

	public String getEventBookId() {
		return eventBookId;
	}

	public void setEventBookId(String eventBookId) {
		this.eventBookId = eventBookId;
	}

	public Long getAssetStatusId() {
		return assetStatusId;
	}

	public void setAssetStatusId(Long assetStatusId) {
		this.assetStatusId = assetStatusId;
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

	public boolean isPrintable() {
		return printable;
	}

	public void setPrintable(boolean printable) {
		this.printable = printable;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}
	
	public Long getModifiedById() {
		return modifiedById;
	}

	public void setModifiedById(Long modifiedById) {
		this.modifiedById = modifiedById;
	}
	
	public Long getEventStatusId() {
		return eventStatusId;
	}

	public void setEventStatusId(Long eventStatusId) {
		this.eventStatusId = eventStatusId;
	}
	
	public List<ApiEventAttribute> getAttributes() {
		return attributes;
	}
	
	public void setAttributes(List<ApiEventAttribute> attributes) {
		this.attributes = attributes;
	}
}
