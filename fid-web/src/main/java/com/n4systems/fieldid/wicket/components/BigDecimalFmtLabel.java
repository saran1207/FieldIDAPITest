package com.n4systems.fieldid.wicket.components;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.IModel;
import org.apache.wicket.util.convert.ConversionException;
import org.apache.wicket.util.convert.IConverter;

import java.math.BigDecimal;
import java.util.Locale;

public class BigDecimalFmtLabel extends Label {


    public BigDecimalFmtLabel(final String id, IModel<?> model)
    {
        super(id, model);
    }

    public final IConverter getConverter(Class type)
    {
        if (BigDecimal.class.isAssignableFrom(type))
        {
            return new IConverter() {
                @Override
                public Object convertToObject(String value, Locale locale) {

                    BigDecimal val = new BigDecimal(value.toString());
                    return val;
                }

                @Override
                public String convertToString(Object value, Locale locale) {

                    if (value == null) {
                        return null;
                    }
                    else if (value instanceof BigDecimal) {
                        BigDecimal bd = (BigDecimal) value;
                        return bd.toString();

                    }
                    else {
                        throw new ConversionException("Can't convert " +
                                value.getClass().getName() + "["+value+"] to BigDecimal.");
                    }

                }
            };
        }

        return super.getConverter(type);
    }

}
