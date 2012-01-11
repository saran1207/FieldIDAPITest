package com.n4systems.model.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import org.joda.time.LocalDate;


public class DefaultDateRangeFormatter implements DateRangeFormatter {

	private DateFormat formatter;

	public DefaultDateRangeFormatter(String format) {
		this.formatter = new SimpleDateFormat(format);
	}
	
	@Override
	public String getFromDateDisplayString(LocalDate localDate) {
		return formatter.format(localDate.toDate());
	}

	@Override
	public String getToDateDisplayString(LocalDate localDate) {
		return formatter.format(localDate);
	}

	@Override
	public String getDisplayName(DateRange dateRange) {
		return getFromDateDisplayString(dateRange.getFrom()) + "..." + getToDateDisplayString(dateRange.getTo());
	}

}
