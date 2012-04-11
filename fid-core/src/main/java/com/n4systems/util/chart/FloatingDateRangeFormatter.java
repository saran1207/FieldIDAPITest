package com.n4systems.util.chart;

import com.n4systems.model.utils.DateRange;
import com.n4systems.model.utils.DateRangeFormatter;
import org.joda.time.LocalDate;

import java.text.SimpleDateFormat;

public class FloatingDateRangeFormatter implements DateRangeFormatter {

	private String displayName;
	private SimpleDateFormat dateFormat;

	public FloatingDateRangeFormatter(String displayName, String format) { 
		this.displayName = displayName;
		this.dateFormat = new SimpleDateFormat(format);
	}
	
	@Override
	public String getFromDateDisplayString(LocalDate localDate) {
		return dateFormat.format(localDate.toDate());
	}

	@Override
	public String getToDateDisplayString(LocalDate localDate) {
		return dateFormat.format(localDate.toDate());
	}

	@Override
	public String getDisplayName(DateRange dateRange) {
		return displayName;
	}


}
