package com.n4systems.fieldid.wicket.components.reporting.columns.display;

import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.model.BaseEntity;
import org.apache.wicket.extensions.markup.html.repeater.data.table.PropertyColumn;
import org.apache.wicket.model.IModel;

public class AssignedToPropertyColumn extends PropertyColumn<BaseEntity> {

    public AssignedToPropertyColumn(IModel<String> displayModel, String propertyExpression) {
        super(displayModel, propertyExpression);
    }

    @Override
    protected IModel<?> createLabelModel(IModel<BaseEntity> rowModel) {
        IModel<?> labelModel = super.createLabelModel(rowModel);
        if (labelModel.getObject() != null) {
            return labelModel;
        }
        return new FIDLabelModel("label.unassigned");
    }
}
