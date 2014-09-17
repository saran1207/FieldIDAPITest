package com.n4systems.fieldid.wicket.pages.event;

import com.n4systems.fieldid.service.event.EventCriteriaEditService;
import com.n4systems.fieldid.service.event.EventService;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.fieldid.wicket.model.navigation.PageParametersBuilder;
import com.n4systems.model.Event;
import com.n4systems.model.EventResult;
import com.n4systems.model.FileAttachment;
import com.n4systems.model.PlaceEvent;
import com.n4systems.persistence.utils.PostFetcher;
import org.apache.wicket.Component;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.ArrayList;

public class EditPlaceEventPage extends PlaceEventPage {

    @SpringBean private EventService eventService;
    @SpringBean private EventCriteriaEditService criteriaEditService;

    private long uniqueId;

    public EditPlaceEventPage(PageParameters parameters) {
        uniqueId = parameters.get("uniqueID").toLong();
        event = Model.of(loadExistingEvent());
        event.getObject().setResultFromCriteriaAvailable();
        if(event.getObject().isResultFromCriteriaAvailable()) {
            setEventResult(EventResult.VOID);
        } else {
            setEventResult(event.getObject().getEventResult());
        }
        fileAttachments = new ArrayList<FileAttachment>(event.getObject().getAttachments());
    }

    protected PlaceEvent loadExistingEvent() {
        PlaceEvent existingEvent = eventService.lookupExistingEvent(PlaceEvent.class, uniqueId, false, true);
        PostFetcher.postFetchFields(existingEvent, Event.PLACE_FIELD_PATHS);
        return existingEvent;
    }

    @Override
    protected Component createCancelLink(String id) {
        return new BookmarkablePageLink<PlaceEventSummaryPage>(id, PlaceEventSummaryPage.class, PageParametersBuilder.id(uniqueId));
    }

    @Override
    protected PlaceEvent doSave() {
        saveEventBookIfNecessary();

        PlaceEvent editedEvent = event.getObject();
        editedEvent.setEventResult(getEventResult());
        criteriaEditService.storeCriteriaChanges(editedEvent);
        editedEvent.storeTransientCriteriaResults();
        return eventCreationService.updateEvent(editedEvent, null, fileAttachments);
    }

    @Override
    protected Label createTitleLabel(String labelId) {
        return new Label(labelId, new FIDLabelModel("title.edit_event"));
    }

}
