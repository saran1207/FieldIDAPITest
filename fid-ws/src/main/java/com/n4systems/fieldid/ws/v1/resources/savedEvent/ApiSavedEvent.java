package com.n4systems.fieldid.ws.v1.resources.savedEvent;

import com.n4systems.fieldid.ws.v1.resources.event.ApiBaseThingEvent;
import com.n4systems.fieldid.ws.v1.resources.eventtype.ApiEventForm;

import java.util.List;

public class ApiSavedEvent extends ApiBaseThingEvent {
	private ApiEventForm form;
	private List<ApiSavedEvent> subEvents;
	private String masterEventSid;

	public List<ApiSavedEvent> getSubEvents() {
		return subEvents;
	}

	public void setSubEvents(List<ApiSavedEvent> subEvents) {
		this.subEvents = subEvents;
	}	

	public ApiEventForm getForm() {
		return form;
	}
	
	public void setForm(ApiEventForm form) {
		this.form = form;
	}

	public String getMasterEventSid() {
		return masterEventSid;
	}

	public void setMasterEventSid(String masterEventSid) {
		this.masterEventSid = masterEventSid;
	}
}