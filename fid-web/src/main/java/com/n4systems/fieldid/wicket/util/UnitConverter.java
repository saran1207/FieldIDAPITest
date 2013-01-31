package com.n4systems.fieldid.wicket.util;

import com.google.common.collect.Lists;
import org.apache.wicket.util.convert.IConverter;
import org.jscience.physics.amount.Amount;

import java.util.List;
import java.util.Locale;

public class UnitConverter<T extends javax.measure.unit.Unit> implements IConverter<T> {

    private final T unit;

    public UnitConverter(T unit) {
        this.unit = unit;
    }

    @Override
    public T convertToObject(String value, Locale locale) {   // should it throw exception or just return null?
        Amount amount = parseValue(value);
        return null;
    }

    private Amount parseValue(String value) {
        List<Amount> amounts = Lists.newArrayList();
        Amount total = null;
        for (Amount amount:amounts) {
            total = total==null ? amount : total.plus(amount);
        }
        return total;
    }

    @Override
    public String convertToString(T value, Locale locale) {
        return null;
    }

}
