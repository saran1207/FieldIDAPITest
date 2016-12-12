package com.n4systems.services;

import com.n4systems.model.ThingEvent;

public interface EventScheduleService {

	public abstract Long createSchedule(ThingEvent schedule);

	public abstract ThingEvent updateSchedule(ThingEvent schedule);

}