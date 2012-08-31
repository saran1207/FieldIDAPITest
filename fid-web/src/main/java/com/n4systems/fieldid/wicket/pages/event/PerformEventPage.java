package com.n4systems.fieldid.wicket.pages.event;

import com.n4systems.fieldid.service.PersistenceService;
import com.n4systems.fieldid.service.event.EventService;
import com.n4systems.model.AbstractEvent;
import com.n4systems.model.Asset;
import com.n4systems.model.Event;
import com.n4systems.model.FileAttachment;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.Collections;

public class PerformEventPage extends EventPage {

    @SpringBean private EventService eventService;
    @SpringBean private PersistenceService persistenceService;

    private PerformEventPage(Long scheduleId, Long assetId, Long typeId) {
        if (scheduleId!=null) {
            event = new EventFromOpenEventModel(scheduleId);
        } else {
            event = new NewMasterEventModel(assetId, typeId);
        }

        doAutoSchedule();
    }

    public PerformEventPage(Event event, Asset asset) {
        this(event.getId(), asset.getId(), event.getType().getId());
    }

    public PerformEventPage(PageParameters parameters) {
        this(parameters.get("scheduleId").isEmpty()?null:parameters.get("scheduleId").toLongObject(),
                parameters.get("type").toLongObject(),
                parameters.get("assetId").toLongObject());
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
