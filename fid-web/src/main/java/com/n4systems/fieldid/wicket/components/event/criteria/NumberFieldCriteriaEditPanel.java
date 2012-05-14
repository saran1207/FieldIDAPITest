package com.n4systems.fieldid.wicket.components.event.criteria;

import com.n4systems.fieldid.wicket.behavior.UpdateComponentOnChange;
import com.n4systems.model.NumberFieldCriteria;
import com.n4systems.model.NumberFieldCriteriaResult;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.util.convert.IConverter;
import org.apache.wicket.util.convert.converter.DoubleConverter;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

public class NumberFieldCriteriaEditPanel extends Panel {

    public NumberFieldCriteriaEditPanel(String id, IModel<NumberFieldCriteriaResult> result) {
        super(id);


        final NumberFieldCriteria criteria = (NumberFieldCriteria) result.getObject().getCriteria();
        TextField<Double> numberField = new TextField<Double>("numberField", new PropertyModel<Double>(result, "value")) {
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
        };
        numberField.add(new UpdateComponentOnChange());
        add(numberField);
    }

}
