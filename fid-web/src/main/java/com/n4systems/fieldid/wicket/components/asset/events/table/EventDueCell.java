package com.n4systems.fieldid.wicket.components.asset.events.table;

import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.model.Event;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;

public class EventDueCell extends Panel {
    
    public EventDueCell(String id, IModel<Event> eventModel) {
        super(id);
        
        Event schedule = eventModel.getObject();
        
        if(schedule.getNextDate() == null) {
            add(new Label("dueDate", new FIDLabelModel("label.no_due_date")));
        }else {
            if(schedule.isPastDue()) {
                Long daysPastDue = schedule.getDaysPastDue();
                if(daysPastDue > 1L)
                    add(new Label("dueDate", new FIDLabelModel("label.x_days_ago", daysPastDue)));
                else
                    add(new Label("dueDate", new FIDLabelModel("label.a_day_ago")));
            }else {
                Long daysToDue = schedule.getDaysToDue();
                if(daysToDue > 1L)
                    add(new Label("dueDate", new FIDLabelModel("label.x_days_from_now", daysToDue)));
                else
                    add(new Label("dueDate", new FIDLabelModel("label.a_day_from_now")));
            }
        }
    }
}
