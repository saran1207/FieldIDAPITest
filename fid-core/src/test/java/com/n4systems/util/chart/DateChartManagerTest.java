package com.n4systems.util.chart;

import static org.junit.Assert.*;

import java.util.ArrayList;

import com.n4systems.model.utils.DateRange;
import org.joda.time.DateTimeConstants;
import org.joda.time.DateTimeUtils;
import org.joda.time.LocalDate;
import org.joda.time.Period;
import org.junit.Test;


public class DateChartManagerTest {
	
	private DateChartManager dateChartManager;
	private LocalDate jan1_2011 = new LocalDate().withYear(2011).withMonthOfYear(DateTimeConstants.JANUARY).withDayOfMonth(1);
	private LocalDate feb28_2011 = new LocalDate().withYear(2011).withMonthOfYear(DateTimeConstants.FEBRUARY).withDayOfMonth(28);
	private LocalDate nov16_2022 = new LocalDate().withYear(2022).withMonthOfYear(DateTimeConstants.NOVEMBER).withDayOfMonth(16);
	
	
	@Test 
	public void test_normalize_forever() {
		DateTimeUtils.setCurrentMillisFixed(jan1_2011.toDate().getTime());
		
        DateRange dateRange = new DateRange(RangeType.FOREVER);

		for (ChartGranularity granularity:ChartGranularity.values()) {
			dateChartManager = new DateChartManager(granularity, dateRange);
		
			// |0....5....10| are given (3 in total).  
			// padded to include all the others for a total of 11 
			assertSeries(11, 3, 0, 5);
			
			// |..2.....7....12| are given (3 in total). 
			//padded to include all the others. 
			assertSeries(13, 3, 2, 5);
		}	
	}
	
	@Test 
	public void test_normalize_thisyear() {
		DateTimeUtils.setCurrentMillisFixed(jan1_2011.toDate().getTime());
        RangeType bleh = RangeType.CUSTOM;
		
        DateRange dateRange = new DateRange(RangeType.THIS_YEAR);

		dateChartManager = new DateChartManager(ChartGranularity.YEAR, dateRange);
		assertSeries(1, 1, 0, 1);  // one point max for viewing yearly data with year date range.
	
		dateChartManager = new DateChartManager(ChartGranularity.QUARTER, dateRange);
		assertSeries(4, 4, 0, 1);
		assertSeries(4, 1, 2, 1);
	
		dateChartManager = new DateChartManager(ChartGranularity.MONTH, dateRange);
		assertSeries(12, 5, 0, 2);
		assertSeries(12, 3, 2, 1);
	
		dateChartManager = new DateChartManager(ChartGranularity.WEEK, dateRange);
		assertSeries(53, 5, 0, 2);
		assertSeries(53, 3, 2, 1);
	
		dateChartManager = new DateChartManager(ChartGranularity.DAY, dateRange);
		assertSeries(365, 5, 0, 2);
		assertSeries(365, 3, 2, 1);
	
	}
	
	@Test 
	public void test_normalize_thisquarter() {
		DateTimeUtils.setCurrentMillisFixed(jan1_2011.toDate().getTime());
		
        DateRange dateRange = new DateRange(RangeType.THIS_QUARTER);

		dateChartManager = new DateChartManager(ChartGranularity.QUARTER, dateRange);
		assertSeries(1, 0, 0, 1);
		assertSeries(1, 1, 0, 1);
	
		dateChartManager = new DateChartManager(ChartGranularity.MONTH, dateRange);
		assertSeries(3, 4, 0, 2);
		assertSeries(3, 1, 2, 1);
	
		dateChartManager = new DateChartManager(ChartGranularity.WEEK, dateRange);
		assertSeries(14, 4, 0, 2);  // 14 weeks in the particular jan/feb/mar quarter we are looking at.
		assertSeries(14, 3, 2, 1);
	
		dateChartManager = new DateChartManager(ChartGranularity.DAY, dateRange);
		assertSeries(90, 5, 0, 2);   // 90 days in jan/feb/mar quarter.
		assertSeries(90, 3, 2, 1);
	
	}
	
	@Test 
	public void test_normalize_thismonth() {
		DateTimeUtils.setCurrentMillisFixed(jan1_2011.toDate().getTime());
		
        DateRange dateRange = new DateRange(RangeType.THIS_MONTH);
	
		dateChartManager = new DateChartManager(ChartGranularity.MONTH, dateRange);
		assertSeries(1, 1);
		assertSeries(1, 0);
	
		dateChartManager = new DateChartManager(ChartGranularity.WEEK, dateRange);
		// WEIRDNESS EXPLAINED.  yes, that's right. there are 6 weeks in jan 2011.
		// check the calendar.  since a week is defined as starting on monday, the first week (the one that contains jan 1st) is dec 27-jan2.
		// then weeks ending on 9, 16, 23 & 30th. another (the 6th) partial week begins on monday jan 31st.
		// so this test will probably (hopefully) break if you change the definition of "when does a week start".  because if you do it  
		// will negatively impact the app.
		assertSeries(6, 4, 0, 2);  
	
		dateChartManager = new DateChartManager(ChartGranularity.DAY, dateRange);
		assertSeries(31, 5, 0, 2);   
		assertSeries(31, 3, 2, 1);	
	}

	
	@Test 
	public void test_normalize_thisweek() {
		DateTimeUtils.setCurrentMillisFixed(jan1_2011.toDate().getTime());
		
        DateRange dateRange = new DateRange(RangeType.THIS_WEEK);
	
		dateChartManager = new DateChartManager(ChartGranularity.WEEK, dateRange);
		assertSeries(1, 0);  
		assertSeries(1, 1);  
	
		dateChartManager = new DateChartManager(ChartGranularity.DAY, dateRange);
		assertSeries(7, 3, 0, 2);   
		assertSeries(7, 3, 2, 1);	
	}

	
	
	private void assertSeries(int size, int numberOfPoints) {
		assertSeries(size, numberOfPoints, 0, 1);
	}
	
	private void assertSeries(int size, int numberOfPoints, int offset, int periodMultiplier) {
		DateRange dateRange = dateChartManager.getDateRange();
		assertSeries(dateRange.getEarliest(), dateRange.getLatest(), size, numberOfPoints, offset, periodMultiplier);
	}
	
	private void assertSeries(LocalDate from, LocalDate to, int size, int numberOfPoints, int offset, int periodMultiplier) {
		DateRange dateRange = dateChartManager.getDateRange();
		ChartGranularity granularity = dateChartManager.getGranularity();
		
		ChartSeries<LocalDate> series = createTestSeries(granularity, from, to, offset, periodMultiplier, numberOfPoints);
		// create series with original numberOfPoints, the normalize() which will add padding (i.e. more 0 value points). 
		// .: end size of series will be >= original.
		series = dateChartManager.normalize(series);

		assertEquals(size, series.size());

		Period offsetPeriod = multiplyPeriod(offset, granularity.getPeriod());
		Period periodBetweenPoints = multiplyPeriod(periodMultiplier, granularity.getPeriod());  // the points we defined. (skips the padded ones).
		
		LocalDate nextCreatedPoint = granularity.normalize(dateRange.getEarliest()).plus(offsetPeriod);
		LocalDate start = granularity.normalize(dateRange.getEarliest());
		LocalDate expect = start;
		long createdPoints = 0;
		while (expect.compareTo(series.getLastX())<=0) {
			// all padded data will have value of ZERO.
			if (nextCreatedPoint.equals(expect) && createdPoints<numberOfPoints) {
				assertEquals(100L+createdPoints, series.get(expect).getY());
				createdPoints++;
				nextCreatedPoint = nextCreatedPoint.plus(periodBetweenPoints);
			} else { 
				assertEquals(0L, series.get(expect).getY());
			}
			expect = granularity.next(expect);
		}		
	}

	private Period multiplyPeriod(int multiplier, Period period) {
		// CAVEAT : this is not production code.  just good enough for testing. makes assumption that only one of these fields is non-zero.
		return period.withDays(period.getDays()*multiplier).withWeeks(period.getWeeks()*multiplier).withMonths(period.getMonths()*multiplier).withYears(period.getYears()*multiplier);
	}

	private ChartSeries<LocalDate> createTestSeries(ChartGranularity granularity, LocalDate from, LocalDate to, int offset, int period, int count) {
		ChartSeries<LocalDate> series = new ChartSeries<LocalDate>(new ArrayList<DateChartable>());
		LocalDate firstDate = granularity.normalize(advance(from,granularity, offset));
		LocalDate lastDate = to;
		LocalDate d = firstDate;
		for (int i=0; i<count; i++) {
			if (d.isAfter(lastDate)) {
				System.out.println("can't create test series out of bounds of date range " + d + " is after end date of " + lastDate + " for date range " + dateChartManager.getDateRange() + " with granularity " + granularity);
				System.out.println("skipping rest of data creation and returning data of size " + series.size());
				return series;
			}
			series.add(new DateChartable(d,100L+i));
			d = advance(d,granularity, period);
		}
		return series;
	}

	private LocalDate advance(LocalDate date, ChartGranularity granularity, int period) {
		LocalDate advanced=date;
		for (int p=0; p<period; p++) {
			advanced = granularity.next(advanced);			
		}
		return advanced;
		
	}

}
