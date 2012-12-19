package com.n4systems.fieldid.wicket.components.asset.events.table;

import com.n4systems.fieldid.wicket.FieldIDSession;
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
        Event.WorkflowState state = event.getWorkflowState();

        if(state.equals(Event.WorkflowState.COMPLETED) || state.equals(Event.WorkflowState.CLOSED) ) {
            add(new Label("completedDate", new DayDisplayModel(Model.of(event.getCompletedDate())).includeTime().withTimeZone(FieldIDSession.get().getSessionUser().getTimeZone())));
        }else {
            add(new Label("completedDate"));
        }
    }
}
