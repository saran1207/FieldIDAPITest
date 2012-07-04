package com.n4systems.fieldid.ws.v1.resources.savedEvent;

import java.util.List;

import com.n4systems.fieldid.ws.v1.resources.event.ApiBaseEvent;

public class ApiSavedEvent extends ApiBaseEvent{
	private ApiSavedEventForm form;
	private List<ApiSavedEvent> subEvents;
	
	public List<ApiSavedEvent> getSubEvents() {
		return subEvents;
	}

	public void setSubEvents(List<ApiSavedEvent> subEvents) {
		this.subEvents = subEvents;
	}	

	public ApiSavedEventForm getForm() {
		return form;
	}
	
	public void setForm(ApiSavedEventForm form) {
		this.form = form;
	}
}
