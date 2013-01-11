package com.n4systems.fieldid.ws.v1.resources.eventschedule;

import java.util.List;

public class ApiMultiEventSchedule {
	private ApiEventSchedule eventScheduleTemplate;
	private List<ApiEventScheduleAssetLink> eventSchedules;

	public ApiEventSchedule getEventScheduleTemplate() {
		return eventScheduleTemplate;
	}

	public void setEventScheduleTemplate(ApiEventSchedule eventScheduleTemplate) {
		this.eventScheduleTemplate = eventScheduleTemplate;
	}

	public List<ApiEventScheduleAssetLink> getEventSchedules() {
		return eventSchedules;
	}

	public void setEventSchedules(List<ApiEventScheduleAssetLink> eventSchedules) {
		this.eventSchedules = eventSchedules;
	}
}
