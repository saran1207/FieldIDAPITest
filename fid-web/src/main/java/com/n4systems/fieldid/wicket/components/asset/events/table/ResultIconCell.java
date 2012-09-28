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

        Event event = eventModel.getObject();
        Event.EventState state = event.getEventState();
        ContextImage image;
        if(state.equals(Event.EventState.COMPLETED)) {
            Status status = event.getStatus();
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
        } else if(state.equals(Event.EventState.OPEN)) {


            //label.open_assigned_overdue=This event is open, assigned to {0} and overdue
            //label.open_overdue=This event is open and overdue

            if(event.getAssignee() != null) {
                if(event.isPastDue()) {
                    add(image = new ContextImage("resultIcon", "images/event-open-assigned-overdue.png"));
                    image.add(new AttributeAppender("title", new FIDLabelModel("label.open_assigned_overdue", event.getAssignee().getDisplayName())));
                } else {
                    add(image = new ContextImage("resultIcon", "images/event-open-assigned.png"));
                    image.add(new AttributeAppender("title", new FIDLabelModel("label.assignee_is", event.getAssignee().getDisplayName())));
                }
            }else {
                if(event.isPastDue()) {
                    add(image = new ContextImage("resultIcon", "images/event-open-overdue.png"));
                    image.add(new AttributeAppender("title", new FIDLabelModel("label.open_overdue").getObject()));
                } else {
                    add(image = new ContextImage("resultIcon", "images/event-open.png"));
                    image.add(new AttributeAppender("title", new FIDLabelModel("label.event_open").getObject()));
                }
            }
        }else /*if(state.equals(Event.EventState.CLOSED)) */ {
            add(image = new ContextImage("resultIcon", "images/event-closed.png"));
            image.add(new AttributeAppender("title", new FIDLabelModel("label.event_closed").getObject()));
        }
    }
}
