package com.n4systems.util.chart;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import com.google.common.collect.Lists;
import com.google.common.collect.Ordering;




public enum ChartGranularity {
	
	HOUR(Calendar.HOUR_OF_DAY), DAY(Calendar.DAY_OF_YEAR), WEEK(Calendar.WEEK_OF_YEAR), MONTH(Calendar.MONTH), QUARTER(Calendar.MONTH,3), YEAR(Calendar.YEAR);

	private int field;
	private int multiplier;

	private ChartGranularity(int cal) { 
		this(cal,1);
	}
	
	private ChartGranularity(int cal, int multiplier) { 
		this.field = cal;
		this.multiplier = multiplier;
	}

	public int compare(Calendar a, Calendar b) {		
		int compare = 0;
		if (a==null) { 
			return b==null ? 0 : -1; 
		}
		if (b==null) {
			return 1;
		}
		for (ChartGranularity granularity:reverseValues()) {  // CAVEAT : enum must be in order of least to most significant. i.e. months before years. 
			if (granularity.compareTo(this)>=0) {
				compare = (a.get(granularity.field)/granularity.multiplier) - (b.get(granularity.field)/granularity.multiplier);
				if (compare!=0) {
					break;
				}
			}
		}
		return compare;		
	}
	
	private List<ChartGranularity> reverseValues() {
		ArrayList<ChartGranularity> result = Lists.newArrayList(values());
		Collections.sort(result, Ordering.natural().reverse());
		return result;
	}

	public Long delta() {
		Calendar today = Calendar.getInstance();		
		// try to get around 50 data points per plot... 
		Calendar from = Calendar.getInstance();
		from.setTimeInMillis(today.getTimeInMillis());
		
		switch (this) { 
		case DAY:
			from.add(Calendar.MONTH, -1);
			break;
		case WEEK:
			from.add(Calendar.MONTH, -12);
			break;
		case MONTH: 
			from.add(Calendar.YEAR, -3);
			break;
		case QUARTER:
			from.add(Calendar.YEAR, -7);
			break;
		case YEAR:
			from.set(Calendar.YEAR, 2005);
			break;
		}
		return today.getTimeInMillis()- from.getTimeInMillis();
	}

	public Calendar next(Calendar calendar) {
		Calendar c = Calendar.getInstance();
		c.setTime(calendar.getTime());
		c.add(field, 1*multiplier);		
		return c;
	}

	public Calendar previous(Calendar calendar) {
		Calendar c = Calendar.getInstance();
		c.setTime(calendar.getTime());
		c.add(field, -1*multiplier);		
		return c;
	}
	

}
