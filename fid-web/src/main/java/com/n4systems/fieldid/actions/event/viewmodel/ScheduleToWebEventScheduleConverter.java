package com.n4systems.fieldid.actions.event.viewmodel;

import com.n4systems.fieldid.actions.event.WebEventSchedule;
import com.n4systems.fieldid.actions.helpers.UserDateConverter;
import com.n4systems.model.AssetTypeSchedule;
import com.n4systems.model.Event;
import com.n4systems.model.user.Assignable;
import com.n4systems.model.user.User;
import com.n4systems.model.user.UserGroup;

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
		webEventSchedule.setAssignee(createAssigneeeString(openEvent));
		webEventSchedule.setDate(dateConverter.convertDate(openEvent.getDueDate()));
		
		return webEventSchedule;
	}

    private String createAssigneeeString(Event openEvent) {
        Assignable assignedUserOrGroup = openEvent.getAssignedUserOrGroup();
        if (assignedUserOrGroup instanceof User) {
            return "U" + assignedUserOrGroup.getId();
        } else if (assignedUserOrGroup instanceof UserGroup) {
            return "G" + assignedUserOrGroup.getId();
        }
        return null;
    }
}
