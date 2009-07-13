package com.n4systems.util;

import static org.junit.Assert.*;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import org.junit.Before;
import org.junit.Test;

public class DateHelperTest {

	
	@Before
	public void setup() {
		TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
	}
	
	@Test
	public void should_convert_to_utc_time() {
		Calendar date = Calendar.getInstance();
		date.set(Calendar.YEAR, 2009);  
		date.set(Calendar.MONTH, 0);  
		date.set(Calendar.DAY_OF_MONTH, 1);  
		date.set(Calendar.HOUR_OF_DAY, 0);  
		date.set(Calendar.MINUTE, 0);  
		date.set(Calendar.SECOND, 0);  
		date.set(Calendar.MILLISECOND, 0); 
		Date dateInPST = date.getTime();
		TimeZone pst = TimeZone.getTimeZone("America/Los_Angeles");
		
		Calendar expectedDate = Calendar.getInstance();
		expectedDate.set(Calendar.YEAR, 2009);  
		expectedDate.set(Calendar.MONTH, 0);  
		expectedDate.set(Calendar.DAY_OF_MONTH, 1);  
		expectedDate.set(Calendar.HOUR_OF_DAY, 8);  
		expectedDate.set(Calendar.MINUTE, 0);  
		expectedDate.set(Calendar.SECOND, 0);  
		expectedDate.set(Calendar.MILLISECOND, 0); 
		Date expectedDateInUTC = expectedDate.getTime();
		
		
		Date resultingDate = DateHelper.convertToUTC(dateInPST, pst);
		assertEquals(expectedDateInUTC, resultingDate);	
	}
	
	
	@Test
	public void should_convert_to_user_time() {
		Calendar date = Calendar.getInstance();
		date.set(Calendar.YEAR, 2009);  
		date.set(Calendar.MONTH, 0);  
		date.set(Calendar.DAY_OF_MONTH, 1);  
		date.set(Calendar.HOUR_OF_DAY, 8);  
		date.set(Calendar.MINUTE, 0);  
		date.set(Calendar.SECOND, 0);  
		date.set(Calendar.MILLISECOND, 0); 
		Date dateInUTC = date.getTime();
		TimeZone pst = TimeZone.getTimeZone("America/Los_Angeles");
		
		Calendar expectedDate = Calendar.getInstance();
		expectedDate.set(Calendar.YEAR, 2009);  
		expectedDate.set(Calendar.MONTH, 0);  
		expectedDate.set(Calendar.DAY_OF_MONTH, 1);  
		expectedDate.set(Calendar.HOUR_OF_DAY, 0);  
		expectedDate.set(Calendar.MINUTE, 0);  
		expectedDate.set(Calendar.SECOND, 0);  
		expectedDate.set(Calendar.MILLISECOND, 0); 
		Date expectedDateInPST = expectedDate.getTime();
		
		
		Date resultingDate = DateHelper.convertToUserTimeZone(dateInUTC, pst);
		assertEquals(expectedDateInPST, resultingDate);	
	}

	@Test(expected=IllegalArgumentException.class)
	public void truncate_date_throws_exception_on_invalid_arg() {
		DateHelper.truncate(new Date(), 20);
	}
	
	@Test
	public void truncate_all_fields() throws ParseException {
		DateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss.S");
				
        Date date = format.parse("27/05/2009 15:50:32.234");
		
        String dateTruncated = format.format(DateHelper.truncate(date, DateHelper.MILLISECOND));
        assertEquals("Truncating Milliseconds", "27/05/2009 15:50:32.234", dateTruncated);
        
        dateTruncated = format.format(DateHelper.truncate(date, DateHelper.SECOND));
        assertEquals("Truncating Seconds", "27/05/2009 15:50:32.0", dateTruncated);
        
        dateTruncated = format.format(DateHelper.truncate(date, DateHelper.MINUTE));
        assertEquals("Truncating Minutes", "27/05/2009 15:50:00.0", dateTruncated);
        
        dateTruncated = format.format(DateHelper.truncate(date, DateHelper.HOUR));
        assertEquals("Truncating Hours", "27/05/2009 15:00:00.0", dateTruncated);
        
        dateTruncated = format.format(DateHelper.truncate(date, DateHelper.DAY));
        assertEquals("Truncating Days", "27/05/2009 00:00:00.0", dateTruncated);
        
        dateTruncated = format.format(DateHelper.truncate(date, DateHelper.WEEK));
        assertEquals("Truncating Week", "24/05/2009 00:00:00.0", dateTruncated);
        
        dateTruncated = format.format(DateHelper.truncate(date, DateHelper.MONTH));
        assertEquals("Truncating Month", "01/05/2009 00:00:00.0", dateTruncated);
        
        dateTruncated = format.format(DateHelper.truncate(date, DateHelper.YEAR));
        assertEquals("Truncating Year", "01/01/2009 00:00:00.0", dateTruncated);

	}
	
	@Test
	public void increment_all_fields() throws ParseException {
		DateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss.S");
		
        Date date = format.parse("27/05/2009 15:50:32.234");
		
        String dateIncremented = format.format(DateHelper.increment(date, DateHelper.MILLISECOND, 5));
        assertEquals("Incrementing Milliseconds", "27/05/2009 15:50:32.239", dateIncremented);
        
        dateIncremented = format.format(DateHelper.increment(date, DateHelper.SECOND, 3));
        assertEquals("Incrementing Seconds", "27/05/2009 15:50:35.234", dateIncremented);
        
        dateIncremented = format.format(DateHelper.increment(date, DateHelper.MINUTE, 1));
        assertEquals("Incrementing Minutes", "27/05/2009 15:51:32.234", dateIncremented);
        
        dateIncremented = format.format(DateHelper.increment(date, DateHelper.HOUR, -4));
        assertEquals("Incrementing Hours", "27/05/2009 11:50:32.234", dateIncremented);
        
        dateIncremented = format.format(DateHelper.increment(date, DateHelper.DAY, 2));
        assertEquals("Incrementing Days", "29/05/2009 15:50:32.234", dateIncremented);
        
        dateIncremented = format.format(DateHelper.increment(date, DateHelper.WEEK, -2));
        assertEquals("Incrementing Week", "13/05/2009 15:50:32.234", dateIncremented);
        
        dateIncremented = format.format(DateHelper.increment(date, DateHelper.MONTH, 4));
        assertEquals("Incrementing Month", "27/09/2009 15:50:32.234", dateIncremented);

        dateIncremented = format.format(DateHelper.increment(date, DateHelper.YEAR, 20));
        assertEquals("Incrementing Year", "27/05/2029 15:50:32.234", dateIncremented);

	}
	
	@Test
	public void increment_zero_does_nothing() throws ParseException {
		DateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss.S");
		
		String dateString = "27/05/2009 15:50:32.234";
		
        Date date = format.parse(dateString);
		
        String dateIncremented = format.format(DateHelper.increment(date, DateHelper.MILLISECOND, 0));
        assertEquals("Zero Incrementing Milliseconds", dateString, dateIncremented);
        
        dateIncremented = format.format(DateHelper.increment(date, DateHelper.SECOND, 0));
        assertEquals("Zero Incrementing Seconds", dateString, dateIncremented);
        
        dateIncremented = format.format(DateHelper.increment(date, DateHelper.MINUTE, 0));
        assertEquals("Zero Incrementing Minutes", dateString, dateIncremented);
        
        dateIncremented = format.format(DateHelper.increment(date, DateHelper.HOUR, 0));
        assertEquals("Zero Incrementing Hours", dateString, dateIncremented);
        
        dateIncremented = format.format(DateHelper.increment(date, DateHelper.DAY, 0));
        assertEquals("Zero Incrementing Days", dateString, dateIncremented);
        
        dateIncremented = format.format(DateHelper.increment(date, DateHelper.WEEK, 0));
        assertEquals("Zero Incrementing Week", dateString, dateIncremented);
        
        dateIncremented = format.format(DateHelper.increment(date, DateHelper.MONTH, 0));
        assertEquals("Zero Incrementing Month", dateString, dateIncremented);

        dateIncremented = format.format(DateHelper.increment(date, DateHelper.YEAR, 0));
        assertEquals("Zero Incrementing Year", dateString, dateIncremented);

	}
	
	@Test
	public void increment_rolls_times() throws ParseException {
		DateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss.S");
				
        Date date = format.parse("27/05/2009 15:50:32.234");
		
        String dateIncremented = format.format(DateHelper.increment(date, DateHelper.MILLISECOND, 2000));
        assertEquals("Incrementing Milliseconds", "27/05/2009 15:50:34.234", dateIncremented);
        
        dateIncremented = format.format(DateHelper.increment(date, DateHelper.SECOND, 86400));
        assertEquals("Incrementing Seconds", "28/05/2009 15:50:32.234", dateIncremented);
        
        dateIncremented = format.format(DateHelper.increment(date, DateHelper.MINUTE, 11));
        assertEquals("Incrementing Minutes", "27/05/2009 16:01:32.234", dateIncremented);
        
        dateIncremented = format.format(DateHelper.increment(date, DateHelper.HOUR, -24));
        assertEquals("Incrementing Hours", "26/05/2009 15:50:32.234", dateIncremented);
        
        dateIncremented = format.format(DateHelper.increment(date, DateHelper.DAY, 7));
        assertEquals("Incrementing Days", "03/06/2009 15:50:32.234", dateIncremented);
        
        dateIncremented = format.format(DateHelper.increment(date, DateHelper.WEEK, 2));
        assertEquals("Incrementing Week", "10/06/2009 15:50:32.234", dateIncremented);
        
        dateIncremented = format.format(DateHelper.increment(date, DateHelper.MONTH, 24));
        assertEquals("Incrementing Month", "27/05/2011 15:50:32.234", dateIncremented);

	}
}
