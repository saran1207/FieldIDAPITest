package com.n4systems.fieldid.wicket.components.reporting.results;

import com.n4systems.model.Event;
import org.apache.wicket.extensions.markup.html.repeater.data.grid.ICellPopulator;
import org.apache.wicket.extensions.markup.html.repeater.data.table.AbstractColumn;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

public class EventActionsColumn extends AbstractColumn<Event> {

    public EventActionsColumn() {
        super(new Model<String>(""));
    }

    @Override
    public void populateItem(Item<ICellPopulator<Event>> item, String id, IModel<Event> rowModel) {
        item.add(new EventActionsCell(id, rowModel, item.getMarkupId()));
    }

}
