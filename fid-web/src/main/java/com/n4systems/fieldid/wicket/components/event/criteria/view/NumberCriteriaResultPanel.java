package com.n4systems.fieldid.wicket.components.event.criteria.view;

import com.n4systems.model.NumberFieldCriteria;
import com.n4systems.model.NumberFieldCriteriaResult;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.util.convert.IConverter;
import org.apache.wicket.util.convert.converter.DoubleConverter;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

public class NumberCriteriaResultPanel extends Panel{
    public NumberCriteriaResultPanel(String id, IModel<NumberFieldCriteriaResult> resultModel) {
        super(id);
        final NumberFieldCriteria criteria = (NumberFieldCriteria) resultModel.getObject().getCriteria();

        add(new Label("numberResult", new PropertyModel<Double>(resultModel, "value")) {
            @Override
            @SuppressWarnings("unchecked")
            public <C> IConverter<C> getConverter(Class<C> type) {
                return (IConverter<C>) new DoubleConverter() {
                    @Override
                    public NumberFormat getNumberFormat(Locale locale) {
                        NumberFormat format = new DecimalFormat();
                        format.setMaximumFractionDigits(criteria.getDecimalPlaces());
                        format.setMinimumFractionDigits(criteria.getDecimalPlaces());
                        return format;
                    }
                };
            }
        });
    }
}
