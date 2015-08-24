package com.n4systems.fieldid.wicket.pages.masterevent;

import com.n4systems.fieldid.service.event.perform.PerformThingEventHelperService;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.fieldid.wicket.pages.asset.AssetSummaryPage;
import com.n4systems.model.*;
import com.n4systems.tools.FileDataContainer;
import org.apache.wicket.Component;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.ArrayList;

public class PerformMasterEventPage extends MasterEventPage {

    @SpringBean
    private PerformThingEventHelperService thingEventHelperService;

    private PerformMasterEventPage(Long scheduleId, Long assetId, Long typeId) {
        try {
            ThingEvent thingEvent = thingEventHelperService.createEvent(scheduleId, assetId, typeId);
            event = Model.of(thingEvent);

            setEventResult(event.getObject().getEventResult());
            fileAttachments = new ArrayList<FileAttachment>();

            doAutoSchedule();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public PerformMasterEventPage(IModel<ThingEvent> thingEvent) {
        event = thingEvent;
        setEventResult(event.getObject().getEventResult());
        fileAttachments = new ArrayList<FileAttachment>();
    }

    public PerformMasterEventPage(PageParameters parameters) {
        this(parameters.get("scheduleId").isEmpty() ? null : parameters.get("scheduleId").toLongObject(),
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
        event.getObject().getSubEvents().stream().forEach(subEvent -> subEvent.storeTransientCriteriaResults());
        event.getObject().setEventResult(getEventResult());

        FileDataContainer fileDataContainer = null;
        if (event.getObject().getType().isThingEventType()) {
            fileDataContainer = proofTestEditPanel.getFileDataContainer();
        }

        if (event.getObject().getType().isThingEventType()) {
            Asset asset =  ((ThingEvent)event.getObject()).getAsset();

            if (null != asset && null != event.getObject().getGpsLocation() && null != event.getObject().getGpsLocation().getLatitude() && null != event.getObject().getGpsLocation().getLongitude()) {
                asset.setGpsLocation(new GpsLocation(event.getObject().getGpsLocation().getLatitude(), event.getObject().getGpsLocation().getLongitude()));
            }
        }

        Event savedEvent = eventCreationService.createEventWithSchedules(event.getObject(), 0L, fileDataContainer, fileAttachments, createEventScheduleBundles());

        return savedEvent;
    }

    @Override
    protected Label createTitleLabel(String labelId) {
        return new Label(labelId, new FIDLabelModel("title.perform_master_event", event.getObject().getType().getDisplayName()));
    }
}
