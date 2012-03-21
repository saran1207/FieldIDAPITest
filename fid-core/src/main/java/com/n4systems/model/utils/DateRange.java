package com.n4systems.model.utils;

import com.n4systems.util.chart.RangeType;
import com.n4systems.util.time.DateUtil;
import org.joda.time.*;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.io.Serializable;
import java.util.Date;


/**
 *  handles floating dates & static dates
 * e.g. LAST WEEK   or     jan 1-jan7    or  ALL TIME    or   jan1,1970-jan1,2099
 * @see com.n4systems.util.chart.RangeType
 * 
 * note that it is up to the user to decide if this represents an inclusive or exclusive range. 
 * does Jan1-Jan8 mean Jan1,2,3,4,5,6,7?   (one week before the 8th).     OR 
 *      Jan1-Jan8 mean Jan1,2,3,4,5,6,7,8?
 *      
 * this object is not aware of the context but you should know if you use LE or LT in queries.   
 * LT = use calculateToDate();  LE = use getInclusiveToDate()
 */
@SuppressWarnings("serial")
@Embeddable
public class DateRange implements Serializable, Cloneable {
	
	public static final Period OPEN_PERIOD = new Period().withYears(3000);  // can't use MAX_INT 'cause that causes overflow in certain scenarios.
	
    @Column(name="dateRange", nullable = false)
    @Enumerated(EnumType.STRING)
    private RangeType rangeType = RangeType.FOREVER;

    @Column
    private Date fromDate;

    @Column
    private Date toDate;

    public DateRange() {
    }

    public DateRange(LocalDate fromDate, LocalDate toDate) {
        this.fromDate = fromDate.toDate();
        this.toDate = toDate.toDate();
        this.rangeType = RangeType.CUSTOM;
    }

    public DateRange(RangeType rangeType) {
        this.rangeType = rangeType;
    }

    public static LocalDate earliestDate() {
        return DateUtil.getEarliestFieldIdDate();
    }
	
	public String getFromDateDisplayString() {
		return rangeType.getFormatter().getFromDateDisplayString(getFrom());
	}

	public String getToDateDisplayString() {
		return rangeType.getFormatter().getToDateDisplayString(getInclusiveTo());
	}
	
	// note that getTo() is exclusive.  e.g. for a year 2011
	// Jan 1, 2011 is getFrom() and getTo() is Jan 1, 2012  *not*  dec 31,2011    [11:59:59.9999...]
	// this method is used for display reasons.  (e.g.  "Searching oct 1,2011 - oct 31,2011")
	// @see getToDateDisplayString();
	private LocalDate getInclusiveTo() {
		LocalDate to = getTo();
		return (to==null) ? null : to.minusDays(1);
	}
	
	public Date getInclusiveToDate() {
		LocalDate to = getInclusiveTo();
		return (to==null) ? null : to.toDate();
	}

	public LocalDate getTo() {
		return getHandler().getNowTo();
	}

	public LocalDate getFrom() {
		return getHandler().getNowFrom();
	}
	
	private DateRangeHandler getHandler() {
		if (RangeType.CUSTOM.equals(rangeType)) {
			return new IntervalHandler(fromDate,toDate);
		} else { 
			return rangeType.getHandler();
		}
	}

	
	public Date calculateFromDate() {
		LocalDate from = getFrom();
		return from==null ? null : from.toDate();
	}

	// NOTE : static date ranges like LastWeek are inclusive.   i.e. From = Jan1, To=Jan8
	// but when the user enters custom dates like Jan1-Jan8 they want to include the last day. 
	public Date calculateToDate() {
		LocalDate to = getTo();
		if (rangeType.isPredefinedType()) {
			to = to.minusDays(1);
		}
		return to==null ? null : to.toDate();
	}

	public String getDisplayName() {
		return rangeType.getFormatter().getDisplayName(this);
	}
	
	public Period getPeriod() {
		return getHandler().getPeriod();
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

	public LocalDate getEarliest() {
		return getFrom()==null ? DateUtil.getEarliestFieldIdDate() : getFrom();
	}

	public LocalDate getLatest() {
		return getTo()==null ? DateUtil.getLatestFieldIdDate() : getTo();
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
	
	public static class DayHandler extends AbstractDateRangeHandler {
		private int offset;
		public DayHandler(int i) {
			super(Period.days(1));
			this.offset = i;
		}
		@Override
		public LocalDate getNowFrom() {
			return LocalDate.now().plusDays(offset);			
		}
	}

	public static class WeekHandler extends AbstractDateRangeHandler {
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

    public RangeType getRangeType() {
        return rangeType;
    }

    public void setRangeType(RangeType rangeType) {
        this.rangeType = rangeType;
    }

    /** warning : this method is here for hibernate/persistence only. end users of this object should use calculateFromDate()
    	because it will deal with nulls correctly.  
   		@see #getToDate()
     */
    @Deprecated  
    public Date getFromDate() {
        return fromDate;
    }

    public void setFromDate(Date fromDate) {
        this.fromDate = fromDate;
    }

    /** warning : this method is here for hibernate/persistence only. end users of this object should use calculateFromDate()
    	because it will deal with nulls correctly.  
   		@see #getFromDate()
     */
    @Deprecated  
    public Date getToDate() {
        return toDate;
    }

    public void setToDate(Date toDate) {
        this.toDate = toDate;
    }

    @Override
    public DateRange clone() throws CloneNotSupportedException {
        return (DateRange) super.clone();
    }


}
