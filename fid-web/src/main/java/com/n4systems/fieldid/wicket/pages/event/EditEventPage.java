package com.n4systems.fieldid.wicket.pages.event;

import com.n4systems.fieldid.service.event.EventService;
import com.n4systems.fieldid.wicket.components.NonWicketLink;
import com.n4systems.model.Event;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;

public class EditEventPage extends EventPage {

    @SpringBean private EventService eventService;

    public EditEventPage(PageParameters parameters) {
        Long uniqueId = parameters.get("uniqueID").toLong();
        event = eventService.lookupExistingEvent(Event.class, uniqueId);

        NonWicketLink backToStrutsLink = new NonWicketLink("backToStrutsLink", "eventEdit.action?uniqueID=" + uniqueId);
        add(backToStrutsLink);
    }

}
