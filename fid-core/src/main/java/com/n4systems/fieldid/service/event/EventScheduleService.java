package com.n4systems.fieldid.service.event;

import com.n4systems.fieldid.service.FieldIdPersistenceService;
import com.n4systems.model.Asset;
import com.n4systems.model.AssetTypeSchedule;
import com.n4systems.model.Event;
import com.n4systems.model.security.OpenSecurityFilter;
import com.n4systems.util.persistence.QueryBuilder;
import com.n4systems.util.persistence.WhereClauseFactory;
import com.n4systems.util.persistence.WhereParameter.Comparator;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public class EventScheduleService extends FieldIdPersistenceService {

    private static Logger logger = Logger.getLogger( EventScheduleService.class );

    @Autowired private NotifyEventAssigneeService notifyEventAssigneeService;

	@Transactional(readOnly = true)
	public Event getNextEventSchedule(Long assetId, Long eventTypeId) {
		QueryBuilder<Event> query = createUserSecurityBuilder(Event.class)
				.addOrder("dueDate")
				.addWhere(WhereClauseFactory.create(Comparator.EQ, "workflowState", Event.WorkflowState.OPEN))
				.addWhere(WhereClauseFactory.create("asset.id", assetId));

		if (eventTypeId != null) {
			query.addWhere(WhereClauseFactory.create("type.id", eventTypeId));
		}

		List<Event> schedules = persistenceService.findAll(query);
		return (schedules.isEmpty()) ? null : schedules.get(0);
	}

	@Transactional(readOnly = true)
	public Event findByMobileId(String mobileId) {
		QueryBuilder<Event> query = createUserSecurityBuilder(Event.class);
		query.addWhere(WhereClauseFactory.create("mobileGUID", mobileId));
        Event eventSchedule = persistenceService.find(query);
		return eventSchedule;
	}

//    @SuppressWarnings("deprecation")
//    @Transactional
//    public List<Event> autoSchedule(Asset asset) {
//        List<Event> schedules = new ArrayList<Event>();
//
//        AssetType assetType = persistenceService.find(AssetType.class, asset.getType().getId());
//        if (assetType != null) {
//            for (EventType type : assetType.getEventTypes()) {
//                AssetTypeSchedule schedule = assetType.getSchedule(type, asset.getOwner());
//                if (schedule != null && schedule.isAutoSchedule()) {
//                    Event openEvent = new Event();
//                    openEvent.setAsset(asset);
//                    openEvent.setType(type);
//                    openEvent.setDueDate(assetType.getSuggestedNextEventDate(new Date(), type, asset.getOwner()));
//                    schedules.add(openEvent);
//                    updateSchedule(openEvent);
//                }
//            }
//        }
//        logger.info("auto scheduled for asset " + asset);
//        return schedules;
//    }


    @Transactional
    public void create(AssetTypeSchedule schedule) {
        persistenceService.save(schedule);
    }

    @Transactional
    public List<Event> getAvailableSchedulesFor(Asset asset) {
        QueryBuilder<Event> query = new QueryBuilder<Event>(Event.class, new OpenSecurityFilter());
        query.addSimpleWhere("asset", asset).addWhere(Comparator.EQ, "workflowState", "workflowState", Event.WorkflowState.OPEN);
        query.addOrder("dueDate");

        return persistenceService.findAll(query);
    }

    @Transactional
    public Event updateSchedule(Event schedule) {
        Event updatedSchedule = persistenceService.update(schedule);
        updatedSchedule.getAsset().touch();
        persistenceService.update(updatedSchedule.getAsset());
        return updatedSchedule;
    }

    @Transactional
    public Long createSchedule(Event openEvent) {
        openEvent.setOwner(openEvent.getAsset().getOwner());
        Long id = persistenceService.save(openEvent);
        openEvent.getAsset().touch();
        persistenceService.update(openEvent.getAsset());
        return id;
    }

}
