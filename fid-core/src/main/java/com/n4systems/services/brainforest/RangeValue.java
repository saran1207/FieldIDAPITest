package com.n4systems.services.brainforest;

public class RangeValue extends MultiValue {

    Value from;
    Value to;

    public RangeValue(SimpleValue value) {
        withFrom(value);
    }

    public RangeValue() {
    }

    public RangeValue(Value value) {
        if (value instanceof RangeValue) {
            RangeValue rangeValue = (RangeValue) value;
            withFrom(rangeValue.getFrom());
            withTo(rangeValue.getTo());
        } else {
            withFrom(value);
        }
    }

    public RangeValue(String date, SimpleValue.DateFormatType type) {

    }

    public RangeValue withFrom(Value value) {
        // TODO : ensure that FROM<TO.   if not, switch them around.
        from = value;
        return this;
    }

    public RangeValue withTo(Value value) {
        // TODO : ensure that FROM<TO
        to = value;
        return this;
    }

    public Value getFrom() {
        return from;
    }

    public Value getTo() {
        return to;
    }

    @Override
    public String toString() {
        return toVerboseString();
    }

    @Override
    public boolean isDate() {
        return from.isDate() && to.isDate();
    }

    @Override
    public boolean isNumber() {
        return from.isNumber() && to.isNumber();
    }

    @Override
    public boolean isString() {
        return !isDate() && !isNumber();
    }

    @Override
    public String toVerboseString() {
        StringBuilder builder = new StringBuilder();
        builder.append("[").append(from).append("...").append(to).append("]");
        return String.format(getVerboseFormat(),builder.toString());
    }


}
