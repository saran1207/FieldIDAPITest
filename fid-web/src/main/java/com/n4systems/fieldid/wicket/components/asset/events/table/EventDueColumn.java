package com.n4systems.fieldid.wicket.components.asset.events.table;

import com.n4systems.model.ThingEvent;
import org.apache.wicket.extensions.markup.html.repeater.data.grid.ICellPopulator;
import org.apache.wicket.extensions.markup.html.repeater.data.table.PropertyColumn;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;

public class EventDueColumn extends PropertyColumn<ThingEvent> {


    public EventDueColumn(IModel<String> displayModel, String sortProperty, String propertyExpression) {
        super(displayModel, sortProperty, propertyExpression);
    }

    @Override
    public void populateItem(Item<ICellPopulator<ThingEvent>> item, String id, IModel<ThingEvent> eventModel) {
        item.add(new EventDueCell(id, eventModel));
    }
}
