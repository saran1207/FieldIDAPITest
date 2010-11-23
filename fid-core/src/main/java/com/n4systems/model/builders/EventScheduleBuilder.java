package com.n4systems.model.builders;

import static com.n4systems.model.builders.EventBuilder.anEvent;
import static com.n4systems.model.builders.EventTypeBuilder.anEventType;
import static com.n4systems.model.builders.AssetBuilder.anAsset;

import java.util.Date;

import com.n4systems.exceptions.ProcessFailureException;
import com.n4systems.model.Event;
import com.n4systems.model.EventSchedule;
import com.n4systems.model.EventType;
import com.n4systems.model.Asset;
import com.n4systems.model.Project;

public class EventScheduleBuilder extends BaseBuilder<EventSchedule> {

	private final Asset asset;
	private final EventType eventType;
	private final Date nextDate;
	private final Event event;
	private final Project job;
	
	public static EventScheduleBuilder aScheduledEventSchedule() {
		return new EventScheduleBuilder(anAsset().build(), anEventType().build(),new Date(), null, null);
	}
	
	public static EventScheduleBuilder aCompletedEventSchedule() {
		EventType eventType = anEventType().build();
		Asset asset = anAsset().build();
		Event event = anEvent().ofType(eventType).on(asset).build();
		return new EventScheduleBuilder(asset, eventType,new Date(), event, null);
	}

	public EventScheduleBuilder(Asset asset, EventType eventType, Date nextDate, Event event, Project job) {
		this.asset = asset;
		this.eventType = eventType;
		this.nextDate = nextDate;
		this.event = event;
		this.job = job;
	}
	
	public EventScheduleBuilder asset(Asset asset) {
		return new EventScheduleBuilder(asset, eventType, nextDate, event, job);
	}
	
	public EventScheduleBuilder eventType(EventType eventType) {
		return new EventScheduleBuilder(asset, eventType, nextDate, event, job);
	}
	
	public EventScheduleBuilder nextDate(Date nextDate) {
		return new EventScheduleBuilder(asset, eventType, nextDate, event, job);
	}
	
	public EventScheduleBuilder completedDoing(Event event) {
		return new EventScheduleBuilder(asset, eventType, nextDate, event, job);
	}
	
	public EventScheduleBuilder forJob(Project job) {
		return new EventScheduleBuilder(asset, eventType, nextDate, event, job);
	}
	
	@Override
	public EventSchedule createObject() {
		EventSchedule eventSchedule = new EventSchedule(asset, eventType);
		eventSchedule.setNextDate(nextDate);
		eventSchedule.setId(getId());
		eventSchedule.setProject(job);
		
		if (event != null) {
			eventSchedule.completed(event);
			try {
				injectField(event, "schedule", eventSchedule);
			} catch (Exception e) {
				throw new ProcessFailureException("couldn't inject schedule", e);
			}
		}
		return eventSchedule;
	}
	
}
