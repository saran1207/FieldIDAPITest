package com.n4systems.fieldid.wicket.pages.event;

import com.n4systems.fieldid.service.event.EventCriteriaEditService;
import com.n4systems.fieldid.service.event.EventService;
import com.n4systems.fieldid.wicket.components.NonWicketLink;
import com.n4systems.model.AbstractEvent;
import com.n4systems.model.Event;
import com.n4systems.model.FileAttachment;
import com.n4systems.model.ProofTestInfo;
import org.apache.wicket.Component;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.ArrayList;

public class EditEventPage extends EventPage {

    @SpringBean private EventService eventService;
    @SpringBean private EventCriteriaEditService criteriaEditService;

    private long uniqueId;

    public EditEventPage(PageParameters parameters) {
        uniqueId = parameters.get("uniqueID").toLong();
        event = Model.of(loadExistingEvent());

        fileAttachments = new ArrayList<FileAttachment>(event.getObject().getAttachments());
    }

    protected Event loadExistingEvent() {
        Event existingEvent = eventService.lookupExistingEvent(Event.class, uniqueId);
        if (existingEvent.getProofTestInfo() == null) {
            existingEvent.setProofTestInfo(new ProofTestInfo());
        }
        return existingEvent;
    }

    @Override
    protected Component createCancelLink(String id) {
        return new NonWicketLink(id, "event.action?uniqueID="+event.getObject().getId());
    }

    @Override
    protected AbstractEvent doSave() {
        Event editedEvent = event.getObject();
        criteriaEditService.storeCriteriaChanges(editedEvent);
        editedEvent.storeTransientCriteriaResults();
        return eventCreationService.updateEvent(editedEvent, proofTestEditPanel.getFileDataContainer(), fileAttachments);
    }
}
