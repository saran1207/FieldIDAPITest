package com.n4systems.util.chart;

import static org.junit.Assert.*;

import java.util.Calendar;

import org.hamcrest.collection.IsIn;
import org.junit.Test;

import com.n4systems.util.time.DateUtil;


public class ChartDateRangeTest {
	 
	@Test
	public void testFromToDelta() {
		assertFromToDelta(ChartDateRange.THIS_WEEK, 7L);
		assertFromToDelta(ChartDateRange.THIS_MONTH, 28L, 29L, 30L, 31L);
		assertFromToDelta(ChartDateRange.THIS_QUARTER, 88L, 89L, 90L, 91L, 92L, 93L);
		assertFromToDelta(ChartDateRange.THIS_YEAR, 365L);
	}

	@Test
	public void testFrom() {		
		assertFrom(ChartDateRange.LAST_YEAR.getFromCalendar(), Calendar.YEAR, -1);
		assertFrom(ChartDateRange.THIS_YEAR.getFromCalendar(), Calendar.YEAR, 0);		
		assertFrom(ChartDateRange.LAST_MONTH.getFromCalendar(), Calendar.MONTH, -1);		
		assertFrom(ChartDateRange.THIS_MONTH.getFromCalendar(), Calendar.MONTH, 0);		
		assertFrom(ChartDateRange.LAST_WEEK.getFromCalendar(), Calendar.WEEK_OF_YEAR, -1);		
		assertFrom(ChartDateRange.THIS_WEEK.getFromCalendar(), Calendar.WEEK_OF_YEAR, 0);

		int expectedLastQuarterDeltas[] = {-3,-4,-5,-3,-4,-5,-3,-4,-5,-3,-4,-5};		
		assertCalendar(ChartDateRange.LAST_QUARTER.getFromCalendar(), Calendar.MONTH, expectedLastQuarterDeltas);		
		int expectedThisQuarterDeltas[] = {-0,-1,-2,0,-1,-2,0,-1,-2,0,-1,-2};
		assertCalendar(ChartDateRange.THIS_QUARTER.getFromCalendar(), Calendar.MONTH, expectedThisQuarterDeltas);		
	}

	@Test
	public void testTo() {		
		assertFrom(ChartDateRange.LAST_YEAR.getToCalendar(), Calendar.YEAR, 0);
		assertFrom(ChartDateRange.THIS_YEAR.getToCalendar(), Calendar.YEAR, 1);		
		assertFrom(ChartDateRange.LAST_MONTH.getToCalendar(), Calendar.MONTH, 0);		
		assertFrom(ChartDateRange.THIS_MONTH.getToCalendar(), Calendar.MONTH, 1);		
		assertFrom(ChartDateRange.LAST_WEEK.getToCalendar(), Calendar.WEEK_OF_YEAR, 0);		
		assertFrom(ChartDateRange.THIS_WEEK.getToCalendar(), Calendar.WEEK_OF_YEAR, 1);

		int expectedLastQuarterDeltas[] = {0,-1,-2,0,-1,-2,0,-1,-2,0,-1,-2};
		assertCalendar(ChartDateRange.LAST_QUARTER.getToCalendar(), Calendar.MONTH, expectedLastQuarterDeltas);		
		int expectedThisQuarterDeltas[] = {3,2,1,3,2,1,3,2,1,3,2,1};		
		assertCalendar(ChartDateRange.THIS_QUARTER.getToCalendar(), Calendar.MONTH, expectedThisQuarterDeltas);		
	}
	
	private void assertFrom(Calendar from, int calendarParam, int delta) {
		Calendar today = Calendar.getInstance();
		assertEquals(today.get(calendarParam) + delta, from.get(calendarParam));
	}

	private void assertCalendar(Calendar cal, int calendarParam, int[] delta) {
		Calendar today = Calendar.getInstance();
		int index = today.get(calendarParam);  
		int expected = (delta[index]+today.get(calendarParam))%delta.length;
		int actual = cal.get(calendarParam);
		assertEquals(expected, actual);
	}

	private void assertFromToDelta(ChartDateRange range, Long... expected) {
		Calendar from = range.getFromCalendar();
		Calendar to = range.getToCalendar();
		
		Long delta = 0L;
		delta = DateUtil.intervalInDays(to.getTimeInMillis() - from.getTimeInMillis());
		
		assertThat(delta, new IsIn<Long>(expected) );		
	}

}
