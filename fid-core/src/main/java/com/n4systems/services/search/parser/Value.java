package com.n4systems.services.search.parser;


import com.n4systems.model.utils.DateRange;
import org.joda.time.DateTime;

public abstract class Value {

    public Value() {
    }

    public DateRange getDateRange() {
        throw new UnsupportedOperationException("implement this in concrete classes");
    }

    public DateTime getDate() {
        throw new UnsupportedOperationException("implement this in concrete classes");
    }

    public Number getNumber() {
        throw new UnsupportedOperationException("implement this in concrete classes");
    }

    public String getString() {
        return toString();
    }

    public String getStringOriginalCase() {
        return toString();
    }

    public boolean isDate() {
        return false;
    }

    public boolean isNumber() {
        return false;
    }

    public boolean isLong() {
        return false;
    }

    public boolean isDouble() {
        return false;
    }

    public boolean isString() {
        return false;
    }

    public String toVerboseString() {
        return toString();
    }

    protected String getVerboseFormat() {
        return isDate() ? "@%s" :
                isNumber() ? "#%s" :
                        "'%s'";
    }

    public boolean isDateRange() {
        return false;
    }
}


