package com.n4systems.fieldid.wicket.pages.setup.eventtypegroup;

import com.n4systems.model.EventTypeGroup;
import org.apache.wicket.extensions.markup.html.repeater.data.grid.ICellPopulator;
import org.apache.wicket.extensions.markup.html.repeater.data.table.PropertyColumn;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;

/**
 * Created by rrana on 2014-08-05.
 */
public class EventTypeGroupColumn extends PropertyColumn<EventTypeGroup> {


    public EventTypeGroupColumn(IModel<String> displayModel, String sortProperty, String propertyExpression) {
        super(displayModel, sortProperty, propertyExpression);
    }

    @Override
    public void populateItem(Item<ICellPopulator<EventTypeGroup>> item, String id, IModel<EventTypeGroup> eventTypeGroupModel) {
        item.add(new EventTypeGroupCell(id, eventTypeGroupModel));
    }
}
