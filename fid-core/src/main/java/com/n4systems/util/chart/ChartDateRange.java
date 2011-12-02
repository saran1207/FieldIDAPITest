package com.n4systems.util.chart;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.joda.time.LocalDate;

import com.n4systems.exceptions.InvalidArgumentException;
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
		return formatter.format(getFrom().toDate());
	}
	
	public String getToDateDisplayString() {
		if (this.equals(FOREVER)) { 
			return "";
		} else { 
			return dateFormatters.get(this).format(getInclusiveTo().toDate());
		}
	}
	
	// note that toDate is exclusive.  e.g. for a year 2011
	// Jan 1, 2011 is from.   
	//  and to is Jan 1, 2012.  *not*  dec 31,2011    [11:59:59.9999...]
	// this is used for display reasons. 
	private LocalDate getInclusiveTo() {
		return getTo().minusDays(1);
	}

	public LocalDate getTo() { 
		// exclusive date :  should use <  *not*  <= when comparing against returned value!!!
		LocalDate today = LocalDate.now();
		
		switch (this) {
			case FOREVER: 			
				return DateUtil.getLatestFieldIdDate();
			case LAST_YEAR:
				return today.withDayOfYear(1); 
			case THIS_YEAR:
				return today.plusYears(1).withDayOfYear(1);
			case LAST_QUARTER:
				return today.minusMonths((today.getMonthOfYear()-1)%3).withDayOfMonth(1);
			case THIS_QUARTER:
				return today.plusMonths(3-(today.getMonthOfYear()-1)%3).withDayOfMonth(1);
			case LAST_MONTH:
				return today.withDayOfMonth(1);
			case THIS_MONTH:
				return today.plusMonths(1).withDayOfMonth(1);
			case LAST_WEEK:
				return today.withDayOfWeek(1);
			case THIS_WEEK:
				return today.plusWeeks(1).withDayOfWeek(1);
			default: 
				throw new InvalidArgumentException("ChartDateRange " + this + " not supported."); 
		}
	}

	public LocalDate getFrom() {
		LocalDate today = new LocalDate();
		switch (this) {
			case FOREVER: 			
				return DateUtil.getEarliestFieldIdDate();
			case LAST_YEAR:
				return today.minusYears(1).withDayOfYear(1);				
			case THIS_YEAR:
				return today.withDayOfYear(1);				
			case LAST_QUARTER:
				return today.minusMonths(3+(today.getMonthOfYear()-1)%3).withDayOfMonth(1);
			case THIS_QUARTER:
				return today.minusMonths((today.getMonthOfYear()-1)%3).withDayOfMonth(1);
			case LAST_MONTH:
				return today.minusMonths(1).withDayOfMonth(1);
			case THIS_MONTH:
				return today.withDayOfMonth(1);
			case LAST_WEEK:
				return today.minusWeeks(1).withDayOfWeek(1);
			case THIS_WEEK:
				return today.withDayOfWeek(1);
			default: 
				throw new InvalidArgumentException("ChartDateRange " + this + " not supported."); 
		}
	}
		
	public String getDisplayName() { 
		return displayName;
	}

	public Date getFromDate() {
		return getFrom().toDate();
	}

	public Date getToDate() {
		return getTo().toDate();
	}

	
}
