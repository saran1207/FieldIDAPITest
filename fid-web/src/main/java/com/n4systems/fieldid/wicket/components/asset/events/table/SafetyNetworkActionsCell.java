package com.n4systems.fieldid.wicket.components.asset.events.table;

import com.n4systems.fieldid.wicket.components.NonWicketLink;
import com.n4systems.model.Event;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;

public class SafetyNetworkActionsCell extends Panel {

    public SafetyNetworkActionsCell(String id, IModel<Event> model) {
        super(id, model);

        if (model.getObject().getWorkflowState() != Event.WorkflowState.COMPLETED) {
            setVisible(false);
        }

        Long eventId = model.getObject().getId();
        String viewAction = "event.action?uniqueID=" + eventId;
        add(new NonWicketLink("viewLink", viewAction, new AttributeModifier("class", "mattButton")));
    }

}
