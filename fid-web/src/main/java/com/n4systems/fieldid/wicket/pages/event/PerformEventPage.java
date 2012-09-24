package com.n4systems.fieldid.wicket.pages.event;

import com.n4systems.fieldid.service.PersistenceService;
import com.n4systems.fieldid.service.event.EventService;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.fieldid.wicket.pages.asset.AssetSummaryPage;
import com.n4systems.model.AbstractEvent;
import com.n4systems.model.Asset;
import com.n4systems.model.Event;
import com.n4systems.model.FileAttachment;
import com.n4systems.persistence.utils.PostFetcher;
import com.n4systems.tools.FileDataContainer;
import org.apache.wicket.Component;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.ArrayList;

public class PerformEventPage extends EventPage {

    @SpringBean private EventService eventService;
    @SpringBean private PersistenceService persistenceService;

    private PerformEventPage(Long scheduleId, Long assetId, Long typeId) {
        if (scheduleId != null) {
            Event openEvent = eventService.createEventFromOpenEvent(scheduleId);
            PostFetcher.postFetchFields(openEvent, Event.ALL_FIELD_PATHS_WITH_SUB_EVENTS);
            event = Model.of(openEvent);
        } else {
            event = Model.of(eventService.createNewMasterEvent(assetId, typeId));
        }

        fileAttachments = new ArrayList<FileAttachment>();

        doAutoSchedule();
    }

    public PerformEventPage(Event event, Asset asset) {
        this(event.getId(), asset.getId(), event.getType().getId());
    }

    public PerformEventPage(PageParameters parameters) {
        this(parameters.get("scheduleId").isEmpty()?null:parameters.get("scheduleId").toLongObject(),
				parameters.get("assetId").toLongObject(),
				parameters.get("type").toLongObject());
    }


    @Override
    protected Component createCancelLink(String id) {
        return new Link(id) {
            @Override public void onClick() {
                setResponsePage(new AssetSummaryPage(event.getObject().getAsset()));
            }
        };
    }

    @Override
    protected AbstractEvent doSave() {
        event.getObject().storeTransientCriteriaResults();

        saveEventBookIfNecessary();

        saveAssignedToIfNecessary();

        FileDataContainer fileDataContainer = proofTestEditPanel.getFileDataContainer();

        Event savedEvent = eventCreationService.createEventWithSchedules(event.getObject(), 0L, fileDataContainer, fileAttachments, createEventScheduleBundles());

        return savedEvent;
    }

    @Override
    protected Label createTitleLabel(String labelId) {
        return new Label(labelId, new FIDLabelModel("title.perform_event"));
    }

}
