package com.n4systems.fieldid.wicket.components;


import org.apache.wicket.model.IModel;

import java.text.DecimalFormat;
import java.text.NumberFormat;

public class GpsTextField<BigDecimal> extends BigDecimalFmtTextField<BigDecimal> {
    public GpsTextField(String id, IModel<BigDecimal> model) {

        super(id, model);

        // GPS - set to 6 or 10 digits?  Go with 6 but db stores 10
        NumberFormat numberFormat = new DecimalFormat();
        numberFormat.setMaximumFractionDigits(6);
        numberFormat.setMinimumFractionDigits(0);

        super.setNumberFormat(numberFormat);
    }
}
