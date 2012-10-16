package com.n4systems.services;

import com.n4systems.ejb.EventScheduleManager;
import com.n4systems.fieldid.CopiedToService;
import com.n4systems.model.Event;
import com.n4systems.util.DateHelper;

import java.util.List;

@CopiedToService(com.n4systems.fieldid.service.event.NextEventScheduleService.class)
public class ManagerBackedNextEventScheduleService implements NextEventScheduleSerivce {

	
	private final EventScheduleManager eventScheduleManager;
	
	public ManagerBackedNextEventScheduleService(EventScheduleManager eventScheduleManager) {
		this.eventScheduleManager = eventScheduleManager;
	}
	
	/**
	 * Creates the next event schedule for the asset.  If there is already a schedule
	 * for the contained asset and event type it will simply return that one.
	 * @return The newly created schedule, or the already existing one.
	 */
	public Event createNextSchedule(Event schedule) {
		
		Event eventSchedule = findExistingSchedule(schedule);
		
		if (eventSchedule == null) {
			eventSchedule = eventScheduleManager.update(schedule);
		}
		
		return eventSchedule;
	}


	private Event findExistingSchedule(Event newSchedule) {
		List<Event> upcomingSchedules = eventScheduleManager.getAvailableSchedulesFor(newSchedule.getAsset());
		
		for (Event upcomingSchedule : upcomingSchedules) {
            if (upcomingSchedule.getDueDate() == null) continue;
			if (DateHelper.isEqualIgnoringTime(upcomingSchedule.getDueDate(), newSchedule.getDueDate())
					&& upcomingSchedule.getType().equals(newSchedule.getType())) {
				return upcomingSchedule;
			}
		}
		return null;
	}
	
}
