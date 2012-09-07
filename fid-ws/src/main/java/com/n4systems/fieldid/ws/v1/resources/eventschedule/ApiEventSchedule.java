package com.n4systems.fieldid.ws.v1.resources.eventschedule;

import java.util.Date;
import java.util.List;

import com.n4systems.fieldid.ws.v1.resources.model.ApiReadWriteModelWithOwner;

public class ApiEventSchedule extends ApiReadWriteModelWithOwner {
	private String assetId;	
	private Long eventTypeId;	
	private String eventTypeName;
	private Date nextDate;
	private Long assigneeUserId;
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
