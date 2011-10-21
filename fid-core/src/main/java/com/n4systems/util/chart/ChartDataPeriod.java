package com.n4systems.util.chart;

import java.util.Calendar;



public enum ChartDataPeriod {
	
	DAILY(Calendar.DAY_OF_YEAR), WEEKLY(Calendar.DAY_OF_WEEK), MONTHLY(Calendar.MONTH), QUARTERLY(Calendar.MONTH,3), YEARLY(Calendar.YEAR);

	private int cal;
	private int multiplier;

	private ChartDataPeriod(int cal) { 
		this(cal,1);
	}
	
	private ChartDataPeriod(int cal, int multiplier) { 
		this.cal = cal;
		this.multiplier = multiplier;
	}
	
	public Calendar nextPeriod(Calendar c) {
		c.add(cal, multiplier);
		return c;
	}
	
}
