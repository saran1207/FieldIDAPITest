package com.n4systems.util.chart;

import java.util.Date;

import org.joda.time.LocalDate;

import com.n4systems.util.time.DateUtil;

/**
 * note that the granularity affects the actual date. 
 * so if you pass in the date may 2, 2011 it gets transformed to :  
 *   YEAR = jan 1, 2011
 *   QUARTER = april 1, 2011
 *   MONTH = may 1, 2011
 *   WEEK =  ?? 
 *   
 * currently doesn't support hours, minutes, seconds etc...         
 */
@SuppressWarnings("serial")
public class CalendarChartable extends AbstractChartable<LocalDate> {
	
	public CalendarChartable(LocalDate x, Number y) {
		super(x,y);
	}
	
    public CalendarChartable(Date date, Long value) {
    	this(new LocalDate(date), value);
	}
	
	public CalendarChartable(Long value, ChartGranularity granularity, Integer year, Integer month, Integer day) {		
		this(granularity.normalize(DateUtil.getLocalDate(year, month, day)), value);		
	}
	
	public CalendarChartable(Long value, String granularity, Integer year, Integer month, Integer day) {		
		this(DateUtil.getLocalDate(year, month, day), ChartGranularity.valueOf(granularity),  value);		
	}	

	public CalendarChartable(LocalDate date, ChartGranularity granularity, Long value) {
		this(granularity.normalize(date), value );
	}

	@Override
	public Long getLongX() {
		return getX().toDate().getTime();
    }

    
	public String getJavascriptX() {	    	
    	return ""+getLongX();
    }	
    
	@Override
	public String toString() { 
		return getX().toString() + " : " + getY();
	}
	    
}
