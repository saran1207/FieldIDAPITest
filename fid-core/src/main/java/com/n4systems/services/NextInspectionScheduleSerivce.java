package com.n4systems.services;

import com.n4systems.model.EventSchedule;

public interface NextInspectionScheduleSerivce {

	/**
	 * Creates the next inspection schedule for the asset.  If there is already a schedule
	 * for the contained asset and inspection type it will simply return that one.
	 * @return The newly created schedule, or the already existing one.
	 */
	public EventSchedule createNextSchedule(EventSchedule schedule);

}