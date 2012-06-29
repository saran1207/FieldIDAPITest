package com.n4systems.fieldid.wicket.components.asset.events.table;

import com.n4systems.model.Event;
import org.apache.wicket.extensions.markup.html.repeater.data.grid.ICellPopulator;
import org.apache.wicket.extensions.markup.html.repeater.data.table.PropertyColumn;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;



public class ResultColumn extends PropertyColumn<Event> {

    public ResultColumn(IModel<String> displayModel, String sortProperty, String propertyExpression) {
        super(displayModel, sortProperty, propertyExpression);
    }

    @Override
    public void populateItem(Item<ICellPopulator<Event>> item, String id, IModel<Event> eventModel) {
        item.add(new ResultCell(id, eventModel));
    }
}
