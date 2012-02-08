package com.n4systems.fieldid.ws.v1.resources.eventschedule;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.n4systems.fieldid.service.event.EventScheduleService;
import com.n4systems.fieldid.ws.v1.resources.ApiResource;
import com.n4systems.model.EventSchedule;

public class ApiEventScheduleResource extends ApiResource<ApiEventSchedule, EventSchedule> {
	@Autowired private EventScheduleService eventScheduleService;
	
	public List<ApiEventSchedule>  findAllSchedules(Long assetId) {
		List<ApiEventSchedule> apiEventSchedules = new ArrayList<ApiEventSchedule>();		
		List<EventSchedule> schedules = eventScheduleService.getIncompleteSchedules(assetId);
		for (EventSchedule schedule: schedules) {
			apiEventSchedules.add(convertEntityToApiModel(schedule));
		}
		
		return apiEventSchedules;
	}

	@Override
	protected ApiEventSchedule convertEntityToApiModel(EventSchedule schedule) {
		ApiEventSchedule apiSchedule = new ApiEventSchedule();
		apiSchedule.setSid(schedule.getMobileGUID());
		apiSchedule.setActive(true);
		apiSchedule.setModified(schedule.getModified());
		apiSchedule.setOwnerId(schedule.getOwner().getId());
		apiSchedule.setAssetId(schedule.getAsset().getMobileGUID());
		apiSchedule.setEventTypeId(schedule.getEventType().getId());
		apiSchedule.setEventTypeName(schedule.getEventType().getName());
		apiSchedule.setNextDate(schedule.getNextDate());
		return apiSchedule;
	}
}
