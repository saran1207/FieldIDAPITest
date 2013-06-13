package com.n4systems.services.brainforest;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;

import java.util.List;

public class ListValue extends MultiValue {

    protected List<Value> values = Lists.newArrayList();

    public ListValue() {
    }

    public ListValue(Value value) {
        if (value instanceof ListValue) {
            ListValue listValue = (ListValue) value;
            for (Value v:listValue.getValues()) {
                add(v);
            }
        } else {
            add( value);
        }
    }

    public ListValue add(Value value) {
        values.add(value);
        return this;
    }

    public List<Value> getValues() {
        return values;
    }

    @Override
    public boolean isDate() {
        for (Value value:values) {
            if (!(value.isDate())) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean isNumber() {
        for (Value value:values) {
            if (!(value.isNumber())) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean isString() {
        return !isDate() && !isNumber();
    }

    @Override
    public String toVerboseString() {
        StringBuilder builder = new StringBuilder();
        builder.append("[");
        builder.append(Joiner.on(",").join(values));
        builder.append("]");
        return String.format(getVerboseFormat(),builder.toString());
    }

}
