package com.n4systems.fieldid.wicket.components.reporting.columns.display;

import com.n4systems.model.search.ColumnMappingView;
import com.n4systems.util.views.RowView;
import org.apache.wicket.extensions.markup.html.repeater.data.grid.ICellPopulator;
import org.apache.wicket.extensions.markup.html.repeater.data.table.PropertyColumn;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

public class FieldIdPropertyColumn extends PropertyColumn<RowView> {

    private ColumnMappingView column;
    private int index;

    public FieldIdPropertyColumn(IModel<String> displayModel, ColumnMappingView column, int index, boolean sortable) {
        // For wicket's sort property, we'll use a String representation of the column ID
        // Since this is the only information we'll get from wicket when the user clicks a sort link,
        // but we'll need more info than that to actually sort our search (sort join, sort expr).
        super(displayModel, column.getDbColumnId().toString(), column.getPathExpression());
        this.column = column;
        this.index = index;
    }

    public FieldIdPropertyColumn(IModel<String> displayModel, ColumnMappingView column, int index) {
        super(displayModel, column.getPathExpression());
        this.column = column;
        this.index = index;
    }

    @Override
    public void populateItem(Item<ICellPopulator<RowView>> item, String componentId, IModel<RowView> rowModel) {
        Label label = new Label(componentId, createLabelModel(rowModel));
        label.setEscapeModelStrings(false);
        item.add(label);
    }

    @Override
    protected IModel<?> createLabelModel(IModel<RowView> rowModel) {
        return new Model<String>(rowModel.getObject().getStringValues().get(index));
    }

}
