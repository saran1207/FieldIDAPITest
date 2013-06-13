package com.n4systems.services.brainforest;

import com.n4systems.fieldid.junit.FieldIdServiceTest;
import com.n4systems.model.utils.DateRange;
import com.n4systems.services.date.DateService;
import com.n4systems.test.TestMock;
import com.n4systems.test.TestTarget;
import org.junit.Test;

import java.util.TimeZone;

import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;

public class DateParserTest extends FieldIdServiceTest {

    @TestTarget DateParser dateParser;

    @TestMock DateService dateService;

    @Test
    public void test_parser() throws java.text.ParseException {
        expect(dateService.getUserTimeZone()).andReturn(TimeZone.getDefault()).anyTimes();
        replay(dateService);

        DateRange range = dateParser.parseRange("TODAY", SimpleValue.DateFormatType.TODAY);
        System.out.println(range);
        range = dateParser.parseRange("TOMORROW", SimpleValue.DateFormatType.TOMORROW);
        System.out.println(range);
        range = dateParser.parseRange("YESTERDAY", SimpleValue.DateFormatType.YESTERDAY);
        System.out.println(range);
        range = dateParser.parseRange("March 1,2011", SimpleValue.DateFormatType.VERBOSE_DATE);
        System.out.println(range);
        range = dateParser.parseRange("06/09/2015", SimpleValue.DateFormatType.SLASH_DATE);
        System.out.println(range);
        range = dateParser.parseRange("04-10-2012", SimpleValue.DateFormatType.DASH_DATE);
        System.out.println(range);

        range = dateParser.parseRange("THIS WEEK", SimpleValue.DateFormatType.FLOATING_DATE);
        System.out.println(range);
        range = dateParser.parseRange("THIS QUARTER", SimpleValue.DateFormatType.FLOATING_DATE);
        System.out.println(range);
        range = dateParser.parseRange("THIS MONTH", SimpleValue.DateFormatType.FLOATING_DATE);
        System.out.println(range);
        range = dateParser.parseRange("THIS YEAR", SimpleValue.DateFormatType.FLOATING_DATE);
        System.out.println(range);

        range = dateParser.parseRange("LAST WEEK", SimpleValue.DateFormatType.FLOATING_DATE);
        System.out.println(range);
        range = dateParser.parseRange("LAST QUARTER", SimpleValue.DateFormatType.FLOATING_DATE);
        System.out.println(range);
        range = dateParser.parseRange("LAST MONTH", SimpleValue.DateFormatType.FLOATING_DATE);
        System.out.println(range);
        range = dateParser.parseRange("LAST YEAR", SimpleValue.DateFormatType.FLOATING_DATE);
        System.out.println(range);

    }


}
