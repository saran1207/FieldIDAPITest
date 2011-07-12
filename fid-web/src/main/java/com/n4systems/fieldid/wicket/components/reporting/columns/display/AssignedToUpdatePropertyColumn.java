package com.n4systems.fieldid.wicket.components.reporting.columns.display;

import com.n4systems.model.BaseEntity;
import org.apache.wicket.extensions.markup.html.repeater.data.table.PropertyColumn;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;

public class AssignedToUpdatePropertyColumn extends PropertyColumn<BaseEntity> {

    public AssignedToUpdatePropertyColumn(IModel<String> displayModel, String propertyExpression) {
        super(displayModel, propertyExpression);
    }

    @Override
    protected IModel<?> createLabelModel(IModel<BaseEntity> rowModel) {
        IModel<?> labelModel = super.createLabelModel(rowModel);
        return new PropertyModel(labelModel, "assignedUser.fullName");
    }

}
