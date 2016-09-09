package com.n4systems.services;

import com.n4systems.ejb.PersistenceManager;
import com.n4systems.fieldid.CopiedToService;
import com.n4systems.model.ThingEvent;

@CopiedToService(com.n4systems.fieldid.service.event.EventScheduleService.class)
public class EventScheduleServiceImpl implements EventScheduleService {

	private final PersistenceManager persistenceManager;

	public EventScheduleServiceImpl(PersistenceManager persistenceManager) {
		this.persistenceManager = persistenceManager;
	}
	
	@Override
	public Long createSchedule(ThingEvent schedule) {
		Long id = persistenceManager.save(schedule);
		schedule.getAsset().touch();
		persistenceManager.update(schedule.getAsset());
		return id;
	}

    @Override
	public ThingEvent updateSchedule(ThingEvent schedule) {
        ThingEvent event = null;
        if (schedule.getId() == null) {
            persistenceManager.save(schedule);
            event = schedule;
        } else {
            event = persistenceManager.update(schedule);
        }

        schedule.getAsset().touch();
		persistenceManager.update(schedule.getAsset());
		return event;
	}
	
}
