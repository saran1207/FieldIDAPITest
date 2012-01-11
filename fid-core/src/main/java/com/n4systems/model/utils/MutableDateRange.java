package com.n4systems.model.utils;

import java.util.Date;

import org.joda.time.LocalDate;

import com.n4systems.util.chart.ChartDateRange;


public class MutableDateRange extends DateRange {

	private ChartDateRange chartDateRange;

	public MutableDateRange() {
		super();		
	}
	
	public void setFromDate(Date from) {
		this.from = new LocalDate(from);
	}
	
	public void setToDate(Date to) {
		this.to = new LocalDate(to);
	}
	
	public void setDateRange(ChartDateRange dateRange) {		
		this.chartDateRange = dateRange;		
	}
	
	
	
	
}
