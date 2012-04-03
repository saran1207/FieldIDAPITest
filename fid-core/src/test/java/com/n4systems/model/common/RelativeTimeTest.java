package com.n4systems.model.common;



import static org.junit.Assert.*;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.Test;

public class RelativeTimeTest {

	@Test
	public void test_relative_dates() throws ParseException {
		DateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss.S");
		
        Date date = format.parse("27/05/2009 15:50:32.234");
        
        String relDate = format.format(RelativeTime.TODAY.getRelative(date));
        assertEquals("Today", "27/05/2009 00:00:00.0", relDate);
        
        relDate = format.format(RelativeTime.TOMORROW.getRelative(date));
        assertEquals("Tomorrow", "28/05/2009 00:00:00.0", relDate);

        relDate = format.format(RelativeTime.DAY_1.getRelative(date));
        assertEquals("1 Day", "28/05/2009 00:00:00.0", relDate);

        relDate = format.format(RelativeTime.DAY_2.getRelative(date));
        assertEquals("2 Days", "29/05/2009 00:00:00.0", relDate);

        relDate = format.format(RelativeTime.DAY_7.getRelative(date));
        assertEquals("7 Days", "03/06/2009 00:00:00.0", relDate);

        relDate = format.format(RelativeTime.THIS_WEEK.getRelative(date));
        assertEquals("This Week", "24/05/2009 00:00:00.0", relDate);

        relDate = format.format(RelativeTime.NEXT_WEEK.getRelative(date));
        assertEquals("Next Week", "31/05/2009 00:00:00.0", relDate);

        relDate = format.format(RelativeTime.WEEK_1.getRelative(date));
        assertEquals("1 Week", "03/06/2009 00:00:00.0", relDate);

        relDate = format.format(RelativeTime.WEEK_2.getRelative(date));
        assertEquals("2 Weeks", "10/06/2009 00:00:00.0", relDate);

        relDate = format.format(RelativeTime.THIS_MONTH.getRelative(date));
        assertEquals("This Month", "01/05/2009 00:00:00.0", relDate);

        relDate = format.format(RelativeTime.NEXT_MONTH.getRelative(date));
        assertEquals("Next Month", "01/06/2009 00:00:00.0", relDate);
        
        relDate = format.format(RelativeTime.MONTH_1.getRelative(date));
        assertEquals("1 Month", "27/06/2009 00:00:00.0", relDate);

        relDate = format.format(RelativeTime.MONTH_2.getRelative(date));
        assertEquals("2 Months", "27/07/2009 00:00:00.0", relDate);
	}

}
