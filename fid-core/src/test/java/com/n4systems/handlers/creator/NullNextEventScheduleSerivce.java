/**
 * 
 */
package com.n4systems.handlers.creator;

import com.n4systems.model.ThingEvent;
import com.n4systems.services.NextEventScheduleSerivce;

import java.util.Random;

final class NullNextEventScheduleSerivce implements NextEventScheduleSerivce {
	@Override
	public ThingEvent createNextSchedule(ThingEvent schedule) {
		schedule.setId(new Random().nextLong());
		return schedule;
	}
}