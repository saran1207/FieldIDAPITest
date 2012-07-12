package com.n4systems.fieldid.wicket.components.asset.events.table;

import com.n4systems.model.Event;
import com.n4systems.model.Status;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;

public class ResultCell extends Panel {

    public ResultCell(String id, IModel<Event> eventModel) {
        super(id);

        Event event = eventModel.getObject();
        Event.EventState state = event.getEventState();
        Status status = event.getStatus();
        
        Label statusLabel;
        
        if(state.equals(Event.EventState.COMPLETED)) {
            add(statusLabel = new Label("result", new PropertyModel<Object>(status, "displayName")));

            if(status.equals(Status.FAIL))
                statusLabel.add(new AttributeModifier("class", "failColor"));
            else
                statusLabel.add(new AttributeModifier("class", "passColor"));
        } else {
            add(statusLabel = new Label("result"));
        }
    }
}
