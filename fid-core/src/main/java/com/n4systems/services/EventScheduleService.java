package com.n4systems.services;

import com.n4systems.model.EventSchedule;

public interface EventScheduleService {

	public abstract Long createSchedule(EventSchedule schedule);

	public abstract EventSchedule updateSchedule(EventSchedule schedule);

}