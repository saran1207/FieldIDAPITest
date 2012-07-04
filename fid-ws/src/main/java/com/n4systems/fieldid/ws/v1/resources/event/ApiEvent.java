package com.n4systems.fieldid.ws.v1.resources.event;

import java.math.BigDecimal;
import java.util.List;

import com.n4systems.fieldid.ws.v1.resources.eventattachment.ApiEventAttachment;

public class ApiEvent extends ApiBaseEvent {	
	private BigDecimal gpsLatitude;
	private BigDecimal gpsLongitude;
	private String eventScheduleId;
	private ApiEventFormResult form;
	private List<ApiEventAttachment> attachments;
	private List<ApiEvent> subEvents;
	
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

	public List<ApiEventAttachment> getAttachments() {
		return attachments;
	}
	
	public void setAttachments(List<ApiEventAttachment> attachments) {
		this.attachments = attachments;
	}
	
	public List<ApiEvent> getSubEvents() {
		return subEvents;
	}

	public void setSubEvents(List<ApiEvent> subEvents) {
		this.subEvents = subEvents;
	}
}
