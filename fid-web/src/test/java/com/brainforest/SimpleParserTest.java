package com.brainforest;


import com.n4systems.fieldid.junit.FieldIdServiceTest;
import com.n4systems.model.utils.DateRange;
import com.n4systems.services.brainforest.*;
import com.n4systems.test.TestTarget;
import com.n4systems.util.chart.RangeType;
import org.joda.time.LocalDate;
import org.junit.Test;

import java.io.StringReader;
import java.lang.reflect.Field;
import java.util.Date;

import static org.apache.commons.lang.time.DateUtils.parseDate;
import static org.easymock.EasyMock.*;


public class SimpleParserTest extends FieldIdServiceTest {


    @TestTarget SimpleParser simpleParser;

    DateParser dateParser;

    @Override
    public void setUp() {
        super.setUp();
        dateParser = createMock(DateParser.class);
    }

    @Override
    protected Object createSut(Field sutField) throws Exception {
        return new SimpleParser(new StringReader("")) {
            @Override public ValueFactory getValueFactory() {
                return new ValueFactory() {
                    @Override protected DateParser getDateParser() {
                        return dateParser;
                    }
                };
            }
        };
    }

    //    <VERBOSE_DATE : (["0"-"9"]){1,2} ([" "])* (",")? ([" "])* (["0"-"9"]){2,4} >  : DEFAULT
    @Test
    public void test_parser_date() throws ParseException {
        String[] patterns = {"dd/mm/yyyy",
                    "dd-mm-yyyy",
                "MMM dd yyyy","MMM dd,yyyy","MMM dd"};

        Date date = null;
        try {
            System.out.println(parseDate("apr 22,2013", patterns));
            System.out.println(parseDate("apr 22", patterns));  // need to calculate if date is year 0, then assume current year.
            System.out.println(parseDate("01-04-2022", patterns));
            System.out.println(parseDate("11/07/2005", patterns));
            System.out.println(new LocalDate(parseDate("apr 22", patterns)).getYear());
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }
        System.out.println(date);
    }

    @Test
    public void test_parser_stuff() throws ParseException, java.text.ParseException {
        expect(dateParser.parseRange(anyObject(String.class), anyObject(SimpleValue.DateFormatType.class))).andReturn(new DateRange(RangeType.TODAY)).anyTimes();
        replay(dateParser);
        System.out.println(simpleParser.parseQuery("days=01-05-2011, TODAY, 02-06-2012 "));
    }


    @Test
    public void test_parser_conjunction() throws ParseException, java.text.ParseException {
        expect(dateParser.parseRange(anyObject(String.class), anyObject(SimpleValue.DateFormatType.class))).andReturn(new DateRange(RangeType.TODAY)).anyTimes();
        replay(dateParser);
        System.out.println(simpleParser.parseQuery("days=apple...banana"));

        System.out.println(simpleParser.parseQuery("fruit = apple,orange, banana"));

        System.out.println(simpleParser.parseQuery("x=7 and y='hello'"));

        System.out.println(simpleParser.parseQuery("x=99 or y='goodbye'"));
    }


    @Test
    public void test_parser() throws ParseException, java.text.ParseException {
        expect(dateParser.parseRange(anyObject(String.class), anyObject(SimpleValue.DateFormatType.class))).andReturn(new DateRange(RangeType.TODAY)).anyTimes();
        replay(dateParser);

        System.out.println(simpleParser.parseQuery("days=TODAY,TOMORROW,march 10,2012"));

        System.out.println(simpleParser.parseQuery("dotdotdot=march 1,2011...march 10,2012"));

        System.out.println(simpleParser.parseQuery("color:blue"));

        System.out.println(simpleParser.parseQuery("'c o l o r':'b l u e '"));

        System.out.println(simpleParser.parseQuery("x>99.99"));

        System.out.println(simpleParser.parseQuery("x=1 to 100"));

        System.out.println(simpleParser.parseQuery("apple banana pear"));

        System.out.println(simpleParser.parseQuery("x=1 TO 100"));

        System.out.println(simpleParser.parseQuery("x>99.99"));

        System.out.println(simpleParser.parseQuery("x=g,f,4,Z"));

        System.out.println(simpleParser.parseQuery("x=apple to orange"));

        System.out.println(simpleParser.parseQuery("x1000=1...100"));

        System.out.println(simpleParser.parseQuery("x=march 1,2011,march 10,2012"));

        System.out.println(simpleParser.parseQuery("this week to next week"));

        System.out.println(simpleParser.parseQuery("foo=this tuesday to next march"));

        System.out.println(simpleParser.parseQuery("ellip=this  week...March 1 2012"));

        System.out.println(simpleParser.parseQuery("foo=March 1,2012 to March 11,2012"));

        System.out.println(simpleParser.parseQuery("foo=March 1 2012,March 11 2012"));

        System.out.println(simpleParser.parseQuery("foo=this  week to next week"));

        System.out.println(simpleParser.parseQuery("blargh=this  week,next week "));

        System.out.println(simpleParser.parseQuery("identified=this  week"));

        System.out.println(simpleParser.parseQuery("next year"));

        System.out.println(simpleParser.parseQuery("march 1,2012"));

        System.out.println(simpleParser.parseQuery("dashDate=01-22-2012"));

        System.out.println(simpleParser.parseQuery("slashDate=01/22/2012"));

        System.out.println(simpleParser.parseQuery("mar 1,2012"));

        System.out.println(simpleParser.parseQuery("mar 1,   2012"));

        System.out.println(simpleParser.parseQuery("february 31   2012"));

        System.out.println(simpleParser.parseQuery("yyy=this  week"));

        System.out.println(simpleParser.parseQuery("xxx=this  week and bar=next week"));

        System.out.println(simpleParser.parseQuery("feb 21 2012"));

        System.out.println(simpleParser.parseQuery("jan 31,  2012"));

        System.out.println(simpleParser.parseQuery("fruit = apple,orange, banana"));

        System.out.println(simpleParser.parseQuery("stuff=apple, cherry, pear and otherStuff=banana"));

        System.out.println(simpleParser.parseQuery("apple, cherry, pear and banana"));

        System.out.println(simpleParser.parseQuery("fruit=apple, cherry, pear and  smoothie=banana or XYZ ZZZ"));
        
    }

}
