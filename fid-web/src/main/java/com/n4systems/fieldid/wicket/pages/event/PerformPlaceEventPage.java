package com.n4systems.fieldid.wicket.pages.event;

import com.n4systems.fieldid.service.PersistenceService;
import com.n4systems.fieldid.service.event.EventScheduleService;
import com.n4systems.fieldid.service.event.EventService;
import com.n4systems.fieldid.service.event.perform.PerformPlaceEventHelperService;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.fieldid.wicket.model.navigation.PageParametersBuilder;
import com.n4systems.fieldid.wicket.pages.org.PlaceSummaryPage;
import com.n4systems.model.AbstractEvent;
import com.n4systems.model.Event;
import com.n4systems.model.FileAttachment;
import com.n4systems.model.PlaceEvent;
import com.n4systems.tools.FileDataContainer;
import org.apache.wicket.Component;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.ArrayList;

public class PerformPlaceEventPage extends PlaceEventPage {

    @SpringBean private EventService eventService;
    @SpringBean private PersistenceService persistenceService;
    @SpringBean private EventScheduleService eventScheduleService;

    @SpringBean private PerformPlaceEventHelperService placeEventHelperService;

    public PerformPlaceEventPage(Long scheduleId, Long orgId, Long typeId) {
        try {
            PlaceEvent placeEvent = placeEventHelperService.createEvent(scheduleId, orgId, typeId);
            event = Model.of(placeEvent);

            setEventResult(event.getObject().getEventResult());
            fileAttachments = new ArrayList<FileAttachment>();

            doAutoSchedule();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public PerformPlaceEventPage(PageParameters parameters) {
        this(parameters.get("scheduleId").isEmpty()?null:parameters.get("scheduleId").toLongObject(),
                parameters.get("placeId").toLongObject(),
                parameters.get("type").toLongObject());
    }

    @Override
    protected Component createCancelLink(String id) {
        return new BookmarkablePageLink<Void>(id, PlaceSummaryPage.class, PageParametersBuilder.id(event.getObject().getPlace().getId()));
    }

    @Override
    protected AbstractEvent doSave() {
        event.getObject().storeTransientCriteriaResults();
        event.getObject().setEventResult(getEventResult());

        FileDataContainer fileDataContainer = null;
        if (event.getObject().getType().isThingEventType()) {
            fileDataContainer = proofTestEditPanel.getFileDataContainer();
        }

        Event savedEvent = eventCreationService.createEventWithSchedules(event.getObject(), 0L, fileDataContainer, fileAttachments, createEventScheduleBundles());

        return savedEvent;
    }


    @Override
    protected Component createTitleLabel(String labelId) {
        return new Label(labelId, new FIDLabelModel("title.perform_place_event"));
    }
}
