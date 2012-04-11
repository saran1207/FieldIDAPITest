package com.n4systems.util.chart;

import org.joda.time.LocalDate;
import org.joda.time.Period;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

public enum ChartPeriod {
	
	SEVEN_DAYS(7), THIRTY_DAYS(30), SIXTY_DAYS(60), NINETY_DAYS(90);
	
	private int days;
	private DateFormat dateFormat = new SimpleDateFormat("MMM d");
	private Period period;
	

	ChartPeriod(int days) { 
		this.days = days;
		this.period = new Period().withDays(days);
	}

	public static ChartPeriod valueOf(Integer days) {
		for (ChartPeriod period:values()) { 
			if (period.days==days) {
				return period;
			}
		}
		return null;
	}

	public Period getPeriod() { 
		return period;
	}
	
	public String getFromDisplayString() { 
		return dateFormat.format(new LocalDate().toDate());
	}

	public String getToDisplayString() {
		return dateFormat.format( new LocalDate().plusDays(days).toDate() );
	}

}
