package com.n4systems.services;

import com.n4systems.model.Event;

public interface EventScheduleService {

	public abstract Long createSchedule(Event schedule);

	public abstract Event updateSchedule(Event schedule);

}