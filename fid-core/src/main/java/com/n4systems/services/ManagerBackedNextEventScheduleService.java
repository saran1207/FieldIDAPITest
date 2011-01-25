package com.n4systems.services;

import java.util.List;

import com.n4systems.ejb.EventScheduleManager;
import com.n4systems.model.EventSchedule;
import com.n4systems.model.EventSchedule.ScheduleStatus;
import com.n4systems.util.DateHelper;

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
	public EventSchedule createNextSchedule(EventSchedule schedule) {
		
		EventSchedule eventSchedule = findExistingSchedule(schedule);
		
		if (eventSchedule == null) {
			eventSchedule = eventScheduleManager.update(schedule);
		}
		
		return eventSchedule;
	}


	private EventSchedule findExistingSchedule(EventSchedule newSchedule) {
		List<EventSchedule> upcomingSchedules = eventScheduleManager.getAvailableSchedulesFor(newSchedule.getAsset());
		
		for (EventSchedule upcomingSchedule : upcomingSchedules) {
			if (DateHelper.isEqualIgnoringTime(upcomingSchedule.getNextDate(), newSchedule.getNextDate()) 
					&& upcomingSchedule.getEventType().equals(newSchedule.getEventType()) 
					&& !upcomingSchedule.getStatus().equals(ScheduleStatus.IN_PROGRESS)) {
				return upcomingSchedule;
			}
		}
		return null;
	}
	
}
