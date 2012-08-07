package com.n4systems.fieldid.wicket.pages.event;

import com.n4systems.fieldid.service.event.EventService;
import com.n4systems.fieldid.wicket.components.NonWicketLink;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;

public class PerformEventPage extends EventPage {

    @SpringBean private EventService eventService;

    public PerformEventPage(PageParameters parameters) {
        Long assetId = parameters.get("assetId").toLong();
        Long type = parameters.get("type").toLong();
        event = eventService.createNewMasterEvent(assetId, type);

        NonWicketLink backToStrutsLink = new NonWicketLink("backToStrutsLink", "eventAdd.action?assetId=" + assetId + "&type=" + type);
        add(backToStrutsLink);

        doAutoSchedule();
    }

}
