package com.n4systems.fieldid.service.event;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.n4systems.fieldid.service.FieldIdPersistenceService;
import com.n4systems.model.EventSchedule;
import com.n4systems.model.EventSchedule.ScheduleStatus;
import com.n4systems.util.persistence.QueryBuilder;
import com.n4systems.util.persistence.WhereClauseFactory;
import com.n4systems.util.persistence.WhereParameter.Comparator;

public class EventScheduleService extends FieldIdPersistenceService {

	@Transactional(readOnly = true)
	public EventSchedule getNextEventSchedule(Long assetId, Long eventTypeId) {
		QueryBuilder<EventSchedule> query = createUserSecurityBuilder(EventSchedule.class)
				.addOrder("nextDate")
				.addWhere(WhereClauseFactory.create(Comparator.NE, "status", ScheduleStatus.COMPLETED))
				.addWhere(WhereClauseFactory.create("asset.id", assetId));

		if (eventTypeId != null) {
			query.addWhere(WhereClauseFactory.create("eventType.id", eventTypeId));
		}

		List<EventSchedule> schedules = persistenceService.findAll(query);
		return (schedules.isEmpty()) ? null : schedules.get(0);
	}

	@Transactional(readOnly = true)
	public List<EventSchedule> getIncompleteSchedules(Long assetId) {
		QueryBuilder<EventSchedule> query = createUserSecurityBuilder(EventSchedule.class)
				.addOrder("nextDate")
				.addWhere(WhereClauseFactory.create(Comparator.NE, "status", ScheduleStatus.COMPLETED))
				.addWhere(WhereClauseFactory.create("asset.id", assetId));

		List<EventSchedule> schedules = persistenceService.findAll(query);
		return schedules;
	}
	
	@Transactional(readOnly = true)
	public EventSchedule findByMobileId(String mobileId) {
		QueryBuilder<EventSchedule> query = createUserSecurityBuilder(EventSchedule.class);
		query.addWhere(WhereClauseFactory.create("mobileGUID", mobileId));		
		EventSchedule eventSchedule = persistenceService.find(query);
		return eventSchedule;
	}
	
}
