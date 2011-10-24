package com.n4systems.util.chart;

import java.util.Calendar;



public enum ChartDataGranularity {
	
	HOUR(Calendar.HOUR_OF_DAY), DAY(Calendar.DAY_OF_YEAR), WEEK(Calendar.DAY_OF_WEEK), MONTH(Calendar.MONTH), QUARTER(Calendar.MONTH,3), YEAR(Calendar.YEAR), ALL(-1);

	private int cal;
	private int multiplier;

	private ChartDataGranularity(int cal) { 
		this(cal,1);
	}
	
	private ChartDataGranularity(int cal, int multiplier) { 
		this.cal = cal;
		this.multiplier = multiplier;
	}
	
//	public Calendar nextPeriod(Calendar c) {
//		if (ALL_TIME.equals(this)) { 
//			return c;  
//		}
//		c.add(cal, multiplier);
//		return c;
//	}
	
}
