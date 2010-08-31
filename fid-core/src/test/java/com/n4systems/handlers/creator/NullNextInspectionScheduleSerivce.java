/**
 * 
 */
package com.n4systems.handlers.creator;

import java.util.Random;

import com.n4systems.model.InspectionSchedule;
import com.n4systems.services.NextInspectionScheduleSerivce;

final class NullNextInspectionScheduleSerivce implements NextInspectionScheduleSerivce {
	@Override
	public InspectionSchedule createNextSchedule(InspectionSchedule schedule) {
		schedule.setId(new Random().nextLong());
		return schedule;
	}
}