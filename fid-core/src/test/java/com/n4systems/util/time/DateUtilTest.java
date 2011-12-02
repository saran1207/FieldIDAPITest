package com.n4systems.util.time;

import static org.junit.Assert.*;

import org.joda.time.DateTimeConstants;
import org.joda.time.LocalDate;
import org.junit.Test;


public class DateUtilTest {

	private LocalDate jan1 = new LocalDate(2011, 1,1);
	private LocalDate jan12 = new LocalDate(2011, 1,12);
	private LocalDate feb1 = new LocalDate(2011, 2,1);
	private LocalDate mar10 = new LocalDate(2011, 3,10);
	private LocalDate jun18 = new LocalDate(2011, 6,18);
	private LocalDate aug9 = new LocalDate(2011, 8,9);
	private LocalDate oct1 = new LocalDate(2011, 10,1);
	private LocalDate dec31 = new LocalDate(2011, 12,31);
	private LocalDate oct1_2012 = new LocalDate(2012, 10,1);
	
	
	@Test
	public void test_getMonthInQuarter() {		
		assertEquals(1, DateUtil.getMonthInQuarter(jan1));
		assertEquals(1, DateUtil.getMonthInQuarter(jan12));
		assertEquals(2, DateUtil.getMonthInQuarter(feb1));
		assertEquals(3, DateUtil.getMonthInQuarter(mar10));
		assertEquals(2, DateUtil.getMonthInQuarter(aug9));
		assertEquals(1, DateUtil.getMonthInQuarter(oct1));
		assertEquals(3, DateUtil.getMonthInQuarter(dec31));
		assertEquals(1, DateUtil.getMonthInQuarter(oct1_2012));
	}
	
	@Test
	public void test_quarterMonth() { 	
		assertEquals(DateTimeConstants.JANUARY, DateUtil.getQuarterMonth(jan12));
		assertEquals(DateTimeConstants.JULY, DateUtil.getQuarterMonth(aug9));
		assertEquals(DateTimeConstants.JANUARY, DateUtil.getQuarterMonth(mar10));
		assertEquals(DateTimeConstants.OCTOBER, DateUtil.getQuarterMonth(oct1_2012));
	}
	
	@Test
	public void test_getQuarter() {		
		assertEquals(1, DateUtil.getQuarter(jan1));
		assertEquals(1, DateUtil.getQuarter(jan12));
		assertEquals(1, DateUtil.getQuarter(feb1));
		assertEquals(1, DateUtil.getQuarter(mar10));
		assertEquals(2, DateUtil.getQuarter(jun18));
		assertEquals(3, DateUtil.getQuarter(aug9));
		assertEquals(4, DateUtil.getQuarter(oct1));
		assertEquals(4, DateUtil.getQuarter(dec31));
		assertEquals(4, DateUtil.getQuarter(oct1_2012));
	}
	
	@Test
	public void test_nullSafeMin() {
		Long x = 123L;
		Long y = 999L;
		Long Null = null;
		Long anotherNull = null;
		
		assertEquals(x, DateUtil.nullSafeMin(Null, x));
		assertEquals(x, DateUtil.nullSafeMin(x, Null));
		assertEquals(null, DateUtil.nullSafeMin(anotherNull, Null));
		assertEquals(x, DateUtil.nullSafeMin(x, x));
		assertEquals(y, DateUtil.nullSafeMin(y, y));
		assertEquals(x, DateUtil.nullSafeMin(y, x));
		assertEquals(x, DateUtil.nullSafeMin(x, y));
	}

	@Test
	public void test_nullSafeMax() {
		Long x = 123L;
		Long y = 999L;
		Long Null = null;
		Long anotherNull = null;
		
		assertEquals(x, DateUtil.nullSafeMax(Null, x));
		assertEquals(x, DateUtil.nullSafeMax(x, Null));
		assertEquals(null, DateUtil.nullSafeMax(anotherNull, Null));
		assertEquals(x, DateUtil.nullSafeMax(x, x));
		assertEquals(y, DateUtil.nullSafeMax(y, x));
		assertEquals(y, DateUtil.nullSafeMax(x, y));		
		
	}
	
}
