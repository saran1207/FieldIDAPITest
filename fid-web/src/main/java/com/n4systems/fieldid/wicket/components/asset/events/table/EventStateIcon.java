package com.n4systems.fieldid.wicket.components.asset.events.table;

import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.model.Event;
import com.n4systems.model.EventResult;
import com.n4systems.services.date.DateService;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.html.image.ContextImage;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

public class EventStateIcon extends Panel {

    private @SpringBean
    DateService dateService;

    public EventStateIcon(String id, IModel<Event> eventModel) {
        super(id);

        Event event = eventModel.getObject();
        Event.WorkflowState state = event.getWorkflowState();
        ContextImage image;
        if(state.equals(Event.WorkflowState.COMPLETED)) {
            EventResult eventResult = event.getEventResult();
            if(eventResult.equals(EventResult.FAIL)) {
                add(image = new ContextImage("resultIcon", "images/event-completed-fail.png"));
                image.add(new AttributeAppender("title", new FIDLabelModel("label.event_completed", eventResult.getDisplayName()).getObject()));
            } else if(eventResult.equals(EventResult.PASS)) {
                add(image = new ContextImage("resultIcon", "images/event-completed-pass.png"));
                image.add(new AttributeAppender("title", new FIDLabelModel("label.event_completed", eventResult.getDisplayName()).getObject()));
            } else if(eventResult.equals(EventResult.NA)) {
                add(image = new ContextImage("resultIcon", "images/event-completed-na.png"));
                image.add(new AttributeAppender("title", new FIDLabelModel("label.event_completed", eventResult.getDisplayName()).getObject()));
            }
        } else if(state.equals(Event.WorkflowState.OPEN)) {
            if (event.getAssignedUserOrGroup() != null) {
                if(isPastDue(event)) {
                    add(image = new ContextImage("resultIcon", "images/event-open-assigned-overdue.png"));
                    image.add(new AttributeAppender("title", new FIDLabelModel("label.open_assigned_overdue", event.getAssigneeName())));
                } else {
                    add(image = new ContextImage("resultIcon", "images/event-open-assigned.png"));
                    image.add(new AttributeAppender("title", new FIDLabelModel("label.assignee_is", event.getAssigneeName())));
                }
            }else {
                if(isPastDue(event)) {
                    add(image = new ContextImage("resultIcon", "images/event-open-overdue.png"));
                    image.add(new AttributeAppender("title", new FIDLabelModel("label.open_overdue").getObject()));
                } else {
                    add(image = new ContextImage("resultIcon", "images/event-open.png"));
                    image.add(new AttributeAppender("title", new FIDLabelModel("label.event_open").getObject()));
                }
            }
        } else {
            add(image = new ContextImage("resultIcon", "images/event-closed.png"));
            image.add(new AttributeAppender("title", new FIDLabelModel("label.event_closed").getObject()));
        }
    }

    private boolean isPastDue(Event event) {
        return event.getWorkflowState() == Event.WorkflowState.OPEN && dateService.isPastDue(event.getDueDate());
    }
}
