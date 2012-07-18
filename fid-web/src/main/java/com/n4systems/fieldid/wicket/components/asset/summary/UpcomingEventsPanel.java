package com.n4systems.fieldid.wicket.components.asset.summary;

import com.n4systems.fieldid.wicket.components.asset.events.table.OpenActionsCell;
import com.n4systems.fieldid.wicket.model.DayDisplayModel;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.fieldid.wicket.model.event.UpcomingEventsListModel;
import com.n4systems.model.Asset;
import com.n4systems.model.Event;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;

public class UpcomingEventsPanel extends Panel {

    public UpcomingEventsPanel(String id, UpcomingEventsListModel model, final Asset asset) {
        super(id, model);

        add(new ListView<Event>("upcomingEventsList", model) {
            @Override
            protected void populateItem(ListItem<Event> item) {
                Event schedule = item.getModelObject();

                item.add(new Label("upcomingEventType", schedule.getType().getName()));

                DayDisplayModel upcomingEventDate = new DayDisplayModel(Model.of(schedule.getNextDate()));

                if (schedule.isPastDue()) {
                    item.add(new Label("upcomingEventDate", new FIDLabelModel("label.x_days_ago_on", schedule.getDaysPastDue(), upcomingEventDate.getObject())));
                    item.add(new AttributeAppender("class", "overdue").setSeparator(" "));
                } else if(schedule.getDaysToDue().equals(0L)) {
                    item.add(new Label("upcomingEventDate", new FIDLabelModel("label.today_on", upcomingEventDate.getObject())));
                    item.add(new AttributeAppender("class", "due").setSeparator(" "));
                } else {
                    item.add(new Label("upcomingEventDate", new FIDLabelModel("label.in_x_days_on", schedule.getDaysToDue(), upcomingEventDate.getObject())));
                    item.add(new AttributeAppender("class", "due").setSeparator(" "));
                }

                item.add(new OpenActionsCell("openActions", Model.of(schedule), UpcomingEventsPanel.this));

            }
        });
        
    }
}
