package com.n4systems.fieldid.actions.event.viewmodel;

import java.util.Date;

import com.n4systems.fieldid.actions.helpers.UserDateConverter;
import com.n4systems.fieldid.actions.event.WebEventSchedule;
import com.n4systems.model.AssetTypeSchedule;
import com.n4systems.model.EventSchedule;

public class ScheduleToWebEventScheduleConverter {
	
	
	private final UserDateConverter dateConverter;

	public ScheduleToWebEventScheduleConverter(UserDateConverter dateConverter) {
		this.dateConverter = dateConverter;
		
	}

	public WebEventSchedule convert(AssetTypeSchedule schedule, Date currentDatePerformed) {
		if (schedule == null) { return null; }
		
		
		WebEventSchedule webEventSchedule = new WebEventSchedule();
		
		webEventSchedule.setType(schedule.getEventType().getId());
		webEventSchedule.setTypeName(schedule.getEventType().getName());
		
		
		webEventSchedule.setDate(dateConverter.convertDate(schedule.getNextDate(currentDatePerformed)));
		
		return webEventSchedule;
	}
	
	public WebEventSchedule convert(EventSchedule schedule) {
		if (schedule == null) { return null; }
		
		
		WebEventSchedule webEventSchedule = new WebEventSchedule();
		
		webEventSchedule.setType(schedule.getEventType().getId());
		webEventSchedule.setTypeName(schedule.getEventType().getName());
		
		webEventSchedule.setDate(dateConverter.convertDate(schedule.getNextDate()));
		
		return webEventSchedule;
	}	
}
