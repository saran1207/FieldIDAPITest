package com.n4systems.util.chart;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import com.n4systems.util.time.DateUtil;

public enum ChartPeriod {
	
	SEVEN_DAYS(7), THIRTY_DAYS(30), SIXTY_DAYS(60), NINETY_DAYS(90);
	
	private int days;
	private DateFormat dateFormat = new SimpleDateFormat("MMM d");
	

	ChartPeriod(int days) { 
		this.days = days;
	}

	public static ChartPeriod valueOf(Integer days) {
		for (ChartPeriod period:values()) { 
			if (period.days==days) {
				return period;
			}
		}
		return null;
	}
	
	public String getFromDisplayString() { 
		return dateFormat.format(DateUtil.getMidnightIntance().getTime());
	}

	public String getToDisplayString() {
		Calendar to = DateUtil.getMidnightIntance();
		to.add(Calendar.DAY_OF_YEAR, days);
		return dateFormat.format(to.getTime());
	}

}
