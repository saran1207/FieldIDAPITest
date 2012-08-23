package com.n4systems.fieldid.wicket.pages.event;

import com.n4systems.fieldid.service.PersistenceService;
import com.n4systems.fieldid.service.event.EventService;
import com.n4systems.fieldid.wicket.components.NonWicketLink;
import com.n4systems.model.AbstractEvent;
import com.n4systems.model.Event;
import com.n4systems.model.FileAttachment;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.string.StringValue;

import java.util.Collections;

public class PerformEventPage extends EventPage {

    @SpringBean private EventService eventService;
    @SpringBean private PersistenceService persistenceService;

    public PerformEventPage(PageParameters parameters) {
        Long assetId = parameters.get("assetId").toLongObject();
        Long type = parameters.get("type").toLongObject();

        StringValue scheduleIdString = parameters.get("scheduleId");
        if (!scheduleIdString.isEmpty()) {
            Long openEventId = scheduleIdString.toLongObject();
            event = new EventFromOpenEventModel(openEventId);
        } else {
            event = new NewMasterEventModel(assetId, type);
        }

        NonWicketLink backToStrutsLink = new NonWicketLink("backToStrutsLink", "eventAdd.action?assetId=" + assetId + "&type=" + type);
        add(backToStrutsLink);

        doAutoSchedule();
    }

    class NewMasterEventModel extends LoadableDetachableModel<Event> {

        private Long assetId;
        private Long type;

        public NewMasterEventModel(Long assetId, Long type) {
            this.assetId = assetId;
            this.type = type;
        }


        @Override
        protected Event load() {
            return eventService.createNewMasterEvent(assetId, type);
        }
    }

    class EventFromOpenEventModel extends LoadableDetachableModel<Event> {

        private Long openEventId;

        public EventFromOpenEventModel(Long openEventId) {
            this.openEventId = openEventId;
        }

        @Override
        protected Event load() {
            return eventService.createEventFromOpenEvent(openEventId);
        }
    }

    @Override
    protected AbstractEvent doSave() {
        event.getObject().storeTransientCriteriaResults();

        saveEventBookIfNecessary();

        Event savedEvent = eventCreationService.createEventWithSchedules((Event)event.getObject(), 0L, null, Collections.<FileAttachment>emptyList(), createEventScheduleBundles());

        return savedEvent;
    }

}
