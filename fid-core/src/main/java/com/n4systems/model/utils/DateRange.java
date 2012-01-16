package com.n4systems.model.utils;

import java.io.Serializable;
import java.util.Date;

import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;
import org.joda.time.Duration;
import org.joda.time.LocalDate;
import org.joda.time.Period;

import com.google.common.base.Preconditions;
import com.n4systems.util.chart.ChartDateRange;
import com.n4systems.util.chart.DaysRangeFormatter;
import com.n4systems.util.chart.FloatingDateRangeFormatter;
import com.n4systems.util.chart.QuarterDateRangeFormatter;
import com.n4systems.util.chart.StaticDateRanageFormatter;


/**
 *  handles floating dates & static dates
 * e.g. LAST WEEK   or     jan 1-jan7    or  ALL TIME    or   jan1,1970-jan1,2099
 * @see ChartDateRange
 * 
 * note that it is up to the user to decide if this represents an inclusive or exclusive range. 
 * does Jan1-Jan8 mean Jan1,2,3,4,5,6,7?   (one week before the 8th).     OR 
 *      Jan1-Jan8 mean Jan1,2,3,4,5,6,7,8?
 *      
 * this object is not aware of the context but you should know if you use LE or LT in queries.   
 * LT = use getToDate();  LE = use getInclusiveToDate()
 */
@SuppressWarnings("serial")
public class DateRange implements Serializable {
	
	// list of static floating date ranges. 
	public static final DateRange SEVEN_DAYS = new DateRange(7, new DaysRangeFormatter("7 days")); 
	public static final DateRange THIRTY_DAYS = new DateRange(30, new DaysRangeFormatter("30 days")); 
	public static final DateRange SIXTY_DAYS = new DateRange(60, new DaysRangeFormatter("60 days")); 
	public static final DateRange NINETY_DAYS = new DateRange(90, new DaysRangeFormatter("90 days")); 
	public static final DateRange LAST_WEEK = new DateRange(new WeekHandler(-1),  new FloatingDateRangeFormatter("Last Week", "MMM d")); 
	public static final DateRange LAST_MONTH = new DateRange(new MonthHandler(-1), new FloatingDateRangeFormatter("Last Month", "MMM yyyy")); 
	public static final DateRange LAST_QUARTER = new DateRange(new QuarterHandler(-1), new QuarterDateRangeFormatter("Last Quarter")); 
	public static final DateRange LAST_YEAR = new DateRange(new YearHandler(-1), new FloatingDateRangeFormatter("Last Year", "yyyy"));
	public static final DateRange THIS_WEEK = new DateRange(new WeekHandler(0), new FloatingDateRangeFormatter("This Week", "MMM d"));
	public static final DateRange THIS_MONTH = new DateRange(new MonthHandler(0), new FloatingDateRangeFormatter("This Month", "MMM yyyy")); 
	public static final DateRange THIS_QUARTER = new DateRange(new QuarterHandler(0), new QuarterDateRangeFormatter("This Quarter")); 
	public static final DateRange THIS_YEAR = new DateRange(new YearHandler(0), new FloatingDateRangeFormatter("This Year", "yyyy"));
	public static final DateRange FOREVER = new DateRange(new StaticDateRanageFormatter("All Time"));
	public static final DateRange CUSTOM = new DateRange(new StaticDateRanageFormatter("Custom Date Range"));

	public static final Period OPEN_PERIOD = new Period().withYears(3000);  // can't use MAX_INT 'cause that causes overflow in certain scenarios.
	
		
	private DateRangeFormatter formatter = new DefaultDateRangeFormatter("MMM d yyyy");
	private DateRangeHandler handler;
	
	public DateRange(DateRangeHandler handler) {
		this.handler = handler;
	}
	
	public DateRange(DateRangeHandler handler, DateRangeFormatter formatter) {
		Preconditions.checkArgument(formatter!=null,"must specify a valid date range formatter.");
		this.handler = handler;
		this.formatter = formatter;
	}
	
	public DateRange(int days, DateRangeFormatter formatter) { 
		this(new DayRangeHandler(days),formatter);		
	}
	
	public DateRange(LocalDate from, LocalDate to) { 
		this(new IntervalHandler(from,to));
	}	
	
	public DateRange(Date from, Date to) { 
		this(new IntervalHandler(from,to));
	}	
	
	public DateRange(DateRangeFormatter formatter) {
		this(new IntervalHandler(),formatter);
	}

	public DateRange() { 
		this(new IntervalHandler());
	}	
	

	public String getFromDateDisplayString() {
		return formatter.getFromDateDisplayString(getFrom());
	}

	public String getToDateDisplayString() {
		return formatter.getToDateDisplayString(getTo());
	}
	
	// note that getTo() is exclusive.  e.g. for a year 2011
	// Jan 1, 2011 is getFrom() and getTo() is Jan 1, 2012  *not*  dec 31,2011    [11:59:59.9999...]
	// this method is used for display reasons.  (e.g.  "hello oct 1,2011-oct 31,2011") 
	private LocalDate getInclusiveTo() {
		LocalDate to = getTo();
		return (to==null) ? null : to.minusDays(1);
	}
	
	public Date getInclusiveToDate() {
		LocalDate to = getInclusiveTo();
		return (to==null) ? null : to.toDate();
	}

	public LocalDate getTo() {
		return handler.getNowTo();		
	}

	public LocalDate getFrom() {
		return handler.getNowFrom();
	}
	
	public Date getFromDate() {
		LocalDate from = getFrom();
		return from==null ? null : from.toDate();
	}

	public Date getToDate() {
		LocalDate to = getTo();
		return to==null ? null : to.toDate();
	}


	public String getDisplayName() {
		return formatter.getDisplayName(this);
	}
	
	public Period getPeriod() {
		return handler.getPeriod();		
	}
	
	public Duration getDuration() { 
		if (OPEN_PERIOD==getPeriod()) {
			return Duration.standardDays(9999);
		} else { 
			DateTime x = new DateTime();
			DateTime y = x.plus(getPeriod());
			return new Duration(x,y);			
		}
	}
	
	
	// ---------------------------------- internal classes -----------------------------------------------

	






	static abstract class AbstractDateRangeHandler implements DateRangeHandler {
		private Period period;

		public AbstractDateRangeHandler(Period period) { 
			this.period = period;
		}
		@Override public LocalDate getNowFrom() {
			return LocalDate.now();
		}
		@Override public LocalDate getNowTo() {
			return getNowFrom().plus(period);			
		}
		@Override public Period getPeriod() {
			return period;
		}
		
	}
	
	public static class IntervalHandler implements DateRangeHandler {
		private LocalDate from;
		private LocalDate to;

		public IntervalHandler() {
			this((Date)null,(Date)null);
		}
		public IntervalHandler(Date from, Date to) { 
			this(from==null ? null : new LocalDate(from), to==null ? null : new LocalDate(to));
		}
		public IntervalHandler(LocalDate from, LocalDate to) { 
			this.from = from;
			this.to = to;
		}
		@Override public LocalDate getNowFrom() {
			return from;
		} 
		@Override public LocalDate getNowTo() {
			return to;
		}
		@Override public Period getPeriod() {
			return from==null||to==null ? OPEN_PERIOD : new Period(from,to);
		}
	}
	
	public static  class WeekHandler extends AbstractDateRangeHandler {
		private int offset;
		public WeekHandler(int i) {
			super(Period.weeks(1));
			this.offset = i;
		}
		@Override
		public LocalDate getNowFrom() {
			return LocalDate.now().withDayOfWeek(DateTimeConstants.MONDAY).plusWeeks(offset);			
		}
	}

	public static class MonthHandler extends AbstractDateRangeHandler {
		private int offset;
		public MonthHandler(int i) {
			super(Period.months(1));
			this.offset = i;
		}
		@Override
		public LocalDate getNowFrom() {
			return LocalDate.now().withDayOfMonth(1).plusMonths(offset);			
		}
	}

	public static class QuarterHandler extends AbstractDateRangeHandler {
		private int offset;

		public QuarterHandler(int i) {
			super(Period.months(3));
			this.offset = i;
		}
		@Override
		public LocalDate getNowFrom() {			
			LocalDate now = LocalDate.now();
			return now.plusMonths(3*offset).minusMonths((now.getMonthOfYear()-1)%3).withDayOfMonth(1);
		}
	}

	public static class YearHandler extends AbstractDateRangeHandler {
		private int offset;
		public YearHandler(int i) {
			super(Period.years(1));
			this.offset = i;
		}
		@Override
		public LocalDate getNowFrom() {			
			return LocalDate.now().plusYears(offset).withDayOfYear(1);
		}
	}
	
	public static class DayRangeHandler extends AbstractDateRangeHandler {
		public DayRangeHandler(int days) { 
			super(new Period().withDays(days));
		}
	}

	
}
