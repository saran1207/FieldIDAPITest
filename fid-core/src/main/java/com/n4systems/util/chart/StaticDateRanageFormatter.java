package com.n4systems.util.chart;

import org.joda.time.LocalDate;

import com.n4systems.model.utils.DateRange;
import com.n4systems.model.utils.DateRangeFormatter;

public class StaticDateRanageFormatter implements DateRangeFormatter {

	private String displayName;

	public StaticDateRanageFormatter(String displayName) {
		this.displayName = displayName;
	}
	
	@Override
	public String getFromDateDisplayString(LocalDate localDate) {
		return displayName;
	}

	@Override
	public String getToDateDisplayString(LocalDate localDate) {
		return "";
	}

	@Override
	public String getDisplayName(DateRange dateRange) {
		return displayName;
	}

}