package com.n4systems.fieldid.api.mobile.resources.eventschedule;

import com.n4systems.fieldid.api.mobile.resources.model.ApiReadWriteModelWithOwner;

import java.util.Date;

public class ApiEventSchedule extends ApiReadWriteModelWithOwner {
	private String assetId;
	private String assetIdentifier;	
	private Long eventTypeId;	
	private String eventTypeName;
	private String owner;
	private Date nextDate;
	private Long assigneeUserId;
	private Long assigneeUserGroupId;
	private boolean action;
	private Long priorityId;	
	private String notes;
	private ApiTriggerEvent triggerEventInfo;

	public String getAssetId() {
		return assetId;
	}

	public void setAssetId(String assetId) {
		this.assetId = assetId;
	}
	
	public String getAssetIdentifier() {
		return assetIdentifier;
	}

	public void setAssetIdentifier(String assetIdentifier) {
		this.assetIdentifier = assetIdentifier;
	}

	public Long getEventTypeId() {
		return eventTypeId;
	}

	public void setEventTypeId(Long eventTypeId) {
		this.eventTypeId = eventTypeId;
	}
	
	public String getEventTypeName() {
		return eventTypeName;
	}

	public void setEventTypeName(String eventTypeName) {
		this.eventTypeName = eventTypeName;
	}
	
	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public Date getNextDate() {
		return nextDate;
	}

	public void setNextDate(Date nextDate) {
		this.nextDate = nextDate;
	}
	
	public Long getAssigneeUserId() {
		return assigneeUserId;
	}

	public void setAssigneeUserId(Long assigneeUserId) {
		this.assigneeUserId = assigneeUserId;
	}
	
	public Long getAssigneeUserGroupId() {
		return assigneeUserGroupId;
	}
	
	public void setAssigneeUserGroupId(Long assigneeUserGroupId) {
		this.assigneeUserGroupId = assigneeUserGroupId;
	}	

	public boolean isAction() {
		return action;
	}

	public void setAction(boolean action) {
		this.action = action;
	}
	
	public Long getPriorityId() {
		return priorityId;
	}

	public void setPriorityId(Long priorityId) {
		this.priorityId = priorityId;
	}

	public String getNotes() {
		return notes;
	}
	
	public void setNotes(String notes) {
		this.notes = notes;
	}
	
	public ApiTriggerEvent getTriggerEventInfo() {
		return triggerEventInfo;
	}
	
	public void setTriggerEventInfo(ApiTriggerEvent triggerEventInfo) {
		this.triggerEventInfo = triggerEventInfo;
	}		
}
