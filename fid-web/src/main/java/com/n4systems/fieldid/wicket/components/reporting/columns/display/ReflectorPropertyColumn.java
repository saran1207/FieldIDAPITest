package com.n4systems.fieldid.wicket.components.reporting.columns.display;

import com.n4systems.fieldid.wicket.model.ReflectorPropertyModel;
import org.apache.wicket.extensions.markup.html.repeater.data.table.PropertyColumn;
import org.apache.wicket.model.IModel;

public class ReflectorPropertyColumn<T> extends PropertyColumn<T> {

    public ReflectorPropertyColumn(IModel<String> displayModel, String propertyExpression) {
        super(displayModel, propertyExpression);
    }

    @Override
    protected IModel<?> createLabelModel(IModel<T> rowModel) {
        return new ReflectorPropertyModel(rowModel, getPropertyExpression());
    }

}
