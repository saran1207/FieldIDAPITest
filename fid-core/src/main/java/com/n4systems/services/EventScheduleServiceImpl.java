package com.n4systems.services;

import com.n4systems.ejb.PersistenceManager;
import com.n4systems.fieldid.CopiedToService;
import com.n4systems.model.Asset;
import com.n4systems.model.Event;
import com.n4systems.model.EventGroup;
import com.n4systems.model.EventSchedule;
import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.model.user.User;

@CopiedToService(com.n4systems.fieldid.service.event.EventScheduleService.class)
public class EventScheduleServiceImpl implements EventScheduleService {

	private final PersistenceManager persistenceManager;

	public EventScheduleServiceImpl(PersistenceManager persistenceManager) {
		this.persistenceManager = persistenceManager;
	}
	
	@Override
	public Long createSchedule(Event schedule) {
        EventGroup eventGroup = new EventGroup();
        schedule.setOwner(schedule.getAsset().getOwner());
        schedule.setGroup(eventGroup);
        persistenceManager.save(eventGroup);

		Long id = persistenceManager.save(schedule);
		schedule.getAsset().touch();
		persistenceManager.update(schedule.getAsset());
		return id;
	}

    @Override
	public Event updateSchedule(Event schedule) {
        Event event = null;
        if (schedule.getId() == null) {
            persistenceManager.save(schedule.getGroup());
            persistenceManager.save(schedule);
            event = schedule;
        } else {
            event = persistenceManager.update(schedule);
        }

        event.getAsset().touch();
		persistenceManager.update(event.getAsset());
		return event;
	}
	
}
