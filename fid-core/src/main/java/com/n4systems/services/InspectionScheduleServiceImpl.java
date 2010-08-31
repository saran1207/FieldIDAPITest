package com.n4systems.services;

import com.n4systems.ejb.PersistenceManager;
import com.n4systems.model.InspectionSchedule;

public class InspectionScheduleServiceImpl implements InspectionScheduleService {

	private final PersistenceManager persistenceManager;

	public InspectionScheduleServiceImpl(PersistenceManager persistenceManager) {
		this.persistenceManager = persistenceManager;
	}
	
	public Long createSchedule(InspectionSchedule schedule) {
		Long id = persistenceManager.save(schedule);
		schedule.getProduct().touch();
		persistenceManager.update(schedule.getProduct());
		return id;
	}
	
	public InspectionSchedule updateSchedule(InspectionSchedule schedule) {
		InspectionSchedule updatedSchedule = persistenceManager.update(schedule);
		updatedSchedule.getProduct().touch();
		persistenceManager.update(updatedSchedule.getProduct());
		return updatedSchedule;
	}
	
}
