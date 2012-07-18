package com.n4systems.fieldid.wicket.components.asset.events.table;

import com.n4systems.fieldid.wicket.model.DayDisplayModel;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.model.Event;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

public class EventDueCell extends Panel {
    
    public EventDueCell(String id, IModel<Event> eventModel) {
        super(id);
        
        Event schedule = eventModel.getObject();
        
        if(schedule.getNextDate() == null) {
            add(new Label("dueDate", new FIDLabelModel("label.no_due_date")));
        }else {
            add(new Label("dueDate", new DayDisplayModel(Model.of(schedule.getNextDate())).includeTime()));
        }
    }
}
