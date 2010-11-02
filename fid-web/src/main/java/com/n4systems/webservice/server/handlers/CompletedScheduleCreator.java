package com.n4systems.webservice.server.handlers;

import java.util.Date;

import com.n4systems.model.Event;
import com.n4systems.model.EventSchedule;
import com.n4systems.model.Project;
import com.n4systems.model.inspection.EventByMobileGuidLoader;
import com.n4systems.model.inspectionschedule.EventScheduleSaver;
import com.n4systems.persistence.loaders.FilteredIdLoader;
import com.n4systems.webservice.server.InspectionNotFoundException;

public class CompletedScheduleCreator {
	
	private EventByMobileGuidLoader<Event> eventLoader;
	private EventScheduleSaver eventScheduleSaver;
	private FilteredIdLoader<Project> jobLoader;
	
	public CompletedScheduleCreator(EventByMobileGuidLoader<Event> eventLoader, EventScheduleSaver eventScheduleSaver, FilteredIdLoader<Project> projectLoader) {
		this.eventLoader = eventLoader;
		this.eventScheduleSaver = eventScheduleSaver;
		this.jobLoader = projectLoader;
	}
	
	public void create(String inspectionMobileGuid, Date scheduledDate, long jobId) {
		EventSchedule schedule = new EventSchedule();
		schedule.setNextDate(scheduledDate);
		schedule.setProject(findJob(jobId));
		Event event = findInspection(inspectionMobileGuid);
		schedule.completed(event);
		schedule.setEventType(event.getType());
		schedule.setAsset(event.getAsset());
		schedule.setTenant(event.getTenant());
		eventScheduleSaver.save(schedule);
	}
	
	private Event findInspection(String inspectionMobileGuid) {
		eventLoader.setMobileGuid(inspectionMobileGuid);
		Event event = eventLoader.load();
		if (event == null) throw new InspectionNotFoundException();
		return event;
	}
	
	private Project findJob(long jobId) {
		jobLoader.setId(jobId);
		return jobLoader.load();
	}
		
}
