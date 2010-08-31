package com.n4systems.services;

import java.util.List;

import com.n4systems.ejb.InspectionScheduleManager;
import com.n4systems.model.InspectionSchedule;
import com.n4systems.util.DateHelper;

public class ManagerBackedNextInspectionScheduleService implements NextInspectionScheduleSerivce {

	
	private final InspectionScheduleManager inspectionScheduleManager;
	
	public ManagerBackedNextInspectionScheduleService(InspectionScheduleManager inspectionScheduleManager) {
		this.inspectionScheduleManager = inspectionScheduleManager;
	}
	
	
	
	
	/**
	 * Creates the next inspection schedule for the product.  If there is already a schedule
	 * for the contained product and inspection type it will simply return that one.
	 * @return The newly created schedule, or the already existing one.
	 */
	public InspectionSchedule createNextSchedule(InspectionSchedule schedule) {
		
		InspectionSchedule inspectionSchedule = findExistingSchedule(schedule);
		
		if (inspectionSchedule == null) {
			inspectionSchedule = inspectionScheduleManager.update(schedule);
		}
		
		return inspectionSchedule;
	}


	private InspectionSchedule findExistingSchedule(InspectionSchedule newSchedule) {
		List<InspectionSchedule> upcomingSchedules = inspectionScheduleManager.getAvailableSchedulesFor(newSchedule.getProduct());
		
		for (InspectionSchedule upcomingSchedule : upcomingSchedules) {
			if (DateHelper.isEqualIgnoringTime(upcomingSchedule.getNextDate(), newSchedule.getNextDate()) 
					&& upcomingSchedule.getInspectionType().equals(newSchedule.getInspectionType())) {
				return upcomingSchedule;
			}
		}
		return null;
	}
	
}
