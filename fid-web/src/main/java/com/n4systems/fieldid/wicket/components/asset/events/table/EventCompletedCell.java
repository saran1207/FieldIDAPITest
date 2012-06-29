package com.n4systems.fieldid.wicket.components.asset.events.table;

import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.model.Event;
import com.n4systems.model.EventSchedule;
import com.n4systems.util.DateHelper;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;

public class EventCompletedCell extends Panel {
    public EventCompletedCell(String id, IModel<Event> eventModel) {
        super(id);
        
        EventSchedule schedule = eventModel.getObject().getSchedule();
        
        if(schedule.getCompletedDate() == null) {
            add(new Label("completedDate", new FIDLabelModel("")));
        }else {
            Long daysCompleted = DateHelper.getDaysUntilToday(schedule.getCompletedDate());
            if(daysCompleted > 1) {
                add(new Label("completedDate", new FIDLabelModel("label.x_days_ago", daysCompleted)));
            } else {
                add(new Label("completedDate", new FIDLabelModel("label.a_day_ago")));
            }
        }
    }
}
