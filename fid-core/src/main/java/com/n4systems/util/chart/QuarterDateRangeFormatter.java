package com.n4systems.util.chart;

import com.n4systems.model.utils.DateRange;
import com.n4systems.model.utils.DateRangeFormatter;
import org.joda.time.LocalDate;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class QuarterDateRangeFormatter implements DateRangeFormatter {

	private String displayName;
	
	private DateFormat fromFormatter = new SimpleDateFormat("MMM");	
	private DateFormat toFormatter = new SimpleDateFormat("MMM yyyy");	

	public QuarterDateRangeFormatter(String displayName) {
		this.displayName = displayName;
	}
	
	@Override
	public String getFromDateDisplayString(LocalDate localDate) {
		return fromFormatter.format(localDate.toDate());
	}

	@Override
	public String getToDateDisplayString(LocalDate localDate) {
		return toFormatter.format(localDate.toDate());
	}

	@Override
	public String getDisplayName(DateRange dateRange) {
		return displayName;
	}

}
