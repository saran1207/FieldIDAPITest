package com.n4systems.services;

import com.n4systems.ejb.PersistenceManager;
import com.n4systems.model.EventSchedule;

public class EventScheduleServiceImpl implements EventScheduleService {

	private final PersistenceManager persistenceManager;

	public EventScheduleServiceImpl(PersistenceManager persistenceManager) {
		this.persistenceManager = persistenceManager;
	}
	
	@Override
	public Long createSchedule(EventSchedule schedule) {
		Long id = persistenceManager.save(schedule);
		schedule.getAsset().touch();
		persistenceManager.update(schedule.getAsset());
		return id;
	}
	
	@Override
	public EventSchedule updateSchedule(EventSchedule schedule) {
		EventSchedule updatedSchedule = persistenceManager.update(schedule);
		updatedSchedule.getAsset().touch();
		persistenceManager.update(updatedSchedule.getAsset());
		return updatedSchedule;
	}
	
}
