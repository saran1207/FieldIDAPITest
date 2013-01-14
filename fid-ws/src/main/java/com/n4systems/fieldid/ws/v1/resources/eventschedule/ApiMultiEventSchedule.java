package com.n4systems.fieldid.ws.v1.resources.eventschedule;

import com.n4systems.fieldid.ws.v1.resources.asset.ApiAssetLink;

import java.util.List;

public class ApiMultiEventSchedule {
	private ApiEventSchedule eventScheduleTemplate;
	private List<ApiAssetLink> eventSchedules;

	public ApiEventSchedule getEventScheduleTemplate() {
		return eventScheduleTemplate;
	}

	public void setEventScheduleTemplate(ApiEventSchedule eventScheduleTemplate) {
		this.eventScheduleTemplate = eventScheduleTemplate;
	}

	public List<ApiAssetLink> getEventSchedules() {
		return eventSchedules;
	}

	public void setEventSchedules(List<ApiAssetLink> eventSchedules) {
		this.eventSchedules = eventSchedules;
	}
}
