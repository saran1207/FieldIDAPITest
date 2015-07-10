package com.n4systems.fieldid.ws.v2.resources.event;

import com.n4systems.fieldid.ws.v2.resources.model.ApiReadWriteModelWithOwner;
import com.n4systems.model.WorkflowState;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public class ApiEvent extends ApiReadWriteModelWithOwner {
	private WorkflowState workflowState;
	private Long typeId;
	private String assetId;
	private Date date;
	private Date dueDate;
	private String status;
	private Long eventStatusId;
	private Long assignedUserId;
	private Long assignedUserGroupId;
	private Long performedById;
	private Long modifiedById;
	private String eventBookId;
	private Long assetStatusId;
	private Long predefinedLocationId;
	private String freeformLocation;
	private boolean printable;
	private String comments;
	private List<ApiEventAttribute> attributes;
	private BigDecimal gpsLatitude;
	private BigDecimal gpsLongitude;
	private String eventScheduleId;
	private ApiEventFormResult form;
	private List<ApiEvent> subEvents;
	private boolean action;
	private Long priorityId;
	private String notes;
	private ApiTriggerEvent triggerEventInfo;

	public WorkflowState getWorkflowState() {
		return workflowState;
	}

	public void setWorkflowState(WorkflowState workflowState) {
		this.workflowState = workflowState;
	}

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

	public Date getDueDate() {
		return dueDate;
	}

	public void setDueDate(Date dueDate) {
		this.dueDate = dueDate;
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

	public Long getAssignedUserGroupId() {
		return assignedUserGroupId;
	}

	public void setAssignedUserGroupId(Long assignedUserGroupId) {
		this.assignedUserGroupId = assignedUserGroupId;
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

	public String getEventScheduleId() {
		return eventScheduleId;
	}

	public void setEventScheduleId(String eventScheduleId) {
		this.eventScheduleId = eventScheduleId;
	}

	public ApiEventFormResult getForm() {
		return form;
	}

	public void setForm(ApiEventFormResult form) {
		this.form = form;
	}
	
	public List<ApiEvent> getSubEvents() {
		return subEvents;
	}

	public void setSubEvents(List<ApiEvent> subEvents) {
		this.subEvents = subEvents;
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
