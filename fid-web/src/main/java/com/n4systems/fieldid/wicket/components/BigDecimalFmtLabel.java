package com.n4systems.fieldid.wicket.components;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.IModel;
import org.apache.wicket.util.convert.IConverter;
import org.apache.wicket.util.convert.converter.BigDecimalConverter;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

public class BigDecimalFmtLabel extends Label {

    NumberFormat numberFormat;

    public BigDecimalFmtLabel(final String id, IModel<?> model)
    {
        super(id, model);
    }

    public BigDecimalFmtLabel(final String id, IModel<?> model, NumberFormat numberFormat)
    {
        super(id, model);
        this.numberFormat = numberFormat;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <C> IConverter<C> getConverter(Class<C> type)
    {
        return (IConverter<C>) new BigDecimalConverter() {
            @Override
            public NumberFormat getNumberFormat(Locale locale) {

                if (null == numberFormat) {
                    numberFormat = new DecimalFormat();
                }

                return numberFormat;
            }
        };

    }



}
