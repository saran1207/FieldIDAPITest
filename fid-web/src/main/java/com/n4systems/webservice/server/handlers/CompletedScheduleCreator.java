package com.n4systems.webservice.server.handlers;

import java.util.Date;

import com.n4systems.ejb.impl.EventSaver;
import com.n4systems.model.Event;
import com.n4systems.model.EventSchedule;
import com.n4systems.model.Project;
import com.n4systems.model.event.EventByMobileGuidLoader;
import com.n4systems.model.event.SimpleEventSaver;
import com.n4systems.model.eventschedule.EventScheduleSaver;
import com.n4systems.persistence.loaders.FilteredIdLoader;
import com.n4systems.webservice.server.InspectionNotFoundException;

public class CompletedScheduleCreator {
	
	private EventByMobileGuidLoader<Event> eventLoader;
	private EventScheduleSaver eventScheduleSaver;
    private SimpleEventSaver eventSaver;
	private FilteredIdLoader<Project> jobLoader;
	
	public CompletedScheduleCreator(EventByMobileGuidLoader<Event> eventLoader, EventScheduleSaver eventScheduleSaver, SimpleEventSaver eventSaver, FilteredIdLoader<Project> projectLoader) {
		this.eventLoader = eventLoader;
		this.eventScheduleSaver = eventScheduleSaver;
        this.eventSaver = eventSaver;
		this.jobLoader = projectLoader;
	}

    public void create(String inspectionMobileGuid, Date scheduledDate, long jobId) {
        Event event = findInspection(inspectionMobileGuid);

        EventSchedule schedule = event.getSchedule();
        schedule.setProject(findJob(jobId));
        schedule.setNextDate(scheduledDate);

        event.setProject(findJob(jobId));
        event.setNextDate(scheduledDate);

        eventScheduleSaver.update(schedule);
        eventSaver.update(event);
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
