package com.n4systems.model.utils;

import org.joda.time.LocalDate;

import java.io.Serializable;


public interface DateRangeFormatter extends Serializable {
	
	public String getFromDateDisplayString(LocalDate localDate);
	public String getToDateDisplayString(LocalDate localDate);
	public String getDisplayName(DateRange dateRange);

}
