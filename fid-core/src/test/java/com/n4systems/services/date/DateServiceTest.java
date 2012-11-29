package com.n4systems.services.date;

import com.n4systems.fieldid.FieldIdServiceTest;
import com.n4systems.fieldid.service.PersistenceService;
import com.n4systems.model.utils.DateRange;
import com.n4systems.services.SecurityContext;
import com.n4systems.test.TestMock;
import com.n4systems.test.TestTarget;
import com.n4systems.util.chart.RangeType;
import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;
import org.joda.time.DateTimeZone;
import org.joda.time.LocalDate;
import org.junit.Test;

import java.lang.reflect.Field;
import java.util.Date;
import java.util.TimeZone;

import static org.junit.Assert.assertEquals;

public class DateServiceTest extends FieldIdServiceTest {

    public static final int HOURS_OFFSET = 9;

    private TimeZone timeZoneGMT_9 = DateTimeZone.forOffsetHours(HOURS_OFFSET).toTimeZone();

    @TestTarget private DateService dateService;

    @TestMock private PersistenceService persistenceService;
    @TestMock private SecurityContext securityContext;


    @Override
    protected Object createSut(Field sutField) throws Exception {
        return new DateService() {
            // extract and override method that, in production, will be fed by securityContext spring bean
            // that contains users context & timezone.
            @Override public TimeZone getUserTimeZone() {
                return timeZoneGMT_9;
            }
        };
    }

    @Test
    public void test_today() {
        // preflight :  we're using a timeZone that is GMT+9.  (japan area)
        // .: if GMT time is 2:59 GMT then "today" should be 11:59 on the SAME DAY.
        //                   3:00 GMT then "today" should be 12:00 on the NEXT DAY.
        // note that today() only returns the day part, not the hours.

        setCurrentMillisFixed(jan1_2011_midnight.withHourOfDay(14).withMinuteOfHour(59));
        LocalDate result = dateService.today();
        assertEquals(jan1_2011, result);

        setCurrentMillisFixed(new DateTime(jan1_2011.toDate()).plusHours(15));
        result = dateService.today();
        assertEquals(jan1_2011.plusDays(1), result);

        // contrast with UTC version which yields different results...

        setCurrentMillisFixed(new DateTime(jan1_2011.toDate()).plusHours(15));
        result = dateService.todayUTC();
        assertEquals(jan1_2011, result);

    }

    @Test
    public void test_now() {
        // preflight :  we're using a timeZone that is GMT+9.  (japan area)
        // .: whatever the GMT time is, "now" should be 9 hours later in that timezone.

        setCurrentMillisFixed(jan1_2011);
        DateTime result = dateService.now();

        assertEquals(HOURS_OFFSET,result.getHourOfDay());
        assertEquals(1,result.getDayOfYear());
        assertEquals(1,result.getMonthOfYear());
        assertEquals(2011,result.getYear());

        // loop thru an arbitrary number of hours so we include hours that straddle different dates.
        for (int hour=0;hour<50;hour++) {
            setCurrentMillisFixed(new DateTime(jan1_2011.toDate()).plusHours(hour));

            result = dateService.now();
            int expectedDay = 1 + (hour+HOURS_OFFSET)/24;
            int expectedHour = (hour + HOURS_OFFSET) % 24;
            assertEquals(expectedDay,result.getDayOfYear());
            assertEquals(expectedHour,result.getHourOfDay());

            // contrast with UTC version
            result = dateService.nowUTC();
            expectedDay = 1 + (hour)/24;
            expectedHour = hour % 24;
            assertEquals(expectedDay,result.getDayOfYear());
            assertEquals(expectedHour,result.getHourOfDay());
        }
    }

    @Test
    public void test_calculateFromDate() {
        setCurrentMillisFixed(jan1_2011_midnight.plusHours(15));

        // if jan1, today should be jan 2nd
        Date result = dateService.calculateFromDate(new DateRange(RangeType.TODAY));
        assertEquals(jan1_2011.plusDays(1).toDate(),result);

        result = dateService.calculateFromDate(new DateRange(RangeType.THIS_QUARTER));
        assertEquals(jan1_2011.toDate(),result);
        result = dateService.calculateFromDate(new DateRange(RangeType.THIS_MONTH));
        assertEquals(jan1_2011.toDate(),result);
        result = dateService.calculateFromDate(new DateRange(RangeType.THIS_WEEK));
        assertEquals(new LocalDate(getTimeZone()).withYear(2010).withMonthOfYear(DateTimeConstants.DECEMBER).withDayOfMonth(27).toDate(),result);
        result = dateService.calculateFromDate(new DateRange(RangeType.THIS_YEAR));
        assertEquals(jan1_2011.toDate(), result);
    }


    @Test
    public void test_getDateRange() {
        DateRange result = dateService.getDateRange(RangeType.LAST_YEAR);
        assertEquals(timeZoneGMT_9, result.getTimeZone());
    }

}
