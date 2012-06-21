package com.n4systems.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.Map;
import java.util.TimeZone;
import java.util.TreeMap;

import com.n4systems.exceptions.InvalidArgumentException;
import com.n4systems.model.user.User;
import com.n4systems.model.utils.DateTimeDefiner;
import com.n4systems.model.utils.PlainDate;

@SuppressWarnings("serial")
public class DateHelper {
	public static final long millisPerDay = 86400000;
	public static final long millisPerMinute = 60000;

	/** Fields for truncate and increment methods */
	public static final int MILLISECOND = 0;
	public static final int SECOND = 1;
	public static final int MINUTE = 2;
	public static final int HOUR = 3;
	public static final int DAY = 4;
	public static final int WEEK = 5;
	public static final int MONTH = 6;
	public static final int YEAR = 7;

	/**
	 * Used to convert SimpleDateFormat formats into Unix style formats for use
	 * with the javascript Calendar widget
	 */
	private static final Map<String, String> java2unixConversions =
	/*
	 * We sort the map in descending order by length, since we need to do match
	 * the longest ones first. Otherwise MMMM could become %m%m rather then %B.
	 */
	new TreeMap<String, String>(new Comparator<String>() {
		@Override
		public int compare(String a, String b) {
			// sort by length in descending order
			return (a.length() > b.length()) ? -1 : 1;
		}
	}) {
		{
			/*
			 * This sets up the date format in this anonymous private method.
			 * Bet'ca didn't know you could do this did ya :)
			 * 
			 * See
			 * http://www.dynarch.com/demos/jscalendar/doc/html/reference.html
			 * for DHTML Calendar mappings See
			 * http://java.sun.com/j2se/1.5.0/docs/api/index.html for Java
			 * mappings
			 */
			put("MM", "%m");
			put("MMM", "%b");
			put("MMMM", "%B");
			put("MMMMM", "%B");
			put("EEE", "%a");
			put("EEEE", "%A");
			put("dd", "%d");
			put("yyyy", "%Y");
			put("yy", "%y");
			put("HH", "%H");
			put("mm", "%M");
			put("ss", "%S");
			put("a", "%P");
			put("aa", "%P");
			put("aaa", "%P");
			put("hh", "%I");
		}
	};

	@Deprecated
	public static Date str2SQLDate(String format, String dateStr) {
		return string2Date(format, dateStr);
	}

	@Deprecated
	public static String sqlDate2Str(String format, Date date) {
		return date2String(format, date);
	}

	public static Date string2DateTime(String format, String dateStr, TimeZone timeZone) {
		SimpleDateFormat formatter = new SimpleDateFormat(format);
        if (timeZone!=null) {
		    formatter.setTimeZone(timeZone);
        }
		String strDateString = dateStr;

		try {
			return formatter.parse(strDateString);
		} catch (ParseException e) {
			return null;
		}
	}

	public static Date string2Date(String format, String dateStr) {
		if (format == null || dateStr == null) {
			return null;
		}

		if (format.trim().length() == 0 || dateStr.trim().length() == 0) {
			return null;
		}

		SimpleDateFormat sdf = new SimpleDateFormat(format);

		Date date = null;
		try {
			date = sdf.parse(dateStr);
		} catch (ParseException parseEx) {
		}

		/**
		 * CAUTION DD : this method will not work properly for date strings with 2 digit years. 
		 *  e.g. 
		 * sdf.parse("19/08/11") does NOT equal sdf.parse("19/08/2011")    // you might think it would be 2011, but it interprets the first date as 0011
		 * 
		 * typically, this method is called with a string that is served up via the date picker so this edge case won't happen.  but if a user typed in that 
		 * string then the results would be very hard to debug. 
		 *  
		*/
		
		return date;
	}

	public static boolean isDateValid(String format, String dateStr) {
		if (format == null || dateStr == null) {
			return false;
		}

		boolean success = true;
		try {
			SimpleDateFormat sdf = new SimpleDateFormat(format);
			sdf.setLenient(false);
			sdf.parse(dateStr);
		} catch (ParseException p) {
			success = false;
		}

		return success;
	}

	public static String date2String(String format, Date time) {
		return date2String(format, time, TimeZone.getDefault());
	}

	public static String date2String(String format, Date date, TimeZone timeZone) {
		if (date == null)
			return "";

		SimpleDateFormat sdf = new SimpleDateFormat(format);
		sdf.setTimeZone(timeZone);

		return sdf.format(date);
	}

	private static Calendar getTodayCalendar() {
		Calendar today = Calendar.getInstance();
		return setToBeginningOfTheDay(today);
	}

	public static Date getFirstDayOfThisYear() {
		Calendar date = getTodayCalendar();
		return setToBeginningOfYear(date).getTime();
	}

	/**
	 * Returns a Date representing today's date as returned by
	 * {@link #getTodayCalendar()}.
	 * 
	 * @return A {@link Date} object, with all time fields rolled back to zero.
	 */
	public static Date getToday() {
		return getTodayCalendar().getTime();
	}

	public static Date getTodayWithTime() {
		Calendar today = Calendar.getInstance();
		return today.getTime();
	}

	public static boolean withinTheLastHour(Date target) {
		Date now = new Date();
		Date oneHourAgo = increment(now, HOUR, -1);

		return (target.before(now) && target.after(oneHourAgo));

	}
	
	public static boolean withinSevenDays(Date target){
		Date now = new Date();
		Date oneWeekAgo = increment(now, DAY, -7);
		
		return (target.before(now) && target.after(oneWeekAgo));
	}

	public static Date getYesterday() {
		return increment(getToday(), DAY, -1);
	}

	public static Date getTomorrow() {
		return increment(getToday(), DAY, 1);
	}

	@Deprecated
	public static Long millisToDays(Long millis) {
		return millis / millisPerDay;
	}
	
	
	public static Long millisToMinutes(Long millis) {
		return millis / millisPerMinute;
	}

	public static Date addDaysToDate(Date date, Long days) {
		return increment(date, DAY, days.intValue());
	}

	public static Long getDaysDelta(Date first, Date second) {
		return millisToDays(millisecondDifference(first, second));
	}

	private static long millisecondDifference(Date first, Date second) {
		return second.getTime() - first.getTime();
	}
	
	public static Long getMinutesDelta(Date first, Date second) {
		return millisToMinutes(millisecondDifference(first, second));
	}
 
	public static Long getDaysFromToday(Date date) {
		return getDaysDelta(getToday(), date);
	}

	public static Long getDaysUntilToday(Date date) {
		return getDaysDelta(date, getToday());
	}

	public static boolean isEqualIgnoringTime(Date date1, Date date2) {
		Date dateNoTime1 = getDateWithOutTime(date1);
		Date dateNoTime2 = getDateWithOutTime(date2);

		return dateNoTime1.equals(dateNoTime2);
	}

	/**
	 * Given a Date object, returns a Date representing only the Date portion
	 * (time portion is zeroed)
	 * 
	 * @see #setToBeginningOfTheDay(Calendar)
	 * @param date
	 *            A given date object
	 * @return The Date only portion of date
	 */
	public static Date getDateWithOutTime(Date date) {
		Calendar d = createCalendarForDate(date);
		return setToBeginningOfTheDay(d).getTime();
	}

	public static Date getBeginingOfDay(Date date) {
		return getDateWithOutTime(date);
	}

	public static Date getEndOfDay(Date date) {
		Calendar d = createCalendarForDate(date);
		setToBeginningOfTheDay(d);
		
		d.set(Calendar.HOUR_OF_DAY, 23);
		d.set(Calendar.MINUTE, 59);
		d.set(Calendar.SECOND, 59);
		d.set(Calendar.MILLISECOND, 999);
		
		return d.getTime();
	}

	private static Calendar createCalendarForDate(Date date) {
		Calendar d = Calendar.getInstance();
		d.setTime(date);
		return d;
	}

	/**
	 * Given a Calendar object, zeros out the hour, min, second and millisecond
	 * fields. Aka returns only the day, month, year portion of the date.
	 * 
	 * @param date
	 *            A Calendar date object
	 * @return The Date only representation of the given calendar object
	 */
	private static Calendar setToBeginningOfTheDay(Calendar date) {
		truncate(date, DAY);
		truncate(date, HOUR);
		truncate(date, MINUTE);
		truncate(date, SECOND);
		truncate(date, MILLISECOND);
		return date;
	}

	private static Calendar setToBeginningOfYear(Calendar date) {
		date.set(Calendar.DAY_OF_YEAR, 1);
		return date;

	}

	/**
	 * Converts SimpleDateFormat formats into Unix style date formats
	 * 
	 * @param format
	 *            A SimpleDateFormat string date format
	 * @return A Unix style date format
	 */
	public static String java2Unix(String format) {
		String workingFormat = new String(format);
		for (Map.Entry<String, String> entry : java2unixConversions.entrySet()) {
			workingFormat = workingFormat.replace(entry.getKey(), entry.getValue());
		}

		return workingFormat;
	}

	public static Date convertToUserTimeZone(Date date, TimeZone timeZone) {
		return convertTimeZone(date, timeZone, TimeZone.getDefault());
	}

	private static Date convertTimeZone(Date date, TimeZone inputTimeZone, TimeZone outputTimeZone) {
		if (date == null) {
			return null;
		}

		if (date instanceof PlainDate) {
			throw new InvalidArgumentException("You are trying to convert the time zone of a plain date you should never do this.");
		}

		Calendar first = Calendar.getInstance(inputTimeZone);
		first.setTimeInMillis(date.getTime());

		// creates a date that is assumed to now be in the given time zone
		// instead of the default one.
		Calendar output = Calendar.getInstance(outputTimeZone);
		output.set(Calendar.YEAR, first.get(Calendar.YEAR));
		output.set(Calendar.MONTH, first.get(Calendar.MONTH));
		output.set(Calendar.DAY_OF_MONTH, first.get(Calendar.DAY_OF_MONTH));
		output.set(Calendar.HOUR_OF_DAY, first.get(Calendar.HOUR_OF_DAY));
		output.set(Calendar.MINUTE, first.get(Calendar.MINUTE));
		output.set(Calendar.SECOND, first.get(Calendar.SECOND));
		output.set(Calendar.MILLISECOND, first.get(Calendar.MILLISECOND));

		return output.getTime();
	}

	public static Date convertToUTC(Date date, TimeZone timeZone) {
		return convertTimeZone(date, TimeZone.getDefault(), timeZone);
	}

	public static String getTimeZoneShortName(Date date, TimeZone zone) {
		return zone.getDisplayName(zone.inDaylightTime(date), TimeZone.SHORT);
	}

	/**
	 * Truncates a date to the precision of the requested calendar field. Sets
	 * all smaller fields to their base value.<br />
	 * Examples:
	 * 
	 * <pre>
	 * date is 'Wednesday May 27, 2009 15:50:32.234'
	 * 
	 * 	DateHelper.truncate(date, DateHelper.MILLISECOND);	// returns Wednesday May 27, 2009 15:50:32.234 (no effect)
	 * 	DateHelper.truncate(date, DateHelper.SECOND);		// returns Wednesday May 27, 2009 15:50:32.0
	 * 	DateHelper.truncate(date, DateHelper.MINUTE);		// returns Wednesday May 27, 2009 15:50:00.0
	 * 	DateHelper.truncate(date, DateHelper.HOUR);		// returns Wednesday May 27, 2009 15:00:00.0
	 * 	DateHelper.truncate(date, DateHelper.DAY);		// returns Wednesday May 27, 2009 00:00:00.0
	 * 	DateHelper.truncate(date, DateHelper.WEEK);		// returns Sunday May 24, 2009 00:00:00.0
	 * 	DateHelper.truncate(date, DateHelper.MONTH);		// returns Friday May 01, 2009 00:00:00.0
	 * 	DateHelper.truncate(date, DateHelper.YEAR);		// returns Thursday January 01, 2009 00:00:00.0
	 * </pre>
	 * 
	 * @param date
	 * @param calendarField
	 * @return
	 */
	public static Date truncate(Date date, int field) {
		Calendar cal = createCalendarForDate(date);

		truncate(cal, field);

		return cal.getTime();
	}

	private static void truncate(Calendar cal, int field) {
		// Each field operates on the next smaller field and continues down the
		// chain.
		switch (field) {
		case YEAR:
			cal.set(Calendar.MONTH, Calendar.JANUARY);
		case MONTH:
		case WEEK:
			/*
			 * WEEK and MONTH both operate on the day (can't be chained
			 * together), so we'll handle them in the same block to allow the
			 * chaining to flow through.
			 */
			if (field == WEEK) {
				cal.set(Calendar.DAY_OF_WEEK, cal.getFirstDayOfWeek());
			} else {
				// if the YEAR or higher was specified, we need to zero out the
				// day rather then the week
				cal.set(Calendar.DAY_OF_MONTH, 1);
			}
		case DAY:
			cal.set(Calendar.HOUR_OF_DAY, 0);
		case HOUR:
			cal.set(Calendar.MINUTE, 0);
		case MINUTE:
			cal.set(Calendar.SECOND, 0);
		case SECOND:
			cal.set(Calendar.MILLISECOND, 0);
		case MILLISECOND:
			// Date use millisecond as the base value so this does nothing
			break;
		default:
			throw new IllegalArgumentException("Invalid date field: " + field);
		}
	}

	/**
	 * Adds (or subtracts when negative) a specified amount to the field of the
	 * given date.
	 * 
	 * @param date
	 *            Date to start field calculation from
	 * @param field
	 *            Field to operate on
	 * @param ammount
	 *            Amount to increment/decrement by
	 * @return Date post field incrementation
	 */
	public static Date increment(Date date, int field, int amount) {
		Calendar cal = createCalendarForDate(date);

		switch (field) {
		case YEAR:
			cal.add(Calendar.YEAR, amount);
			break;
		case MONTH:
			cal.add(Calendar.MONTH, amount);
			break;
		case WEEK:
			cal.add(Calendar.WEEK_OF_MONTH, amount);
			break;
		case DAY:
			cal.add(Calendar.DAY_OF_MONTH, amount);
			break;
		case HOUR:
			cal.add(Calendar.HOUR_OF_DAY, amount);
			break;
		case MINUTE:
			cal.add(Calendar.MINUTE, amount);
			break;
		case SECOND:
			cal.add(Calendar.SECOND, amount);
			break;
		case MILLISECOND:
			cal.add(Calendar.MILLISECOND, amount);
			break;
		default:
			throw new IllegalArgumentException("Invalid date field: " + field);
		}

		return cal.getTime();
	}

	public static String format(Date date, DateTimeDefinition dateTimeDefinition) {
		return new FieldidDateFormatter(date, dateTimeDefinition).format();
	}
	
	public static String getFormattedCurrentDate(User user) {
		return format(new PlainDate(), new DateTimeDefiner(user));
	}
	
	public static String getFormattedCurrentDateTime(User user) {
		return format(new Date(), new DateTimeDefiner(user));
	}
	
	public static int getThisYear() {
		return getTodayCalendar().get(Calendar.YEAR);
	}
	
	public static int getThisMonth() {
		return getTodayCalendar().get(Calendar.MONTH);
	}
	
	public static Date delocalizeDate(Date date, TimeZone fromZone) {
		Calendar cal = createCalendarForDate(date);
		cal.add(Calendar.MILLISECOND, fromZone.getOffset(date.getTime()) * -1);
		return cal.getTime();
	}

	public static Date localizeDate(Date date, TimeZone toZone) {
		Calendar cal = createCalendarForDate(date);
		cal.add(Calendar.MILLISECOND, toZone.getOffset(date.getTime()));
		return cal.getTime();
	}
	
	public static boolean isValidExcelDateString(String date) { 
		try { 
			Long ms = Long.parseLong(date);
			return ms > 0;
		} catch (NumberFormatException e) { 
			return false;
		}
	}
}
