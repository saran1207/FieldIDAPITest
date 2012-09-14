package com.n4systems.fieldid.wicket.pages.event;

import com.n4systems.fieldid.service.event.EventCriteriaEditService;
import com.n4systems.fieldid.service.event.EventService;
import com.n4systems.model.AbstractEvent;
import com.n4systems.model.Event;
import com.n4systems.model.ProofTestInfo;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;

public class EditEventPage extends EventPage {

    @SpringBean private EventService eventService;
    @SpringBean private EventCriteriaEditService criteriaEditService;

    private long uniqueId;

    public EditEventPage(PageParameters parameters) {
        uniqueId = parameters.get("uniqueID").toLong();
        event = new ExistingEventModel();
    }

    class ExistingEventModel extends LoadableDetachableModel<Event> {
        @Override
        protected Event load() {
            Event existingEvent = eventService.lookupExistingEvent(Event.class, uniqueId);
            if (existingEvent.getProofTestInfo() == null) {
                existingEvent.setProofTestInfo(new ProofTestInfo());
            }
            return existingEvent;
        }
    }

    @Override
    protected AbstractEvent doSave() {
        Event editedEvent = event.getObject();
        criteriaEditService.storeCriteriaChanges(editedEvent);
        editedEvent.storeTransientCriteriaResults();
        return eventCreationService.updateEvent(editedEvent, proofTestEditPanel.getFileDataContainer());
    }
}
