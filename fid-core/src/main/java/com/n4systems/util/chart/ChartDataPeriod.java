package com.n4systems.util.chart;

import java.util.Calendar;
import java.util.Date;



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
	
	public long getMsDelta() { 
		Calendar a = Calendar.getInstance();
		Calendar b = Calendar.getInstance();
		System.out.println(new Date(a.getTimeInMillis()));
		b.setTimeInMillis(a.getTimeInMillis());
		b.add(cal, 1*multiplier);
		System.out.println(new Date(b.getTimeInMillis()));
		return b.getTimeInMillis() - a.getTimeInMillis();		
	}
	
}
