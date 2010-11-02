package com.n4systems.webservice.server.handlers;

import java.util.Date;

import com.n4systems.model.Event;
import com.n4systems.model.EventSchedule;
import com.n4systems.model.Project;
import com.n4systems.model.inspection.InspectionByMobileGuidLoader;
import com.n4systems.model.inspectionschedule.InspectionScheduleSaver;
import com.n4systems.persistence.loaders.FilteredIdLoader;
import com.n4systems.webservice.server.InspectionNotFoundException;

public class CompletedScheduleCreator {
	
	private InspectionByMobileGuidLoader<Event> inspectionLoader;
	private InspectionScheduleSaver inspectionScheduleSaver;
	private FilteredIdLoader<Project> jobLoader;
	
	public CompletedScheduleCreator(InspectionByMobileGuidLoader<Event> inspectionLoader, InspectionScheduleSaver inspectionScheduleSaver, FilteredIdLoader<Project> projectLoader) {
		this.inspectionLoader = inspectionLoader;
		this.inspectionScheduleSaver = inspectionScheduleSaver;
		this.jobLoader = projectLoader;
	}
	
	public void create(String inspectionMobileGuid, Date scheduledDate, long jobId) {
		EventSchedule schedule = new EventSchedule();
		schedule.setNextDate(scheduledDate);
		schedule.setProject(findJob(jobId));
		Event event = findInspection(inspectionMobileGuid);
		schedule.completed(event);
		schedule.setInspectionType(event.getType());
		schedule.setAsset(event.getAsset());
		schedule.setTenant(event.getTenant());
		inspectionScheduleSaver.save(schedule);
	}
	
	private Event findInspection(String inspectionMobileGuid) {
		inspectionLoader.setMobileGuid(inspectionMobileGuid);
		Event event = inspectionLoader.load();
		if (event == null) throw new InspectionNotFoundException();
		return event;
	}
	
	private Project findJob(long jobId) {
		jobLoader.setId(jobId);
		return jobLoader.load();
	}
		
}
