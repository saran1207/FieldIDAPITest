package com.n4systems.services;

import com.n4systems.model.InspectionSchedule;

public interface InspectionScheduleService {

	public abstract Long createSchedule(InspectionSchedule schedule);

	public abstract InspectionSchedule updateSchedule(InspectionSchedule schedule);

}