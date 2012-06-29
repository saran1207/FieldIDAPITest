package com.n4systems.fieldid.wicket.components.asset.events.table;

import com.n4systems.model.Event;
import org.apache.wicket.extensions.markup.html.repeater.data.grid.ICellPopulator;
import org.apache.wicket.extensions.markup.html.repeater.data.table.PropertyColumn;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;


public class GpsIconColumn extends PropertyColumn<Event> {

    public GpsIconColumn(IModel<String> displayModel, String propertyExpression) {
        super(displayModel, propertyExpression);
    }

    @Override
    public void populateItem(Item<ICellPopulator<Event>> item, String id, IModel<Event> eventModel) {
        item.add(new GpsIconCell(id, eventModel));
    }
}
