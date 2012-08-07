package com.n4systems.fieldid.wicket.pages.event;

import com.n4systems.fieldid.service.PersistenceService;
import com.n4systems.fieldid.service.event.EventService;
import com.n4systems.fieldid.wicket.components.NonWicketLink;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.string.StringValue;

public class PerformEventPage extends EventPage {

    @SpringBean private EventService eventService;
    @SpringBean private PersistenceService persistenceService;

    public PerformEventPage(PageParameters parameters) {
        Long assetId = parameters.get("assetId").toLongObject();
        Long type = parameters.get("type").toLongObject();

        StringValue scheduleIdString = parameters.get("scheduleId");
        if (!scheduleIdString.isEmpty()) {
            Long openEventId = scheduleIdString.toLongObject();
            event = eventService.createEventFromOpenEvent(openEventId);
        } else {
            event = eventService.createNewMasterEvent(assetId, type);
        }

        NonWicketLink backToStrutsLink = new NonWicketLink("backToStrutsLink", "eventAdd.action?assetId=" + assetId + "&type=" + type);
        add(backToStrutsLink);

        doAutoSchedule();
    }

}
