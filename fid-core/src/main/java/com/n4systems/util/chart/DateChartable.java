package com.n4systems.util.chart;

import com.n4systems.util.time.DateUtil;
import org.joda.time.LocalDate;

import java.util.Date;

/**
 * note that the granularity affects the actual date. 
 * so if you pass in the date may 2, 2011 it gets transformed to :  
 *   YEAR = jan 1, 2011
 *   QUARTER = april 1, 2011
 *   MONTH = may 1, 2011
 *   
 * currently doesn't support hours, minutes, seconds etc...         
 */
@SuppressWarnings("serial")
public class DateChartable extends AbstractChartable<LocalDate> {
	
	public DateChartable(LocalDate x, Number y) {
		super(x,y);
	}
	
    public DateChartable(Date date, Long value) {
    	this(new LocalDate(date), value);
	}
	
	public DateChartable(Long value, ChartGranularity granularity, Integer year, Integer month, Integer day) {		
		this(granularity.normalize(DateUtil.getLocalDate(year, month, day)), value);		
	}
	
	public DateChartable(Long value, String granularity, Integer year, Integer month, Integer day) {		
		this(DateUtil.getLocalDate(year, month, day), ChartGranularity.valueOf(granularity),  value);		
	}	

	public DateChartable(LocalDate date, ChartGranularity granularity, Long value) {
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
