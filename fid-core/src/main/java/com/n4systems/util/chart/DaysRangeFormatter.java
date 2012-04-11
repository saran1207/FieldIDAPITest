package com.n4systems.util.chart;

import com.n4systems.model.utils.DateRange;
import com.n4systems.model.utils.DateRangeFormatter;
import org.joda.time.LocalDate;

@SuppressWarnings("serial")
public class DaysRangeFormatter implements DateRangeFormatter {
	
	private String displayName;

	public DaysRangeFormatter(String displayName) {
		this.displayName = displayName;
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
