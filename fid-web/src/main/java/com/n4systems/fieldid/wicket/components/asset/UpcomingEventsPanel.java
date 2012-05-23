package com.n4systems.fieldid.wicket.components.asset;

import com.n4systems.fieldid.service.event.EventScheduleService;
import com.n4systems.fieldid.wicket.FieldIDSession;
import com.n4systems.fieldid.wicket.components.NonWicketLink;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.model.Asset;
import com.n4systems.model.EventSchedule;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.resource.ContextRelativeResource;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

public class UpcomingEventsPanel extends Panel {

    @SpringBean
    protected EventScheduleService eventScheduleService;

    private DateFormat df = new SimpleDateFormat(FieldIDSession.get().getSessionUser().getDateFormat());


    public UpcomingEventsPanel(String id, IModel<Asset> model) {
        super(id, model);
        
        Asset asset = model.getObject();

        addUpcomingEvents(asset);
    }


    private void addUpcomingEvents(final Asset asset) {
        List<EventSchedule> schedules = eventScheduleService.getAvailableSchedulesFor(asset);
        int remainingEvents = schedules.size() - 3;

        List<EventSchedule> scheduleSubList;
        if(schedules.size() > 3)
            scheduleSubList = schedules.subList(0, 3);
        else
            scheduleSubList = schedules;
        add(new ListView<EventSchedule>("upcomingEventsList", scheduleSubList) {
            @Override
            protected void populateItem(ListItem<EventSchedule> item) {
                EventSchedule schedule = item.getModelObject();
                
                item.add(new Image("upcomingEventState", new ContextRelativeResource("/images/calendar-icon.png")));
                item.add(new Label("upcomingEventDate", df.format(schedule.getNextDate())));
                item.add(new Label("upcomingEventType", schedule.getEventType().getName()));
                item.add(new NonWicketLink("startEventLink", "selectEventAdd.action?scheduleId=" + schedule.getId() + "&type=" + schedule.getEventType().getId() + "&assetId=" + asset.getId()));
                item.add(new NonWicketLink("editScheduleLink", "eventScheduleList.action?assetId=" + asset.getId() + "&useContext=false"));
            }
        });
        NonWicketLink moreUpcomingEventsLink;
        add(moreUpcomingEventsLink = new NonWicketLink("moreUpcomingEvents", "eventScheduleList.action?assetId=" + asset.getId() + "&useContext=false"));
        moreUpcomingEventsLink.add(new Label("moreUpcomingEventsMsg", new FIDLabelModel("label.x_more_scheduled_events", remainingEvents)));

    }
}
