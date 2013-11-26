package com.n4systems.ejb.impl;

import com.n4systems.ejb.EventScheduleManager;
import com.n4systems.ejb.PersistenceManager;
import com.n4systems.fieldid.CopiedToService;
import com.n4systems.fieldid.service.event.EventService;
import com.n4systems.model.*;
import com.n4systems.model.security.OpenSecurityFilter;
import com.n4systems.services.EventScheduleService;
import com.n4systems.services.EventScheduleServiceImpl;
import com.n4systems.util.DateHelper;
import com.n4systems.util.persistence.QueryBuilder;
import com.n4systems.util.persistence.WhereParameter.Comparator;
import org.apache.log4j.Logger;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@CopiedToService(com.n4systems.fieldid.service.event.EventScheduleService.class)
@Deprecated
public class EventScheduleManagerImpl implements EventScheduleManager {

	private static Logger logger = Logger.getLogger( EventScheduleManagerImpl.class );
	
	private PersistenceManager persistenceManager;
	
	private EventScheduleService eventScheduleService;
	
	protected EntityManager em;

	public EventScheduleManagerImpl(EntityManager em) {
		this.em = em;
		persistenceManager = new PersistenceManagerImpl(em);
		eventScheduleService = new EventScheduleServiceImpl(persistenceManager);
	}

    @Deprecated
    @CopiedToService(EventService.class)
	public List<ThingEvent> autoSchedule(Asset asset) {
		List<ThingEvent> schedules = new ArrayList<ThingEvent>();
		
		AssetType assetType = persistenceManager.find(AssetType.class, asset.getType().getId());
		if (assetType != null) {
			for (ThingEventType type : assetType.getEventTypes()) {
				AssetTypeSchedule schedule = assetType.getSchedule(type, asset.getOwner());
				if (schedule != null && schedule.isAutoSchedule()) {
                    ThingEvent event = new ThingEvent();
                    event.setAsset(asset);
                    event.setType(type);
					event.setDueDate(assetType.getSuggestedNextEventDate(new Date(), type, asset.getOwner()));
					schedules.add(event);
					update(event);
				}
			}
		}
		logger.info("auto scheduled for asset " + asset);
		return schedules;
	}
	
	
	public List<ThingEvent> getAutoEventSchedules(Asset asset) {
		List<ThingEvent> schedules = new ArrayList<ThingEvent>();
		
		if (asset.getType() == null) {
			return schedules;
		}
		
		AssetType assetType = persistenceManager.find(AssetType.class, asset.getType().getId());
		if (assetType != null) {
			for (AssociatedEventType type : assetType.getAssociatedEventTypes()) {
				AssetTypeSchedule schedule = assetType.getSchedule(type.getEventType(), asset.getOwner());
				if (schedule != null && schedule.isAutoSchedule()) {
                    ThingEvent openEvent = new ThingEvent();
                    openEvent.setOwner(asset.getOwner());
                    openEvent.setTenant(asset.getTenant());
                    openEvent.setAsset(asset);
                    openEvent.setType(type.getEventType());
                    openEvent.setDueDate(assetType.getSuggestedNextEventDate(asset.getIdentified(), type.getEventType(), asset.getOwner()));
					schedules.add(openEvent);
				}
			}
		}
		return schedules;
	}

    public ThingEvent reattach(ThingEvent event) {
        return persistenceManager.reattach(event);
    }

	public ThingEvent update(ThingEvent event) {
		return eventScheduleService.updateSchedule(event);
	}
	
	public void restoreScheduleForEvent(ThingEvent event) {
		if (event.wasScheduled()) {
            ThingEvent restoredOpenEvent = new ThingEvent();
            restoredOpenEvent.copyDataFrom(event);
            persistenceManager.save(restoredOpenEvent);

			update(event);

            persistenceManager.update(event);
		}
	}
	
	public void removeAllSchedulesFor(Asset asset) {
		for (Event openEvent : getAvailableSchedulesFor(asset)) {
			persistenceManager.delete(openEvent);
		}
	}

    public void create(AssetTypeSchedule schedule) {
		persistenceManager.save(schedule);
	}

    public List<ThingEvent> getAvailableSchedulesFor(Asset asset, String... postFetchFields) {
        QueryBuilder<ThingEvent> query = new QueryBuilder<ThingEvent>(ThingEvent.class, new OpenSecurityFilter());
        query.addSimpleWhere("asset", asset).addWhere(Comparator.EQ, "workflowState", "workflowState", WorkflowState.OPEN);
        query.addPostFetchPaths(postFetchFields);
        query.addOrder("dueDate");

        return persistenceManager.findAll(query);
    }

    public List<ThingEvent> getAvailableSchedulesForAssetFilteredByEventType(Asset asset, ThingEventType eventType) {
		QueryBuilder<ThingEvent> query = new QueryBuilder<ThingEvent>(ThingEvent.class, new OpenSecurityFilter());
		query.addSimpleWhere("asset", asset).addWhere(Comparator.EQ, "workflowState", "workflowState", WorkflowState.OPEN);
		query.addSimpleWhere("type.id", eventType.getId());
		query.addOrder("dueDate");
		
		return persistenceManager.findAll(query);
	}
	
	public boolean schedulePastDue(Long scheduleId) {
		// here we'll select the next date off the schedule and see if it's after today
		QueryBuilder<Date> builder = new QueryBuilder<Date>(Event.class, new OpenSecurityFilter());
		builder.setSimpleSelect("dueDate");
		builder.addSimpleWhere("id", scheduleId);
        builder.addWhere(Comparator.EQ, "workflowState", "workflowState", WorkflowState.OPEN);

		Date dueDate = persistenceManager.find(builder);
		
		
		return (dueDate != null) && DateHelper.getToday().after(dueDate);
	}
	
	public Long getAssetIdForSchedule(Long scheduleId) {
		QueryBuilder<Long> builder = new QueryBuilder<Long>(Event.class, new OpenSecurityFilter());
		builder.setSimpleSelect("asset.id");
		builder.addSimpleWhere("id", scheduleId);
		return persistenceManager.find(builder);
	}
	
	public Long getEventTypeIdForSchedule(Long scheduleId) {
		QueryBuilder<Long> builder = new QueryBuilder<Long>(Event.class, new OpenSecurityFilter());
		builder.setSimpleSelect("type.id");
        builder.addSimpleWhere("workflowState", WorkflowState.OPEN);
        builder.addSimpleWhere("id", scheduleId);
		
		return persistenceManager.find(builder);
	}

	public Long getEventIdForSchedule(Long scheduleId) {
		return scheduleId;
	}

	
}
