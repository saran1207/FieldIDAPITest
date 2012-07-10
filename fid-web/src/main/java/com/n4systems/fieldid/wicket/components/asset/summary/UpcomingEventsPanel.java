package com.n4systems.fieldid.wicket.components.asset.summary;

import com.n4systems.fieldid.service.event.EventScheduleService;
import com.n4systems.fieldid.wicket.FieldIDSession;
import com.n4systems.fieldid.wicket.components.NonWicketLink;
import com.n4systems.model.Asset;
import com.n4systems.model.Event;
import com.n4systems.model.EventSchedule;
import com.n4systems.util.FieldidDateFormatter;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.List;

public class UpcomingEventsPanel extends Panel {

    @SpringBean
    protected EventScheduleService eventScheduleService;

    public UpcomingEventsPanel(String id, IModel<Asset> model) {
        super(id, model);
        
        Asset asset = model.getObject();

        addUpcomingEvents(asset);
    }


    private void addUpcomingEvents(final Asset asset) {
        List<Event> schedules = eventScheduleService.getAvailableSchedulesFor(asset);
        int remainingEvents = schedules.size() - 3;

        List<Event> scheduleSubList;
        if(schedules.size() > 3)
            scheduleSubList = schedules.subList(0, 3);
        else
            scheduleSubList = schedules;
        add(new ListView<Event>("upcomingEventsList", scheduleSubList) {
            @Override
            protected void populateItem(ListItem<Event> item) {
                Event schedule = item.getModelObject();
                
                String upcomingEventDate = new FieldidDateFormatter(schedule.getNextDate(), FieldIDSession.get().getSessionUser(), false, false).format();
                
                item.add(new Label("upcomingEventDate", upcomingEventDate));
                item.add(new Label("upcomingEventType", schedule.getType().getName()));
                item.add(new NonWicketLink("startEventLink", "selectEventAdd.action?scheduleId=" + schedule.getId() + "&type=" + schedule.getEventType().getId() + "&assetId=" + asset.getId()));
                item.add(new NonWicketLink("editScheduleLink", "eventScheduleList.action?assetId=" + asset.getId() + "&useContext=false"));
                if(schedule.isPastDue()) {
                    item.add(new AttributeModifier("class", "overdue"));
                }
            }
        });
    }
}
