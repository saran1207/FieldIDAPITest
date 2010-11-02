package com.n4systems.model.builders;

import static com.n4systems.model.builders.EventBuilder.anEvent;
import static com.n4systems.model.builders.InspectionTypeBuilder.anInspectionType;
import static com.n4systems.model.builders.AssetBuilder.anAsset;

import java.util.Date;

import com.n4systems.exceptions.ProcessFailureException;
import com.n4systems.model.Event;
import com.n4systems.model.EventSchedule;
import com.n4systems.model.InspectionType;
import com.n4systems.model.Asset;
import com.n4systems.model.Project;

public class InspectionScheduleBuilder extends BaseBuilder<EventSchedule> {

	private final Asset asset;
	private final InspectionType inspectionType;
	private final Date nextDate;
	private final Event event;
	private final Project job;
	
	public static InspectionScheduleBuilder aScheduledInspectionSchedule() {
		return new InspectionScheduleBuilder(anAsset().build(),anInspectionType().build(),new Date(), null, null);
	}
	
	public static InspectionScheduleBuilder aCompletedInspectionSchedule() {
		InspectionType inspectionType = anInspectionType().build();
		Asset asset = anAsset().build();
		Event event = anEvent().ofType(inspectionType).on(asset).build();
		return new InspectionScheduleBuilder(asset,inspectionType,new Date(), event, null);
	}

	public InspectionScheduleBuilder(Asset asset, InspectionType inspectionType, Date nextDate, Event event, Project job) {
		this.asset = asset;
		this.inspectionType = inspectionType;
		this.nextDate = nextDate;
		this.event = event;
		this.job = job;
	}
	
	public InspectionScheduleBuilder asset(Asset asset) {
		return new InspectionScheduleBuilder(asset, inspectionType, nextDate, event, job);
	}
	
	public InspectionScheduleBuilder inspectionType(InspectionType inspectionType) {
		return new InspectionScheduleBuilder(asset, inspectionType, nextDate, event, job);
	}
	
	public InspectionScheduleBuilder nextDate(Date nextDate) {
		return new InspectionScheduleBuilder(asset, inspectionType, nextDate, event, job);
	}
	
	public InspectionScheduleBuilder completedDoing(Event event) {
		return new InspectionScheduleBuilder(asset, inspectionType, nextDate, event, job);
	}
	
	public InspectionScheduleBuilder forJob(Project job) {
		return new InspectionScheduleBuilder(asset, inspectionType, nextDate, event, job);
	}
	
	@Override
	public EventSchedule createObject() {
		EventSchedule eventSchedule = new EventSchedule(asset, inspectionType);
		eventSchedule.setNextDate(nextDate);
		eventSchedule.setId(id);
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
