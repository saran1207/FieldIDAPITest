package com.n4systems.services.search.parser;

import com.n4systems.model.utils.DateRange;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

public class ValueFactory {

    private static final Logger logger=Logger.getLogger(ValueFactory.class);


    private @Autowired DateParser dateParser;

    public ValueFactory() {
        dateParser = new DateParser();
    }

    // need to handle the case where input == THIS WEEK...NEXT WEEK.   both terms are dateRanges but should be combined into a single date range (take min of both).

    public Value createDateValue(String date, SimpleValue.DateFormatType type) {
        // to clear up confusion between why DateRanges aren't necessarily RangeValues.
        // recall a simpleValue can actually be a range.  so for example, this week is a range but stored in a simple value.
        // however, "this week to next week" is a rangeValue (using the from/earliest values of each range stored in the 2 simple values).
        // in the case of "march 1,2011 to march 10,2011" it is a rangeValue with two exact dates defining the boundaries.
        try {
            DateRange dateRange = getDateParser().parseRange(date, type);
            return new SimpleValue(dateRange);
        } catch (java.text.ParseException e) {
            //??? what to do here...
        }
        logger.error("don't know how to create date from '" + date + "'");
        return null;
    }

    public SimpleValue createSimpleValue(String s) {  // either string or number.  NO dates!
        return new SimpleValue(s);
    }

    public Value augmentValue(String token, SimpleValue.DateFormatType type, Value value, MultiValue.Delimiter delimiter) {
        Value dateValue = createDateValue(token, type);
        return augmentValue(dateValue, value, delimiter);
    }

    public Value augmentValue(String token, Value value, ListValue.Delimiter delimiter) {
        return augmentValue(new SimpleValue(token),value, delimiter);
    }

    public Value augmentValue(Value newValue, Value value, MultiValue.Delimiter delimiter) {
        switch (delimiter) {
            case RANGE:
                return new RangeValue(value).withTo(newValue);
            case COMMA:
                return new ListValue(value).add(newValue);
            default:
            case NONE:
                throw new IllegalStateException("can't have delimiter of type NONE ");
        }
    }

    /** for testing extract and override purposes only!!! **/
    protected DateParser getDateParser() {
        return dateParser;
    }

}
