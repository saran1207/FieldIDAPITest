package com.n4systems.services.reporting;

import java.util.Calendar;

import com.n4systems.util.chart.Chartable;


public class AssetsIdentifiedReportRecord implements Chartable {	

	private int year;
	private int quarter;
	private long value;

	public AssetsIdentifiedReportRecord() { 
		
	}
	
	public AssetsIdentifiedReportRecord(Integer year, Integer quarter, Long value) {
		setYear(year);
		setQuarter(quarter);
		this.value = value;
	}
		
	public Integer getYear() {
		return year;
	}
	
	public void setYear(Integer year) {
		this.year = year;
	}
	
	public Long getValue() {
		return value;
	}
	
	public void setValue(Long v) {
		this.value = v;
	}

	@Override
	public Long getX() {
		Calendar calendar = Calendar.getInstance();
		calendar.clear();
		calendar.set(Calendar.YEAR, year);
		calendar.set(Calendar.MONTH, quarter*3);
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		return calendar.getTime().getTime();
	}

	@Override
	public Long getY() {
		return value; 
	}

	public void setQuarter(Integer quarter) {
		this.quarter = quarter;
	}

	public Integer getQuarter() {
		return quarter;
	}
	

}
