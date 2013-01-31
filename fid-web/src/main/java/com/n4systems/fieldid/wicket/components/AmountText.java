package com.n4systems.fieldid.wicket.components;


import com.n4systems.fieldid.wicket.util.AmountParser;
import com.n4systems.model.UnitType;
import com.n4systems.model.common.AmountWithString;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.util.convert.IConverter;

import javax.measure.quantity.Quantity;
import java.util.Locale;

public class AmountText<Q extends Quantity> extends TextField<AmountWithString<Q>> {

    private final UnitType unitType;
    private transient final AmountParser parser;

    public AmountText(String id, PropertyModel<AmountWithString<Q>> model, UnitType unitType) {
        super(id,model);
        this.unitType = unitType;
        parser = new AmountParser(unitType);
    }

    @Override
    public <C> IConverter<C> getConverter(Class<C> type) {
        return new IConverter<C>() {
            @Override public String convertToString(Object value, Locale locale) {
                AmountWithString measureWithString = (AmountWithString) value;
                return measureWithString.getText();
            }

            @Override public C convertToObject(String value, Locale locale) {
                return (C) parser.withLocale(locale).parse(value);
            }
        };
    }

}
