package com.n4systems.fieldid.wicket.components.asset.events.table;

import com.n4systems.fieldid.wicket.model.DayDisplayModel;
import com.n4systems.model.Event;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

public class EventCompletedCell extends Panel {
    public EventCompletedCell(String id, IModel<Event> eventModel) {
        super(id);

        Event event = eventModel.getObject();
        Event.EventState state = event.getEventState();

        if(state.equals(Event.EventState.COMPLETED) || state.equals(Event.EventState.CLOSED) ) {
            add(new Label("completedDate", new DayDisplayModel(Model.of(event.getCompletedDate())).includeTime()));
        }else {
            add(new Label("completedDate"));
        }
    }
}
