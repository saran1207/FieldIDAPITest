package com.n4systems.services;

import java.util.List;

import com.n4systems.ejb.InspectionScheduleManager;
import com.n4systems.model.EventSchedule;
import com.n4systems.util.DateHelper;

public class ManagerBackedNextInspectionScheduleService implements NextInspectionScheduleSerivce {

	
	private final InspectionScheduleManager inspectionScheduleManager;
	
	public ManagerBackedNextInspectionScheduleService(InspectionScheduleManager inspectionScheduleManager) {
		this.inspectionScheduleManager = inspectionScheduleManager;
	}
	
	
	
	
	/**
	 * Creates the next inspection schedule for the asset.  If there is already a schedule
	 * for the contained asset and inspection type it will simply return that one.
	 * @return The newly created schedule, or the already existing one.
	 */
	public EventSchedule createNextSchedule(EventSchedule schedule) {
		
		EventSchedule eventSchedule = findExistingSchedule(schedule);
		
		if (eventSchedule == null) {
			eventSchedule = inspectionScheduleManager.update(schedule);
		}
		
		return eventSchedule;
	}


	private EventSchedule findExistingSchedule(EventSchedule newSchedule) {
		List<EventSchedule> upcomingSchedules = inspectionScheduleManager.getAvailableSchedulesFor(newSchedule.getAsset());
		
		for (EventSchedule upcomingSchedule : upcomingSchedules) {
			if (DateHelper.isEqualIgnoringTime(upcomingSchedule.getNextDate(), newSchedule.getNextDate()) 
					&& upcomingSchedule.getEventType().equals(newSchedule.getEventType())) {
				return upcomingSchedule;
			}
		}
		return null;
	}
	
}
