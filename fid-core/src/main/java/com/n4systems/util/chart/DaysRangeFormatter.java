package com.n4systems.util.chart;

import org.joda.time.LocalDate;

import com.n4systems.model.utils.DateRange;
import com.n4systems.model.utils.DateRangeFormatter;

public class DaysRangeFormatter implements DateRangeFormatter {
	
	private String displayName;
	private int days;

	public DaysRangeFormatter(String displayName, int days) {
		this.displayName = displayName;
		this.days = days;
	}

	@Override
	public String getFromDateDisplayString(LocalDate localDate) {
		return "";
	}

	@Override
	public String getToDateDisplayString(LocalDate localDate) {
		return displayName;
	}

	@Override
	public String getDisplayName(DateRange dateRange) {
		return displayName;
	}

}
