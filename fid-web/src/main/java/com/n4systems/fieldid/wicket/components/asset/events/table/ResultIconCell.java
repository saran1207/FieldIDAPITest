package com.n4systems.fieldid.wicket.components.asset.events.table;

import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.model.Event;
import com.n4systems.model.Status;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.html.image.ContextImage;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;

public class ResultIconCell extends Panel {

    public ResultIconCell(String id, IModel<Event> eventModel) {
        super(id);

        Status status = eventModel.getObject().getStatus();
        ContextImage image;
        if(status.equals(Status.FAIL)) {
            add(image = new ContextImage("resultIcon", "images/event-completed-fail.png"));
            image.add(new AttributeAppender("title", new FIDLabelModel("label.event_completed", status.getDisplayName()).getObject()));
        } else if(status.equals(Status.PASS)) {
            add(image = new ContextImage("resultIcon", "images/event-completed-pass.png"));
            image.add(new AttributeAppender("title", new FIDLabelModel("label.event_completed", status.getDisplayName()).getObject()));
        } else if(status.equals(Status.NA)) {
            add(image = new ContextImage("resultIcon", "images/event-completed-na.png"));
            image.add(new AttributeAppender("title", new FIDLabelModel("label.event_completed", status.getDisplayName()).getObject()));
        }
    }
}
