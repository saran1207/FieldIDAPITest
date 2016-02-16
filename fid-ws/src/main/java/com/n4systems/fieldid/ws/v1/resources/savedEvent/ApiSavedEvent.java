package com.n4systems.fieldid.ws.v1.resources.savedEvent;

import com.n4systems.fieldid.ws.v1.resources.event.ApiBaseThingEvent;
import com.n4systems.fieldid.ws.v1.resources.eventtype.ApiEventForm;

import java.util.List;

public class ApiSavedEvent extends ApiBaseThingEvent {
	private ApiEventForm form;
	private List<ApiSavedSubEvent> subEvents;
	
	public List<ApiSavedSubEvent> getSubEvents() {
		return subEvents;
	}

	public void setSubEvents(List<ApiSavedSubEvent> subEvents) {
		this.subEvents = subEvents;
	}	

	public ApiEventForm getForm() {
		return form;
	}
	
	public void setForm(ApiEventForm form) {
		this.form = form;
	}
}