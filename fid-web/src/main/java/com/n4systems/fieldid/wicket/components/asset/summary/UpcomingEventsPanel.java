package com.n4systems.fieldid.wicket.components.asset.summary;

import com.n4systems.fieldid.wicket.components.TimeAgoLabel;
import com.n4systems.fieldid.wicket.components.asset.events.table.OpenActionsCell;
import com.n4systems.fieldid.wicket.model.DayDisplayModel;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.fieldid.wicket.model.event.UpcomingEventsListModel;
import com.n4systems.model.Asset;
import com.n4systems.model.Event;
import com.n4systems.services.date.DateService;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.image.ContextImage;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;

public class UpcomingEventsPanel extends Panel {

    private @SpringBean DateService dateService;

    public UpcomingEventsPanel(String id, UpcomingEventsListModel model, final Asset asset) {
        super(id, model);

        add(new ListView<Event>("upcomingEventsList", model) {
            @Override
            protected void populateItem(ListItem<Event> item) {
                Event schedule = item.getModelObject();

                ContextImage scheduleIcon;
                if(schedule.getAssignee() != null) {

                    if (schedule.isPastDue()) {
                        item.add(scheduleIcon = new ContextImage("scheduleIcon", "images/event-open-assigned-overdue.png"));
                        scheduleIcon.add(new AttributeAppender("title",  new FIDLabelModel("label.open_assigned_overdue", schedule.getAssignee().getDisplayName())));
                    } else {
                        item.add(scheduleIcon = new ContextImage("scheduleIcon", "images/event-open-assigned.png"));
                        scheduleIcon.add(new AttributeAppender("title",  new FIDLabelModel("label.assignee_is", schedule.getAssignee().getDisplayName())));
                    }
                    scheduleIcon.add(new AttributeAppender("class", "tipsy-tooltip").setSeparator(" "));
                } else {
                    if (schedule.isPastDue()) {
                        item.add(scheduleIcon = new ContextImage("scheduleIcon", "images/event-open-overdue.png"));
                        scheduleIcon.add(new AttributeAppender("title", new FIDLabelModel("label.open_overdue").getObject()));
                    } else {
                        item.add(scheduleIcon = new ContextImage("scheduleIcon", "images/event-open.png"));
                        scheduleIcon.add(new AttributeAppender("title", new FIDLabelModel("label.event_open").getObject()));
                    }
                    scheduleIcon.add(new AttributeAppender("class", "tipsy-tooltip").setSeparator(" "));
                }

                item.add(new Label("upcomingEventType", schedule.getType().getName()));
                
                DayDisplayModel upcomingEventDate = new DayDisplayModel(Model.of(schedule.getDueDate())).includeTime();
                
                if (schedule.isPastDue()) {
                    TimeAgoLabel timeAgoField = new TimeAgoLabel("upcomingEventDate",Model.of(schedule.getDueDate()),dateService.getUsersTimeZone());
                    item.add(timeAgoField);
                } else if(schedule.getDaysToDue().equals(0L)) {
                    item.add(new Label("upcomingEventDate", new FIDLabelModel("label.today")));
                } else {
                    item.add(new Label("upcomingEventDate", new FIDLabelModel("label.in_x_days", schedule.getDaysToDue(), upcomingEventDate.getObject())));
                }
                
                item.add(new Label("onDate", new FIDLabelModel("label.on_date", upcomingEventDate.getObject())));
                
                item.add(new OpenActionsCell("openActions", Model.of(schedule), UpcomingEventsPanel.this));
            }
        });
    }
    
}
