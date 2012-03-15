package com.n4systems.ejb.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;

import com.n4systems.model.Asset;
import com.n4systems.model.AssetType;
import com.n4systems.model.AssetTypeSchedule;
import com.n4systems.model.AssociatedEventType;
import com.n4systems.model.EventSchedule;
import com.n4systems.model.EventType;
import com.n4systems.services.EventScheduleService;
import com.n4systems.services.EventScheduleServiceImpl;
import org.apache.log4j.Logger;

import com.n4systems.ejb.EventScheduleManager;
import com.n4systems.ejb.PersistenceManager;
import com.n4systems.model.Event;
import com.n4systems.model.EventSchedule.ScheduleStatus;
import com.n4systems.model.security.OpenSecurityFilter;
import com.n4systems.util.DateHelper;
import com.n4systems.util.persistence.QueryBuilder;
import com.n4systems.util.persistence.WhereParameter.Comparator;

 
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

	@SuppressWarnings("deprecation")
	public List<EventSchedule> autoSchedule(Asset asset) {
		List<EventSchedule> schedules = new ArrayList<EventSchedule>();
		
		AssetType assetType = persistenceManager.find(AssetType.class, asset.getType().getId());
		if (assetType != null) {
			for (EventType type : assetType.getEventTypes()) {
				AssetTypeSchedule schedule = assetType.getSchedule(type, asset.getOwner());
				if (schedule != null && schedule.isAutoSchedule()) {
					EventSchedule eventSchedule = new EventSchedule(asset, type);
					eventSchedule.setNextDate(assetType.getSuggestedNextEventDate(new Date(), type, asset.getOwner()));
					schedules.add(eventSchedule);
					update(eventSchedule);
				}
			}
		}
		logger.info("auto scheduled for asset " + asset);
		return schedules;
	}
	
	
	public List<EventSchedule> getAutoEventSchedules(Asset asset) {
		List<EventSchedule> schedules = new ArrayList<EventSchedule>();
		
		if(asset.getType() == null) {
			return schedules;
		}
		
		AssetType assetType = persistenceManager.find(AssetType.class, asset.getType().getId());
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
	
	
	public EventSchedule update(EventSchedule schedule) {
		return eventScheduleService.updateSchedule(schedule);
	}
	
	public void restoreScheduleForEvent(Event event) {
		EventSchedule schedule = event.getSchedule();

		if (schedule != null && schedule.getNextDate() != null) {
            EventSchedule newSchedule = EventSchedule.createPlaceholderFor(event);
            newSchedule.setRetired(true);
            persistenceManager.save(newSchedule);

			schedule.removeEvent();
			update(schedule);

            event.setSchedule(newSchedule);
            persistenceManager.update(event);
		}
	}
	
	public void removeAllSchedulesFor(Asset asset) {
		for (EventSchedule schedule : getAvailableSchedulesFor(asset)) {
			persistenceManager.delete(schedule);
		}
	}

    public void create(AssetTypeSchedule schedule) {
		persistenceManager.save(schedule);
	}
	
	public List<EventSchedule> getAvailableSchedulesFor(Asset asset) {
		QueryBuilder<EventSchedule> query = new QueryBuilder<EventSchedule>(EventSchedule.class, new OpenSecurityFilter());
		query.addSimpleWhere("asset", asset).addWhere(Comparator.NE, "status", "status", ScheduleStatus.COMPLETED);
		query.addOrder("nextDate");
		
		return persistenceManager.findAll(query);
	}

	public List<EventSchedule> getAvailableSchedulesForAssetFilteredByEventType(Asset asset, EventType eventType) {
		QueryBuilder<EventSchedule> query = new QueryBuilder<EventSchedule>(EventSchedule.class, new OpenSecurityFilter());
		query.addSimpleWhere("asset", asset).addWhere(Comparator.NE, "status", "status", ScheduleStatus.COMPLETED);
		query.addSimpleWhere("eventType.id", eventType.getId());
		query.addOrder("nextDate");
		
		return persistenceManager.findAll(query);
	}
	
	public boolean schedulePastDue(Long scheduleId) {
		// here we'll select the next date off the schedule and see if it's after today
		QueryBuilder<Date> builder = new QueryBuilder<Date>(EventSchedule.class, new OpenSecurityFilter());
		builder.setSimpleSelect("nextDate");
		builder.addSimpleWhere("id", scheduleId);
		builder.addWhere(Comparator.NE, "status", "status", ScheduleStatus.COMPLETED);
		
		Date nextDate = persistenceManager.find(builder);
		
		
		return (nextDate != null) && DateHelper.getToday().after(nextDate);
	}
	
	public Long getAssetIdForSchedule(Long scheduleId) {
		QueryBuilder<Long> builder = new QueryBuilder<Long>(EventSchedule.class, new OpenSecurityFilter());
		builder.setSimpleSelect("asset.id");
		builder.addSimpleWhere("id", scheduleId);
		
		return persistenceManager.find(builder);
	}
	
	public Long getEventTypeIdForSchedule(Long scheduleId) {
		QueryBuilder<Long> builder = new QueryBuilder<Long>(EventSchedule.class, new OpenSecurityFilter());
		builder.setSimpleSelect("eventType.id");
		builder.addSimpleWhere("id", scheduleId);
		
		return persistenceManager.find(builder);
	}

	public Long getEventIdForSchedule(Long scheduleId) {
		QueryBuilder<Long> builder = new QueryBuilder<Long>(EventSchedule.class, new OpenSecurityFilter());
		builder.setSimpleSelect("event.id");
		builder.addSimpleWhere("id", scheduleId);
		
		return persistenceManager.find(builder);
	}

	
}
