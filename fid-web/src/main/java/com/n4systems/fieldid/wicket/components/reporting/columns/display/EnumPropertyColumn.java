package com.n4systems.fieldid.wicket.components.reporting.columns.display;

import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.model.api.DisplayEnum;
import org.apache.wicket.extensions.markup.html.repeater.data.table.PropertyColumn;
import org.apache.wicket.model.IModel;

public class EnumPropertyColumn<T> extends PropertyColumn<T> {

    public EnumPropertyColumn(IModel<String> displayModel, String sortProperty, String propertyExpression) {
        super(displayModel, sortProperty, propertyExpression);
    }

    @Override
    protected IModel<?> createLabelModel(IModel<T> rowModel) {
        IModel<?> labelModel = super.createLabelModel(rowModel);

        DisplayEnum label = (DisplayEnum)labelModel.getObject();
        return new FIDLabelModel(label.getLabel());
    }

}
