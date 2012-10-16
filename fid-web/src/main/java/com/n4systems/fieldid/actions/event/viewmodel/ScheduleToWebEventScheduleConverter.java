package com.n4systems.fieldid.actions.event.viewmodel;

import com.n4systems.fieldid.actions.event.WebEventSchedule;
import com.n4systems.fieldid.actions.helpers.UserDateConverter;
import com.n4systems.model.AssetTypeSchedule;
import com.n4systems.model.Event;

import java.util.Date;

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
	
	public WebEventSchedule convert(Event openEvent) {
		if (openEvent == null) { return null; }
		
		WebEventSchedule webEventSchedule = new WebEventSchedule();
		
		webEventSchedule.setType(openEvent.getType().getId());
		webEventSchedule.setTypeName(openEvent.getType().getName());
		webEventSchedule.setAssignee(openEvent.getAssignee()!=null ? openEvent.getAssignee().getId() : null);
		webEventSchedule.setDate(dateConverter.convertDate(openEvent.getDueDate()));
		
		return webEventSchedule;
	}	
}
