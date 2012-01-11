package com.n4systems.model.utils;

import java.io.Serializable;
import java.util.Date;

import org.joda.time.DateTimeConstants;
import org.joda.time.LocalDate;
import org.joda.time.Period;
import org.joda.time.ReadablePeriod;


/* handles floating dates & static dates
 * e.g. LAST WEEK   or     jan 1-jan7    or  ALL TIME    or   jan1,1970-jan1,2099
 * @see ChartDateRange
 */
@SuppressWarnings("serial")
public class DateRange implements Serializable {

	private DateRangeFormatter formatter = new DefaultDateRangeFormatter("MMM d yyyy");
	private DateRangeHandler handler;
	protected LocalDate from;
	protected LocalDate to;
	
	public DateRange(DateRangeHandler handler, DateRangeFormatter formatter) {
		this.handler = handler;
		this.formatter = formatter;
	}
	
	public DateRange(int days, DateRangeFormatter formatter) { 
		this(new DayRangeHandler(days),formatter);		
	}
	
	public DateRange(LocalDate from, LocalDate to) { 
		this(new IntervalHandler(from,to),null);
	}	
	
	public DateRange(DateRangeFormatter formatter) {
		this(new IntervalHandler(null,null),formatter);
	}

	public DateRange() { 
		this(new IntervalHandler(null,null),null);
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
		return to!=null ? to : 	handler.getNowTo();		
	}

	public LocalDate getFrom() {
		return (from!=null) ? from : handler.getNowFrom();
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
	
//	public String getId() { 
//		return formatter.getId(this);
//	}
		
	
	
	// ---------------------------------- internal classes -----------------------------------------------

	






	static abstract class AbstractDateRangeHandler implements DateRangeHandler {
		private ReadablePeriod period;

		public AbstractDateRangeHandler(Period period) { 
			this.period = period;
		}
		@Override
		public LocalDate getNowFrom() {
			return LocalDate.now();
		}
		@Override
		public LocalDate getNowTo() {
			return getNowFrom().plus(period);			
		}		
	}
	
	public static class IntervalHandler implements DateRangeHandler {
		private LocalDate from;
		private LocalDate to;

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
	}
	
	public static class NowHandler extends AbstractDateRangeHandler {
		public NowHandler(Period period) {
			super(period);			
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
