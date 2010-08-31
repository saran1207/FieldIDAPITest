package com.n4systems.webservice.server.handlers;

import java.util.Date;

import com.n4systems.model.Inspection;
import com.n4systems.model.InspectionSchedule;
import com.n4systems.model.Project;
import com.n4systems.model.inspection.InspectionByMobileGuidLoader;
import com.n4systems.model.inspectionschedule.InspectionScheduleSaver;
import com.n4systems.persistence.loaders.FilteredIdLoader;
import com.n4systems.webservice.server.InspectionNotFoundException;

public class CompletedScheduleCreator {
	
	private InspectionByMobileGuidLoader<Inspection> inspectionLoader;
	private InspectionScheduleSaver inspectionScheduleSaver;
	private FilteredIdLoader<Project> jobLoader;
	
	public CompletedScheduleCreator(InspectionByMobileGuidLoader<Inspection> inspectionLoader, InspectionScheduleSaver inspectionScheduleSaver, FilteredIdLoader<Project> projectLoader) {
		this.inspectionLoader = inspectionLoader;
		this.inspectionScheduleSaver = inspectionScheduleSaver;
		this.jobLoader = projectLoader;
	}
	
	public void create(String inspectionMobileGuid, Date scheduledDate, long jobId) {
		InspectionSchedule schedule = new InspectionSchedule();
		schedule.setNextDate(scheduledDate);
		schedule.setProject(findJob(jobId));
		Inspection inspection = findInspection(inspectionMobileGuid); 
		schedule.completed(inspection);
		schedule.setInspectionType(inspection.getType());
		schedule.setProduct(inspection.getProduct());
		schedule.setTenant(inspection.getTenant());
		inspectionScheduleSaver.save(schedule);
	}
	
	private Inspection findInspection(String inspectionMobileGuid) {
		inspectionLoader.setMobileGuid(inspectionMobileGuid);
		Inspection inspection = inspectionLoader.load();
		if (inspection == null) throw new InspectionNotFoundException();
		return inspection;
	}
	
	private Project findJob(long jobId) {
		jobLoader.setId(jobId);
		return jobLoader.load();
	}
		
}
