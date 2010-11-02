package com.n4systems.model.inspectionschedulecount;

import com.n4systems.exceptions.InvalidArgumentException;
import com.n4systems.model.EventSchedule.ScheduleStatus;
import com.n4systems.model.security.SecurityFilter;
import com.n4systems.util.persistence.QueryBuilder;
import com.n4systems.util.persistence.WhereParameter.Comparator;
import com.n4systems.util.time.Clock;

public class OverdueInspectionScheduleCountListLoader extends NotificationSettingInspectionScheduleCountListLoader {


	private Clock clock;

	public OverdueInspectionScheduleCountListLoader(SecurityFilter filter) {
		super(filter);
	}
	
	

	@Override
	protected void applyNotificationTypeFilters(QueryBuilder<InspectionScheduleCount> builder) {
		guard();
		builder.addWhere(Comparator.NE, "status", "status", ScheduleStatus.COMPLETED);
		builder.addWhere(Comparator.LT, "toDate", "nextDate", clock.currentTime());
	}



	private void guard() {
		if (clock == null) {
			throw new InvalidArgumentException("you must provide a clock");
		}
	}
	
	public OverdueInspectionScheduleCountListLoader setClock(Clock clock) {
		this.clock = clock;
		return this;
	}

}
