package com.n4systems.fieldid.wicket.components.reporting.results;

import org.apache.wicket.extensions.markup.html.repeater.data.grid.ICellPopulator;
import org.apache.wicket.extensions.markup.html.repeater.data.table.AbstractColumn;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

import com.n4systems.util.views.RowView;

public class EventActionsColumn extends AbstractColumn<RowView> {

    public EventActionsColumn() {
        super(new Model<String>(""));
    }

    @Override
    public void populateItem(Item<ICellPopulator<RowView>> item, String id, IModel<RowView> rowModel) {
        item.add(new EventActionsCell(id, rowModel, item.getMarkupId()));
    }
    
    @Override
    public String getCssClass() {
    	return "actionsContainer";
    }

}
