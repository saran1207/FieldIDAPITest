package com.n4systems.util.chart;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.n4systems.util.time.DateUtil;

public enum ChartDateRange {

	LAST_WEEK("Last Week" ), 
	LAST_MONTH("Last Month"), 
	LAST_QUARTER("Last Quarter"), 
	LAST_YEAR("Last Year"),
	THIS_WEEK("This Week"), 
	THIS_MONTH("This Month"), 
	THIS_QUARTER("This Quarter"), 
	THIS_YEAR("This Year"), 
	FOREVER("All Time");
	
	private String displayName;

	private static Map<ChartDateRange, DateFormat> dateFormatters = new HashMap<ChartDateRange, DateFormat>();

	private static DateFormat quarterFromFormat;
	
	static { 
		dateFormatters.put(LAST_WEEK, new SimpleDateFormat("MMM d"));
		dateFormatters.put(LAST_MONTH, new SimpleDateFormat("MMM yyyy"));
		dateFormatters.put(LAST_QUARTER, new SimpleDateFormat("MMM yyyy"));
		dateFormatters.put(LAST_YEAR, new SimpleDateFormat("yyyy"));
		dateFormatters.put(THIS_WEEK, new SimpleDateFormat("MMM d"));
		dateFormatters.put(THIS_MONTH, new SimpleDateFormat("MMM yyyy"));
		dateFormatters.put(THIS_QUARTER, new SimpleDateFormat("MMM yyyy"));
		dateFormatters.put(THIS_YEAR, new SimpleDateFormat("yyyy"));
		dateFormatters.put(FOREVER, null);	// FOREVER just returns a text string. 
		quarterFromFormat = new SimpleDateFormat("MMM");  // NOTE : quarter display = Jan-Mar 2011.  i.e. "from" is different format than "to"
	}

	ChartDateRange(String displayName) {
		this.displayName = displayName;		
	}

	public String getFromDateDisplayString() {
		DateFormat formatter = dateFormatters.get(this);
		if (this.equals(FOREVER)) { 
			return "All Time";
		}
		if (this.equals(LAST_QUARTER) || this.equals(THIS_QUARTER)) {
			formatter = quarterFromFormat;  
		}
		return formatter.format(getFromDate());
	}
	
	public String getToDateDisplayString() {
		if (this.equals(FOREVER)) { 
			return "";
		} else { 
			return dateFormatters.get(this).format(getInclusiveToDate());
		}
	}
	
	// note that toDate is exclusive.  e.g. for a year 2011
	// Jan 1, 2011 is from.   and to is Jan 1, 2012.  (not dec 31,11:59:59.9999...)
	// .: this is used for display reasons 
	private Date getInclusiveToDate() {
		Calendar to = getToCalendar();
		to.add(Calendar.DAY_OF_YEAR, -1);
		return to.getTime();
	}

	public Date getFromDate() {
		return getFromCalendar().getTime();
	}
		
	public Calendar getToCalendar() { 
		// exclusive date :  should use <  *not*  <= when comparing against returned value!!!   
		Calendar calendar = DateUtil.getTimelessIntance();
		switch (this) {
		case FOREVER: 			
			return DateUtil.getLatestCalendar();
		case LAST_YEAR:
			calendar.set(Calendar.DAY_OF_YEAR, 1);
			break;			
		case LAST_QUARTER:
			calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH)/3 * 3);  // round to nearest quarter...
			calendar.set(Calendar.DAY_OF_MONTH, 1);
			break;
		case LAST_MONTH:
			calendar.set(Calendar.DAY_OF_MONTH, 1);
			break;			
		case LAST_WEEK:
			calendar.set(Calendar.DAY_OF_WEEK, 1);
			break;
		case THIS_WEEK:
			calendar.set(Calendar.DAY_OF_WEEK, 1);
			calendar.add(Calendar.WEEK_OF_YEAR, 1);			
			break;
		case THIS_MONTH:
			calendar.add(Calendar.MONTH, 1);
			calendar.set(Calendar.DAY_OF_MONTH, 1);
			break;
		case THIS_QUARTER:
			calendar.set(Calendar.MONTH, 3 + (calendar.get(Calendar.MONTH)/3)*3);  // round to nearest quarter...
			calendar.set(Calendar.DAY_OF_MONTH,1);
			break;
		case THIS_YEAR:
			calendar.add(Calendar.YEAR, 1);
			calendar.set(Calendar.DAY_OF_YEAR, 1);
			break;
		}
		return calendar;
	}

	public Calendar getFromCalendar() {
		Calendar calendar = DateUtil.getTimelessIntance();
		switch (this) {
		case FOREVER: 			
			return DateUtil.getEarliestFieldIdCalendar();
		case LAST_YEAR:
			calendar.add(Calendar.YEAR, -1);
			calendar.set(Calendar.DAY_OF_YEAR, 1);
			break;
		case LAST_QUARTER:
			//Q3/2011= Q2/2011
			calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH)/3 * 3);  // round to nearest quarter...
			calendar.add(Calendar.MONTH, -3);										// then go back one.
			calendar.set(Calendar.DAY_OF_MONTH, 1);
			break;
		case LAST_MONTH:
			calendar.add(Calendar.MONTH, -1);
			calendar.set(Calendar.DAY_OF_MONTH, 1);
			break;
		case LAST_WEEK:
			calendar.add(Calendar.WEEK_OF_YEAR, -1);
			calendar.set(Calendar.DAY_OF_WEEK, 1);
			break;
		case THIS_WEEK:
			calendar.set(Calendar.DAY_OF_WEEK,1);
			break;
		case THIS_MONTH:
			calendar.set(Calendar.DAY_OF_MONTH,1);
			break;
		case THIS_QUARTER:
			calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH)/3 * 3);  
			calendar.set(Calendar.DAY_OF_MONTH,1);
			break;
		case THIS_YEAR:
			calendar.set(Calendar.DAY_OF_YEAR, 1);
			break;
		}
		return calendar;
	}
	
	public Date getToDate() { 
		return getToCalendar().getTime();
	}
	
	public String getDisplayName() { 
		return displayName;
	}
	
}
