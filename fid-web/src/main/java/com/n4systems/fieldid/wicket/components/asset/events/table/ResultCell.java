package com.n4systems.fieldid.wicket.components.asset.events.table;

import com.n4systems.model.Event;
import com.n4systems.model.EventResult;
import com.n4systems.model.WorkflowState;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;

public class ResultCell extends Panel {

    public ResultCell(String id, IModel<? extends Event> eventModel) {
        super(id);

        Event event = eventModel.getObject();
        WorkflowState state = event.getWorkflowState();
        EventResult eventResult = event.getEventResult();
        
        Label statusLabel;
        
        if(state.equals(WorkflowState.COMPLETED)) {
            add(statusLabel = new Label("result", new PropertyModel<Object>(eventResult, "displayName")));

            if(eventResult.equals(EventResult.FAIL))
                statusLabel.add(new AttributeModifier("class", "failColor"));
            else
                statusLabel.add(new AttributeModifier("class", "passColor"));
        } else {
            add(statusLabel = new Label("result"));
        }
    }
}
