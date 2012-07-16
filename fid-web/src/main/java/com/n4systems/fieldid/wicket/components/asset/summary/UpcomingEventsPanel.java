package com.n4systems.fieldid.wicket.components.asset.summary;

import com.n4systems.fieldid.wicket.FieldIDSession;
import com.n4systems.fieldid.wicket.components.NonWicketLink;
import com.n4systems.fieldid.wicket.model.event.UpcomingEventsListModel;
import com.n4systems.model.Asset;
import com.n4systems.model.Event;
import com.n4systems.util.FieldidDateFormatter;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;

public class UpcomingEventsPanel extends Panel {

    public UpcomingEventsPanel(String id, UpcomingEventsListModel model, final Asset asset) {
        super(id, model);

        add(new ListView<Event>("upcomingEventsList", model) {
            @Override
            protected void populateItem(ListItem<Event> item) {
                Event schedule = item.getModelObject();

                String upcomingEventDate = new FieldidDateFormatter(schedule.getNextDate(), FieldIDSession.get().getSessionUser(), false, false).format();

                item.add(new Label("upcomingEventDate", upcomingEventDate));
                item.add(new Label("upcomingEventType", schedule.getType().getName()));
                item.add(new NonWicketLink("startEventLink", "selectEventAdd.action?scheduleId=" + schedule.getId() + "&type=" + schedule.getEventType().getId() + "&assetId=" + asset.getId()));
                item.add(new NonWicketLink("editScheduleLink", "eventScheduleList.action?assetId=" + asset.getId() + "&useContext=false"));
                if (schedule.isPastDue()) {
                    item.add(new AttributeModifier("class", "overdue"));
                }
            }
        });
    }
}
