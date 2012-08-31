package com.n4systems.model.utils;

import com.n4systems.util.chart.RangeType;
import org.apache.log4j.Logger;
import org.joda.time.*;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.io.Serializable;
import java.util.Date;
import java.util.TimeZone;


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
    
    @Deprecated // only required for hibernate/jpa reasons.  use other constructors.
    public DateRange() {
    }

    public DateRange(LocalDate fromDate, LocalDate toDate) {
        this.fromDate = fromDate.toDate();
        this.toDate = toDate.toDate();
        this.rangeType = RangeType.CUSTOM;
    }

    // it is preferred that you specify the timezone up front. it won't blow up if no timezone is specified as it will
    // use default but it's better to be explicit.
    @Deprecated
    public DateRange(RangeType rangeType) {
        this.rangeType = rangeType;
    }

    public DateRange(RangeType rangeType, TimeZone timeZone) {
        this.rangeType = rangeType;
        withTimeZone(timeZone);
    }

	public String getFromDateDisplayString() {
		return rangeType.getFormatter().getFromDateDisplayString(getFrom());
	}
    
	public String getToDateDisplayString() {
		return rangeType.getFormatter().getToDateDisplayString(getDisplayTo());
	}
	
	// note that getTo() is exclusive.  e.g. for a year 2011
	// Jan 1, 2011 is getFrom() and getTo() is Jan 1, 2012  *not*  dec 31,2011    [11:59:59.9999...]
	// this method is used for display reasons.  (e.g.  "Searching Oct 1,2011 - oct 31,2011")
	// @see getToDateDisplayString();
	private LocalDate getDisplayTo() {
		LocalDate to = getTo();
		return (to==null) ? null : to.minusDays(1);
	}
	
	public LocalDate getTo() {
		return getHandler().getNowTo();
	}

	public LocalDate getFrom() {
		return getHandler().getNowFrom();
	}

    // you must make sure you have set timezone before calling this to get correct results.
    // preferrable to use DateService.
    @Deprecated
    public Date calculateFromDate() {
		LocalDate from = getFrom();
		return from==null ? null : from.toDate();
	}

    // you must make sure you have set timezone before calling this to get correct results.
    // preferrable to use DateService.
    @Deprecated
    public Date calculateToDate() {
		LocalDate to = getTo();
		return to==null ? null : to.toDate();
	}

    public DateRange withTimeZone(TimeZone timeZone) {
        getHandler().setTimeZone(timeZone);
        return this;
    }

    private DateRangeHandler getHandler() {
		if (RangeType.CUSTOM.equals(rangeType)) {
			return new IntervalHandler(fromDate,toDate);
		} else {
			return rangeType.getHandler();
		}
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




    // ---------------------------------- internal handler impl classes -----------------------------------------------


	static abstract class AbstractDateRangeHandler implements DateRangeHandler {
        private static final Logger logger= Logger.getLogger(AbstractDateRangeHandler.class);
		private Period period;
        private TimeZone timeZone = null;

		public AbstractDateRangeHandler(Period period) { 
			this.period = period;
		}
		@Override public LocalDate getNowFrom() {
			return new LocalDate(today());
		}

        @Override public LocalDate getNowTo() {
			return getNowFrom().plus(period);
		}
		@Override public Period getPeriod() {
			return period;
		}

        public TimeZone getTimeZone() {
            return timeZone;
        }

        public void setTimeZone(TimeZone timeZone) {
            this.timeZone = timeZone;
        }

        protected DateMidnight today() {
            if (timeZone==null) {
                logger.error("calling date range without using timezone!  using default timezone instead.");
                return new DateMidnight(DateTimeZone.forTimeZone(TimeZone.getDefault()));
            }
            return new DateMidnight(DateTimeZone.forTimeZone(timeZone));
        }
    }
	
	public static class IntervalHandler implements DateRangeHandler {
		private LocalDate from;
		private LocalDate to;
        private TimeZone timeZone;

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

        @Override
        public void setTimeZone(TimeZone timeZone) {
            this.timeZone = timeZone; // irrelevant...user is specifying dates like Jan1-Feb 23.  not TZ's needed.
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
			return new LocalDate(today()).plusDays(offset);
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
			return new LocalDate(today()).withDayOfWeek(DateTimeConstants.MONDAY).plusWeeks(offset);
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
			return new LocalDate(today()).withDayOfMonth(1).plusMonths(offset);
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
            LocalDate now = new LocalDate(today());
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
			return new LocalDate(today()).plusYears(offset).withDayOfYear(1);
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

    public boolean isEmptyCustom() {
        return rangeType == RangeType.CUSTOM && fromDate == null && toDate == null;
    }

    @Override
    public String toString() {
        if (RangeType.CUSTOM.equals(rangeType)) {
            return "DateRange{" +
                    "fromDate=" + fromDate +
                    ", toDate=" + toDate +
                    '}';
        } else {
            return "DateRange{" +
                    rangeType.toString() +
                    '}';
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DateRange)) return false;

        DateRange dateRange = (DateRange) o;

        if (fromDate != null ? !fromDate.equals(dateRange.fromDate) : dateRange.fromDate != null) return false;
        if (rangeType != dateRange.rangeType) return false;
        if (toDate != null ? !toDate.equals(dateRange.toDate) : dateRange.toDate != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = rangeType != null ? rangeType.hashCode() : 0;
        result = 31 * result + (fromDate != null ? fromDate.hashCode() : 0);
        result = 31 * result + (toDate != null ? toDate.hashCode() : 0);
        return result;
    }
}
