/**
 * 
 */
package com.n4systems.handlers.creator;

import java.util.Random;

import com.n4systems.model.Event;

import com.n4systems.model.ThingEvent;
import com.n4systems.services.NextEventScheduleSerivce;

final class NullNextEventScheduleSerivce implements NextEventScheduleSerivce {
	@Override
	public ThingEvent createNextSchedule(ThingEvent schedule) {
		schedule.setId(new Random().nextLong());
		return schedule;
	}
}