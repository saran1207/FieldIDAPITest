package com.n4systems.fieldid.wicket.components.asset.events.table;

import com.n4systems.model.Event;
import org.apache.wicket.extensions.markup.html.repeater.data.grid.ICellPopulator;
import org.apache.wicket.extensions.markup.html.repeater.data.table.PropertyColumn;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;


public class ActionsColumn extends PropertyColumn<Event> {

    public ActionsColumn(IModel<String> displayModel, String propertyExpression) {
        super(displayModel, propertyExpression);
    }

    @Override
    public void populateItem(Item<ICellPopulator<Event>> item, String id, IModel<Event> eventModel) {
        Event.EventState state = eventModel.getObject().getEventState();

        if(state.equals(Event.EventState.COMPLETED)) {
            item.add(new EventActionsCell(id, eventModel));
        } else if (state.equals(Event.EventState.OPEN)) {
            item.add(new OpenActionsCell(id, eventModel));
        } else { //CLOSED
            item.add(new ClosedActionsCell(id, eventModel));
        }
    }
}
