package com.n4systems.fieldid.service.event;

import com.n4systems.fieldid.service.FieldIdPersistenceService;
import com.n4systems.model.*;
import com.n4systems.model.EventSchedule.ScheduleStatus;
import com.n4systems.model.security.OpenSecurityFilter;
import com.n4systems.util.DateHelper;
import com.n4systems.util.persistence.QueryBuilder;
import com.n4systems.util.persistence.WhereClauseFactory;
import com.n4systems.util.persistence.WhereParameter.Comparator;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class EventScheduleService extends FieldIdPersistenceService {

    private static Logger logger = Logger.getLogger( EventScheduleService.class );

    @Autowired private NotifyEventAssigneeService notifyEventAssigneeService;

	@Transactional(readOnly = true)
	public Event getNextEventSchedule(Long assetId, Long eventTypeId) {
		QueryBuilder<Event> query = createUserSecurityBuilder(Event.class)
				.addOrder("nextDate")
				.addWhere(WhereClauseFactory.create(Comparator.EQ, "eventState", Event.EventState.OPEN))
				.addWhere(WhereClauseFactory.create("asset.id", assetId));

		if (eventTypeId != null) {
			query.addWhere(WhereClauseFactory.create("type.id", eventTypeId));
		}

		List<Event> schedules = persistenceService.findAll(query);
		return (schedules.isEmpty()) ? null : schedules.get(0);
	}

	@Transactional(readOnly = true)
	public List<Event> getIncompleteSchedules(Long assetId) {
		QueryBuilder<Event> query = createUserSecurityBuilder(Event.class)
				.addOrder("nextDate")
                .addWhere(WhereClauseFactory.create(Comparator.EQ, "eventState", Event.EventState.OPEN))
				.addWhere(WhereClauseFactory.create("asset.id", assetId));

		List<Event> schedules = persistenceService.findAll(query);
		return schedules;
	}
	
	@Transactional(readOnly = true)
	public EventSchedule findByMobileId(String mobileId) {
		QueryBuilder<EventSchedule> query = createUserSecurityBuilder(EventSchedule.class);
		query.addWhere(WhereClauseFactory.create("mobileGUID", mobileId));		
		EventSchedule eventSchedule = persistenceService.find(query);
		return eventSchedule;
	}

    @SuppressWarnings("deprecation")
    @Transactional
    public List<Event> autoSchedule(Asset asset) {
        List<Event> schedules = new ArrayList<Event>();

        AssetType assetType = persistenceService.find(AssetType.class, asset.getType().getId());
        if (assetType != null) {
            for (EventType type : assetType.getEventTypes()) {
                AssetTypeSchedule schedule = assetType.getSchedule(type, asset.getOwner());
                if (schedule != null && schedule.isAutoSchedule()) {
                    Event openEvent = new Event();
                    openEvent.setAsset(asset);
                    openEvent.setType(type);
                    openEvent.setNextDate(assetType.getSuggestedNextEventDate(new Date(), type, asset.getOwner()));
                    schedules.add(openEvent);
                    updateSchedule(openEvent);
                }
            }
        }
        logger.info("auto scheduled for asset " + asset);
        return schedules;
    }

    @Transactional
    public List<EventSchedule> getAutoEventSchedules(Asset asset) {
        List<EventSchedule> schedules = new ArrayList<EventSchedule>();

        if(asset.getType() == null) {
            return schedules;
        }

        AssetType assetType = persistenceService.find(AssetType.class, asset.getType().getId());
        if (assetType != null) {
            for (AssociatedEventType type : assetType.getAssociatedEventTypes()) {
                AssetTypeSchedule schedule = assetType.getSchedule(type.getEventType(), asset.getOwner());
                if (schedule != null && schedule.isAutoSchedule()) {
                    EventSchedule eventSchedule = new EventSchedule(asset, type.getEventType());
                    eventSchedule.setNextDate(assetType.getSuggestedNextEventDate(asset.getIdentified(), type.getEventType(), asset.getOwner()));
                    schedules.add(eventSchedule);
                }
            }
        }
        return schedules;
    }

    @Transactional
    public void removeAllSchedulesFor(Asset asset) {
        for (Event openEvent : getAvailableSchedulesFor(asset)) {
            persistenceService.delete(openEvent);
        }
    }

    @Transactional
    public void create(AssetTypeSchedule schedule) {
        persistenceService.save(schedule);
    }

    @Transactional
    public List<Event> getAvailableSchedulesFor(Asset asset) {
        QueryBuilder<Event> query = new QueryBuilder<Event>(Event.class, new OpenSecurityFilter());
        query.addSimpleWhere("asset", asset).addWhere(Comparator.EQ, "eventState", "eventState", Event.EventState.OPEN);
        query.addOrder("nextDate");

        return persistenceService.findAll(query);
    }

    @Transactional
    public List<EventSchedule> getAvailableSchedulesForAssetFilteredByEventType(Asset asset, EventType eventType) {
        QueryBuilder<EventSchedule> query = new QueryBuilder<EventSchedule>(EventSchedule.class, new OpenSecurityFilter());
        query.addSimpleWhere("asset", asset).addWhere(Comparator.NE, "status", "status", ScheduleStatus.COMPLETED);
        query.addSimpleWhere("eventType.id", eventType.getId());
        query.addOrder("nextDate");

        return persistenceService.findAll(query);
    }

    @Transactional
    public boolean schedulePastDue(Long scheduleId) {
        // here we'll select the next date off the schedule and see if it's after today
        QueryBuilder<Date> builder = new QueryBuilder<Date>(EventSchedule.class, new OpenSecurityFilter());
        builder.setSimpleSelect("nextDate");
        builder.addSimpleWhere("id", scheduleId);
        builder.addWhere(Comparator.NE, "status", "status", ScheduleStatus.COMPLETED);

        Date nextDate = persistenceService.find(builder);


        return (nextDate != null) && DateHelper.getToday().after(nextDate);
    }

    @Transactional
    public Long getAssetIdForSchedule(Long scheduleId) {
        QueryBuilder<Long> builder = new QueryBuilder<Long>(EventSchedule.class, new OpenSecurityFilter());
        builder.setSimpleSelect("asset.id");
        builder.addSimpleWhere("id", scheduleId);

        return persistenceService.find(builder);
    }

    @Transactional
    public Long getEventTypeIdForSchedule(Long scheduleId) {
        QueryBuilder<Long> builder = new QueryBuilder<Long>(EventSchedule.class, new OpenSecurityFilter());
        builder.setSimpleSelect("eventType.id");
        builder.addSimpleWhere("id", scheduleId);

        return persistenceService.find(builder);
    }

    @Transactional
    public Long getEventIdForSchedule(Long scheduleId) {
        QueryBuilder<Long> builder = new QueryBuilder<Long>(EventSchedule.class, new OpenSecurityFilter());
        builder.setSimpleSelect("event.id");
        builder.addSimpleWhere("id", scheduleId);

        return persistenceService.find(builder);
    }

    @Transactional
    public Event updateSchedule(Event schedule) {
        if (schedule.getGroup() == null) {
            EventGroup group = new EventGroup();
            persistenceService.save(group);
            schedule.setGroup(group);
        }
        Event updatedSchedule = persistenceService.update(schedule);
        updatedSchedule.getAsset().touch();
        persistenceService.update(updatedSchedule.getAsset());
        return updatedSchedule;
    }

    @Transactional
    public Long createSchedule(Event openEvent) {
        EventGroup eventGroup = new EventGroup();
        openEvent.setOwner(openEvent.getAsset().getOwner());
        openEvent.setGroup(eventGroup);
        persistenceService.save(eventGroup);

        Long id = persistenceService.save(openEvent);
        openEvent.getAsset().touch();
        persistenceService.update(openEvent.getAsset());
        notifyEventAssigneeService.notifyEventAssignee(openEvent);
        return id;
    }

}
