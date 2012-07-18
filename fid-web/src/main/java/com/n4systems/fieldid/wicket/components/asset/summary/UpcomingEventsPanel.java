package com.n4systems.fieldid.wicket.components.asset.summary;

import com.n4systems.fieldid.wicket.FieldIDSession;
import com.n4systems.fieldid.wicket.components.asset.events.table.OpenActionsCell;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.fieldid.wicket.model.event.UpcomingEventsListModel;
import com.n4systems.model.Asset;
import com.n4systems.model.Event;
import com.n4systems.util.FieldidDateFormatter;
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

                String upcomingEventDate = new FieldidDateFormatter(schedule.getNextDate(), FieldIDSession.get().getSessionUser(), false, false).format();
                if (schedule.isPastDue()) {
                    item.add(new Label("upcomingEventDate", new FIDLabelModel("label.x_days_ago_on", schedule.getDaysPastDue(), upcomingEventDate)));
                    item.add(new AttributeAppender("class", "overdue").setSeparator(" "));
                }else {
                    item.add(new Label("upcomingEventDate", new FIDLabelModel("label.in_x_days_on", schedule.getDaysToDue(), upcomingEventDate)));
                    item.add(new AttributeAppender("class", "due").setSeparator(" "));
                }

                item.add(new OpenActionsCell("openActions", Model.of(schedule), UpcomingEventsPanel.this));

            }
        });
        
    }
}
