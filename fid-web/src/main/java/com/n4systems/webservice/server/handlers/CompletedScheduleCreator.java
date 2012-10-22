package com.n4systems.webservice.server.handlers;

import com.n4systems.model.Event;
import com.n4systems.model.Project;
import com.n4systems.model.event.EventByMobileGuidLoader;
import com.n4systems.model.event.SimpleEventSaver;
import com.n4systems.persistence.loaders.FilteredIdLoader;
import com.n4systems.webservice.server.InspectionNotFoundException;

import java.util.Date;

public class CompletedScheduleCreator {
	
	private EventByMobileGuidLoader<Event> eventLoader;
    private SimpleEventSaver eventSaver;
	private FilteredIdLoader<Project> jobLoader;
	
	public CompletedScheduleCreator(EventByMobileGuidLoader<Event> eventLoader, SimpleEventSaver eventSaver, FilteredIdLoader<Project> projectLoader) {
		this.eventLoader = eventLoader;
		this.eventSaver = eventSaver;
		this.jobLoader = projectLoader;
	}

    public void create(String inspectionMobileGuid, Date scheduledDate, long jobId) {
        Event event = findInspection(inspectionMobileGuid);

        event.setProject(findJob(jobId));
        event.setDueDate(scheduledDate);

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
