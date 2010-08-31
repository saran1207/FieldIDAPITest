package com.n4systems.model.notificationsettings;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import com.n4systems.model.common.RelativeTime;
import com.n4systems.util.Range;
import com.n4systems.util.time.Clock;


@Embeddable
public class UpcomingEventReport {
	
	@Enumerated(EnumType.STRING)
	@Column(name="periodstart", nullable=false)
	private RelativeTime periodStart;
	
	
	@Enumerated(EnumType.STRING)
	@Column(name="periodend", nullable=false)
	private RelativeTime periodEnd;
	
	private boolean includeUpcoming;
	
	public UpcomingEventReport() {
		this(RelativeTime.TODAY, RelativeTime.NEXT_WEEK, true);
	}
	public UpcomingEventReport(RelativeTime periodStart, RelativeTime periodEnd, boolean include) {
		this.periodStart = periodStart;
		this.periodEnd = periodEnd;
		this.includeUpcoming = include;
	}
	
	public RelativeTime getPeriodStart() {
		return periodStart;
	}
	public RelativeTime getPeriodEnd() {
		return periodEnd;
	}
	
	
	public Range<Date> getDateRange(Clock clock) {
		Date startDate = periodStart.getRelative(clock.currentTime());
		Date endDate = periodEnd.getRelative(startDate);
		
		return new Range<Date>(startDate, endDate);
		
	}
	public boolean isIncludeUpcoming() {
		return includeUpcoming;
	}
	
	
}