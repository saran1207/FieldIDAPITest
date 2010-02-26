package com.n4systems.model.inspectionschedulecount;

import java.util.Date;

import com.n4systems.model.InspectionSchedule.ScheduleStatus;
import com.n4systems.model.security.SecurityFilter;
import com.n4systems.util.persistence.QueryBuilder;
import com.n4systems.util.persistence.WhereParameter.Comparator;

public class UpcomingInspectionScheduleCountListLoader extends NotificationSettingInspectionScheduleCountListLoader {
	private Date fromDate;
	private Date toDate;
	public UpcomingInspectionScheduleCountListLoader(SecurityFilter filter) {
		super(filter);
	}

	protected void applyNotificationTypeFilters(QueryBuilder<InspectionScheduleCount> builder) {
		// we only want schedules that have not been completed
		builder.addWhere(Comparator.NE, "status", "status", ScheduleStatus.COMPLETED);
		
		// from dates are inclusive, to dates are exclusive.  See the RelativeTime class for why it works this way
		builder.addWhere(Comparator.GE, "fromDate", "nextDate", fromDate);
		builder.addWhere(Comparator.LT, "toDate", "nextDate", toDate);
	}

	public void setFromDate(Date fromDate) {
    	this.fromDate = fromDate;
    }

	public void setToDate(Date toDate) {
    	this.toDate = toDate;
    }
	
}
