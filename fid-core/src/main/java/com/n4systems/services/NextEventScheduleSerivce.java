package com.n4systems.services;

import com.n4systems.model.ThingEvent;

public interface NextEventScheduleSerivce {

	/**
	 * Creates the next event schedule for the asset.  If there is already a schedule
	 * for the contained asset and event type it will simply return that one.
	 * @return The newly created schedule, or the already existing one.
	 */
	public ThingEvent createNextSchedule(ThingEvent schedule);

}