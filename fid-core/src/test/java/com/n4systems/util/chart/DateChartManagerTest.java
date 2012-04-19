package com.n4systems.util.chart;

import com.n4systems.fieldid.FieldIdUnitTest;
import com.n4systems.model.utils.DateRange;
import org.joda.time.DateTimeConstants;
import org.joda.time.DateTimeUtils;
import org.joda.time.LocalDate;
import org.joda.time.Period;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;


public class DateChartManagerTest extends FieldIdUnitTest {
	
	private DateChartManager dateChartManager;
    private LocalDate jan1_2007 = new LocalDate().withYear(2007).withMonthOfYear(DateTimeConstants.JANUARY).withDayOfMonth(1);
    private LocalDate jan1_2011 = jan1_2007.plusYears(4);
    private LocalDate jan1_2012 = jan1_2011.plusYears(1);
    private LocalDate jan1_2013 = jan1_2012.plusYears(1);
    private LocalDate jan1_2016 = jan1_2013.plusYears(3);
	private LocalDate feb28_2011 = new LocalDate().withYear(2011).withMonthOfYear(DateTimeConstants.FEBRUARY).withDayOfMonth(28);
	private LocalDate nov16_2022 = new LocalDate().withYear(2022).withMonthOfYear(DateTimeConstants.NOVEMBER).withDayOfMonth(16);
    private LocalDate firstWeekIn2011 = new LocalDate().withWeekOfWeekyear(1).withYear(2011).withDayOfWeek(DateTimeConstants.MONDAY);  // lies on a weekly boundary.
    private LocalDate lastWeekOf2010 = firstWeekIn2011.minusWeeks(1);
    private ChartSeries<LocalDate> emptySeries = new ChartSeries<LocalDate>(new ArrayList<Chartable<LocalDate>>());

    int expected = 1;
    private int testValue;
    private Period yearlyPeriod=new Period().withYears(1);

    @Test
	public void test_normalize_forever() {
		setCurrentMillisFixed(jan1_2011.toDate().getTime());
		
        DateRange dateRange = new DateRange(RangeType.FOREVER);

        LocalDate from = dateRange.getFrom();
        LocalDate to = dateRange.getTo();
        
        dateChartManager = new DateChartManager(ChartGranularity.YEAR, dateRange);

        ChartSeries<LocalDate> series = createTestSeries(jan1_2011);  // single pt series starting at jan1.
        assertSeries(series, from, to, expected=7);                 // no padding, just single data pt.
        assertSeries(emptySeries, from, to, expected=0);

        dateChartManager = new DateChartManager(ChartGranularity.QUARTER, dateRange);
        series = createTestSeries(jan1_2011);
        assertSeries(series, from, to, expected=6*4+1);  // will start at 2005 and end at jan1_2011.  (i.e. ends at max pt).
        series = createTestSeries(jan1_2011.plusMonths(3));
        assertSeries(series, from, to, expected=6*4+2);  // will start at 2005 and end at apr1_2011.  (i.e. ends at max pt).

        dateChartManager = new DateChartManager(ChartGranularity.MONTH, dateRange);
        series = createTestSeries(jan1_2011);
        assertSeries(series, from, to, expected=6*12+1);
        series = createTestSeries(jan1_2011.plusMonths(1));
        assertSeries(series, from, to, expected=6*12+2);

        dateChartManager = new DateChartManager(ChartGranularity.WEEK, dateRange);
        series = createTestSeries(firstWeekIn2011);
        assertSeries(series, from, to, expected=6*52+2+1);
        series = createTestSeries(firstWeekIn2011.plusWeeks(1));
        assertSeries(series, from, to, expected=6*52+2+2);

        dateChartManager = new DateChartManager(ChartGranularity.DAY, dateRange);
        series = createTestSeries(jan1_2011);
        assertSeries(series, from, to, expected=6*365+2); // add a couple for leap year and inclusion of jan1,2011.
	}
	
	@Test 
	public void test_normalize_thisyear() {
		DateTimeUtils.setCurrentMillisFixed(jan1_2011.toDate().getTime());
        DateRange dateRange = new DateRange(RangeType.THIS_YEAR);
        
        dateChartManager = new DateChartManager(ChartGranularity.YEAR, dateRange);

        ChartSeries<LocalDate> series = createTestSeries(jan1_2011);  // single pt series starting at jan1.
        assertSeries(series, jan1_2011, jan1_2012, expected=1);                 // no padding, just single data pt.
        assertSeries(series, jan1_2011, jan1_2016, expected=6);    // one initial pt + 5 padded pts.  (2011,12,13,14,15,16)
        assertSeries(series, jan1_2007, jan1_2016, expected=10);    // 2007...2011....2016   
        assertSeries(emptySeries, jan1_2007, jan1_2011, expected=5);
        assertSeries(createTestSeries(jan1_2007,4,yearlyPeriod), jan1_2011, jan1_2016, expected=10);    // 2007...2011....2016

        dateChartManager = new DateChartManager(ChartGranularity.QUARTER, dateRange);
        series = createTestSeries(jan1_2011);
        assertSeries(series, jan1_2007, jan1_2011, expected=20);  // 5 years X 4 quarters per year.

		dateChartManager = new DateChartManager(ChartGranularity.MONTH, dateRange);
        series = createTestSeries(jan1_2011);
        assertSeries(series, jan1_2011, jan1_2012, expected=12);

		dateChartManager = new DateChartManager(ChartGranularity.WEEK, dateRange);
        series = createTestSeries(firstWeekIn2011);
        assertSeries(series, jan1_2011, jan1_2012, expected=53);  // recall weeks can span year boundaries.  2011 has 54. (one is partial).

		dateChartManager = new DateChartManager(ChartGranularity.DAY, dateRange);
        series = createTestSeries(jan1_2011);
        assertSeries(series, jan1_2011, jan1_2012, expected=365);
	}

    @Test
	public void test_normalize_thisquarter() {
        DateTimeUtils.setCurrentMillisFixed(jan1_2011.toDate().getTime());
        DateRange dateRange = new DateRange(RangeType.THIS_QUARTER);

        LocalDate from = dateRange.getFrom();
        LocalDate to = dateRange.getTo();

        dateChartManager = new DateChartManager(ChartGranularity.QUARTER, dateRange);
        ChartSeries<LocalDate> series = createTestSeries(jan1_2011);
        assertSeries(series, from, to, expected=1);

        dateChartManager = new DateChartManager(ChartGranularity.MONTH, dateRange);
        series = createTestSeries(jan1_2011);
        assertSeries(series, from, to, expected=3);

        dateChartManager = new DateChartManager(ChartGranularity.WEEK, dateRange);
        series = createTestSeries(firstWeekIn2011);
        assertSeries(series, from, to, expected=14);

        dateChartManager = new DateChartManager(ChartGranularity.DAY, dateRange);
        series = createTestSeries(jan1_2011);
        assertSeries(series, from, to, expected=90);
	}
	
	@Test 
	public void test_normalize_thismonth() {
        DateTimeUtils.setCurrentMillisFixed(jan1_2011.toDate().getTime());
        DateRange dateRange = new DateRange(RangeType.THIS_MONTH);

        LocalDate from = dateRange.getFrom();
        LocalDate to = dateRange.getTo();

        dateChartManager = new DateChartManager(ChartGranularity.MONTH, dateRange);
        ChartSeries<LocalDate> series = createTestSeries(jan1_2011);
        assertSeries(series, from, to, expected=1);

        dateChartManager = new DateChartManager(ChartGranularity.WEEK, dateRange);
        series = createTestSeries(firstWeekIn2011);
		// WEIRDNESS EXPLAINED.  yes, that's right. there are 6 weeks in jan 2011.
		// check the calendar.  since a week is defined as starting on monday, the first week (the one that contains jan 1st) is dec 27-jan2.
		// then weeks ending on 9, 16, 23 & 30th. another (the 6th) partial week begins on monday jan 31st.
		// so this test will probably (hopefully) break if you change the definition of "when does a week start".  because if you do it
		// will negatively impact the app.
        assertSeries(series, from, to, expected=6);

        dateChartManager = new DateChartManager(ChartGranularity.DAY, dateRange);
        series = createTestSeries(jan1_2011);
        assertSeries(series, from, to, expected=31);
	}
	
	@Test 
	public void test_normalize_thisweek() {
        DateTimeUtils.setCurrentMillisFixed(jan1_2011.toDate().getTime());
        DateRange dateRange = new DateRange(RangeType.THIS_WEEK);

        LocalDate from = dateRange.getFrom();
        LocalDate to = dateRange.getTo();

        dateChartManager = new DateChartManager(ChartGranularity.WEEK, dateRange);
        ChartSeries<LocalDate> series = createTestSeries(lastWeekOf2010);
        assertSeries(series, from, to, expected=1);

        dateChartManager = new DateChartManager(ChartGranularity.DAY, dateRange);
        series = createTestSeries(jan1_2011);
        assertSeries(series, from, to, expected=7);
	}
	
	@Test 
	public void test_normalize_daily() {
		setCurrentMillisFixed(jan1_2011.toDate().getTime());

        DateRange dateRange = new DateRange(RangeType.TODAY);

        LocalDate from = dateRange.getFrom();
        LocalDate to = dateRange.getTo();

		dateChartManager = new DateChartManager(ChartGranularity.DAY, dateRange);
        ChartSeries<LocalDate> series = createTestSeries(jan1_2011);
        assertSeries(series, from, to, expected = 1);
	}	
	
	
    private void assertSeries(ChartSeries<LocalDate> series, LocalDate from, LocalDate to, int expected) {
        DateRange dateRange = dateChartManager.getDateRange();
        ChartGranularity granularity = dateChartManager.getGranularity();

        // first figure out which points are currently in series (they should remain there after normalizing but new 0 values will also exist).
        Map<LocalDate, Number> originalData = new HashMap<LocalDate,Number>();
        for (Map.Entry<LocalDate, Chartable<LocalDate>> entry:series.getEntrySet()) {
            Chartable<LocalDate> chartable = entry.getValue();
            originalData.put(chartable.getX(), chartable.getY());
        }
        
        // create series with original numberOfPoints, the normalize() which will add padding (i.e. more 0 value points).
        // .: end size of series will be >= original.
        series = dateChartManager.normalize(series, from, to);

        // sanity check...should be the same size.
        assertEquals(expected, series.size());
        
        // now to confirm that the padding is working.
        // if we are given data    |..A....B....C..| then we should expect a series that is padded with the same start,end
        //                       start            end
        //   -->
        //                         |00A0000B0000C00|    note that the starting and ending points are inclusive and will be padded if no values exist.
        //                       start            end

        LocalDate expect = series.getFirstX();        
        
        while (expect != null && expect.compareTo(series.getLastX())<=0) {
            // all padded data will have value of ZERO.
            if (originalData.get(expect)!=null) {
                assertEquals(originalData.get(expect), series.get(expect).getY());
            } else {
                // then a chartable with value 0 should exist.   i.e. padded by normalize method.
                assertNotNull(series.get(expect));
                assertEquals(0L, series.get(expect).getY());
            }
            expect = granularity.next(expect);
        }

    }

    private Long getNextTestValue() {
        return 100L+(testValue++);
    }

    private void replayTestValues() {
        testValue = 0;
    }
    
    private Period multiplyPeriod(int multiplier, Period period) {
		// CAVEAT : this is not production code.  just good enough for testing. makes assumption that only one of these fields is non-zero.
		return period.withDays(period.getDays()*multiplier).withWeeks(period.getWeeks()*multiplier).withMonths(period.getMonths()*multiplier).withYears(period.getYears()*multiplier);
	}

    private ChartSeries<LocalDate> createTestSeries(LocalDate... dates) {
        ChartSeries<LocalDate> series = new ChartSeries<LocalDate>(new ArrayList<DateChartable>());
        replayTestValues();
        for (LocalDate date:dates) {
            LocalDate normalizedDate = dateChartManager.getGranularity().normalize(date);
            if (!normalizedDate.equals(date)) {
                System.out.println("suggested that the date " + date + " be switched to " + normalizedDate);
                throw new RuntimeException("the test data you are creating seems odd...the value doesn't lie on a proper boundary.  e.g. if creating monthly series, the date should be the first day.  ie. May 1, NOT May 3");
            }
            series.add(new DateChartable(date,getNextTestValue()));
        }
        return series;
    }

    private ChartSeries<LocalDate> createTestSeries(LocalDate date, int numberPoints, Period period) {
        ChartSeries<LocalDate> series = new ChartSeries<LocalDate>(new ArrayList<DateChartable>());
        replayTestValues();
        for (int i=0;i<numberPoints;i++) {
            series.add(new DateChartable(date,getNextTestValue()));
            date = date.plus(period);
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
