package com.n4systems.fieldid.api.mobile.resources.savedEvent;

import com.n4systems.fieldid.api.mobile.resources.event.ApiBaseEvent;
import com.n4systems.fieldid.api.mobile.resources.eventtype.ApiEventForm;

import java.util.List;

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