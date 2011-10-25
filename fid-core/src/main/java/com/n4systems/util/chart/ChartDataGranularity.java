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

	public Long delta() {
		Calendar today = Calendar.getInstance();		
		// try to get around 30 data points per plot... 
		Calendar from = Calendar.getInstance();
		from.setTimeInMillis(today.getTimeInMillis());
		
		switch (this) { 
		case DAY:
			from.add(Calendar.MONTH, -1);
			break;
		case WEEK:
			from.add(Calendar.YEAR, -1);
			break;
		case MONTH: 
			from.add(Calendar.YEAR, -2);
			break;
		case QUARTER:
			from.add(Calendar.YEAR, -4);
			break;
		case YEAR:
		case ALL:
			from.set(Calendar.YEAR, 2007);
			break;
		}
		return today.getTimeInMillis()- from.getTimeInMillis();
	}
	

}
