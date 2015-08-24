package com.n4systems.fieldid.wicket.pages.masterevent;

import com.n4systems.fieldid.service.event.EventCriteriaEditService;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.fieldid.wicket.pages.asset.AssetSummaryPage;
import com.n4systems.model.*;
import com.n4systems.persistence.utils.PostFetcher;
import com.n4systems.tools.FileDataContainer;
import org.apache.wicket.Component;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.ArrayList;

public class EditMasterEventPage extends MasterEventPage {

    @SpringBean
    private EventCriteriaEditService criteriaEditService;

    private long uniqueId;

    public EditMasterEventPage(PageParameters parameters) {
        uniqueId = parameters.get("uniqueID").toLong();
        event = Model.of(loadExistingEvent());
        event.getObject().setResultFromCriteriaAvailable();
        if(event.getObject().isResultFromCriteriaAvailable()) {
            setEventResult(EventResult.VOID);
        } else {
            setEventResult(event.getObject().getEventResult());
        }
        fileAttachments = new ArrayList<>(event.getObject().getAttachments());
    }

    public EditMasterEventPage(IModel<ThingEvent> masterEvent) {
        event = masterEvent;
        event.getObject().setResultFromCriteriaAvailable();
        if(event.getObject().isResultFromCriteriaAvailable()) {
            setEventResult(EventResult.VOID);
        } else {
            setEventResult(event.getObject().getEventResult());
        }
        fileAttachments = new ArrayList<>(event.getObject().getAttachments());
    }

    protected ThingEvent loadExistingEvent() {
        ThingEvent existingEvent = eventService.lookupExistingEvent(ThingEvent.class, uniqueId, false, true);
        PostFetcher.postFetchFields(existingEvent, Event.ALL_FIELD_PATHS_WITH_SUB_EVENTS);
        if (existingEvent.getType().isThingEventType()) {
            PostFetcher.postFetchFields(existingEvent, Event.THING_TYPE_PATHS);
        }
        return existingEvent;
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
        saveEventBookIfNecessary();
        event.getObject().setEventResult(getEventResult());

        criteriaEditService.storeCriteriaChanges(event.getObject());
        event.getObject().storeTransientCriteriaResults();

        event.getObject().getSubEvents().stream().forEach(subEvent -> criteriaEditService.storeCriteriaChanges(subEvent));
        event.getObject().getSubEvents().stream().forEach(subEvent -> subEvent.storeTransientCriteriaResults());

        boolean assetOwnerUpdate = getAssetOwnerUpdate();

        if (assetOwnerUpdate) {
            eventCreationService.updateAssetOwner(event.getObject());

        }

        FileDataContainer fileDataContainer = null;
        if (event.getObject().getType().isThingEventType()) {
            fileDataContainer = proofTestEditPanel.getFileDataContainer();
        }

        Event savedEvent = eventCreationService.updateEvent(event.getObject(), fileDataContainer, fileAttachments);

        return savedEvent;
    }

    @Override
    protected Label createTitleLabel(String labelId) {
        return new Label(labelId, new FIDLabelModel("title.perform_master_event", event.getObject().getType().getDisplayName()));
    }
}
