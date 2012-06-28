package com.n4systems.fieldid.wicket.components.asset.events;

import com.n4systems.model.Event;
import org.apache.wicket.extensions.markup.html.repeater.data.grid.ICellPopulator;
import org.apache.wicket.extensions.markup.html.repeater.data.table.PropertyColumn;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;

public class EventCompletedColumn extends PropertyColumn<Event> {


    public EventCompletedColumn(IModel<String> displayModel, String sortProperty, String propertyExpression) {
        super(displayModel, sortProperty, propertyExpression);
    }

    @Override
    public void populateItem(Item<ICellPopulator<Event>> item, String id, IModel<Event> eventModel) {
        item.add(new EventCompletedCell(id, eventModel));
    }
}
