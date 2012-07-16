package com.n4systems.fieldid.wicket.model.event;

import com.n4systems.fieldid.service.event.EventScheduleService;
import com.n4systems.fieldid.wicket.model.FieldIDSpringModel;
import com.n4systems.model.Asset;
import com.n4systems.model.Event;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.List;

public class UpcomingEventsListModel extends FieldIDSpringModel<List<Event>> {

    @SpringBean
    private EventScheduleService eventScheduleService;

    private Asset asset;

    @Override
    protected List<Event> load() {
        List<Event> schedules = eventScheduleService.getAvailableSchedulesFor(asset);
        if(schedules.size() > 3)
            return schedules.subList(0, 3);
        else
            return schedules;
    }
    
    public UpcomingEventsListModel setAsset(Asset asset) {
        this.asset = asset;
        return this;
    }
}
