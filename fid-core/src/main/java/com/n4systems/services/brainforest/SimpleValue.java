package com.n4systems.services.brainforest;

import com.n4systems.model.utils.DateRange;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class SimpleValue extends Value {

    private static final Logger logger=Logger.getLogger(SimpleValue.class);

    public enum DateFormatType {
        VOID,
        TODAY,
        YESTERDAY,
        TOMORROW,
        DASH_DATE("dd-MM-yyyy"),
        SLASH_DATE("dd/MM/yyyy"),
        VERBOSE_DATE("MMM dd yyyy","MMM dd,yyyy","MMM dd", "MMM", "EEE"),
        FLOATING_DATE;

        String[] patterns;

        DateFormatType() {
        }

        DateFormatType(String... patterns) {
            this.patterns = patterns;
        }
        public boolean isRange() {
            return this==FLOATING_DATE;
        }
        public String[] getPatterns() {
            return patterns;
        }
    }

    private DateFormatType dateFormatType=DateFormatType.VOID;
    private DateRange date;
    private Number number;
    protected String stringValue;
    private boolean isQuoted = false;
    private Object originalValue;

    public SimpleValue(String s) {
        super();
        this.originalValue = s;
        this.stringValue = convertToString(s);
        number = convertToNumber(s);
        // this constructor is used when parser knows its dealing with string or number only!
        date = null;
    }

    public SimpleValue(DateRange dateRange) {
        this.date = dateRange;
        this.number = null;
        this.stringValue = formatDate();
        this.originalValue = date;
    }

    private String formatDate() {
        DateTimeFormatter fmt = DateTimeFormat.forPattern("MMMM d,yyyy");
        return fmt.print(date.getFrom());
    }

    protected String convertToString(String stringValue) {
        String s = stringValue.trim();
        if ((s.startsWith("\"") && s.endsWith("\"")) || (s.startsWith("'") && s.endsWith("'"))) {
            isQuoted = true;
            s = s.substring(1,s.length()-1).trim();
        }
        return s;
    }

    private Number convertToNumber(String stringValue) {
        Number number = convertLong(stringValue);
        return (number==null) ? convertFloat(stringValue) : number;
    }

    private Number convertLong(String stringValue) {
        try {
            return Long.parseLong(stringValue);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private Number convertFloat(String stringValue) {
        try {
            return Float.parseFloat(stringValue);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    public String toString() {
        return stringValue;
    }

    public String toVerboseString() {
        return String.format(getVerboseFormat(), toString());
    }

    @Override
    public DateTime getDate() {
        return date.getFrom().toDateTimeAtStartOfDay();
    }

    @Override
    public Number getNumber() {
        return number;
    }

    @Override
    public String getString() {
        return stringValue;
    }

    @Override
    public boolean isDate() {
        return date != null;
    }

    @Override
    public boolean isDouble() {
        return number instanceof Double;
    }

    @Override
    public boolean isLong() {
        return number instanceof Long;
    }

    @Override
    public boolean isString() {
        return !isDate() && !isNumber();
    }

    @Override
    public boolean isNumber() {
        return number != null;
    }

    public boolean isQuoted() {
        return isQuoted;
    }
}












