package com.n4systems.model.utils;

import java.io.Serializable;

import org.joda.time.LocalDate;


public interface DateRangeFormatter extends Serializable {
	
	public String getFromDateDisplayString(LocalDate localDate);
	public String getToDateDisplayString(LocalDate localDate);
	public String getDisplayName(DateRange dateRange);

}
