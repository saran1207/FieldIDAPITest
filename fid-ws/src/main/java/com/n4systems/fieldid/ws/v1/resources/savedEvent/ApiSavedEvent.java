package com.n4systems.fieldid.ws.v1.resources.savedEvent;

import java.util.List;

import com.n4systems.fieldid.ws.v1.resources.event.ApiBaseEvent;
import com.n4systems.fieldid.ws.v1.resources.eventtype.ApiEventForm;

public class ApiSavedEvent extends ApiBaseEvent{
	private ApiEventForm form;
	private List<ApiSavedEvent> subEvents;
	
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
}