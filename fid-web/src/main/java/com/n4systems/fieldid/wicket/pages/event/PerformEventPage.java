package com.n4systems.fieldid.wicket.pages.event;

import com.n4systems.ejb.impl.EventScheduleManagerImpl;
import com.n4systems.fieldid.service.PersistenceService;
import com.n4systems.fieldid.service.event.EventScheduleService;
import com.n4systems.fieldid.service.event.EventService;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.fieldid.wicket.pages.asset.AssetSummaryPage;
import com.n4systems.model.*;
import com.n4systems.model.event.AssignedToUpdate;
import com.n4systems.persistence.utils.PostFetcher;
import com.n4systems.tools.FileDataContainer;
import org.apache.wicket.Component;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

public class PerformEventPage extends EventPage {

    @SpringBean private EventService eventService;
    @SpringBean private PersistenceService persistenceService;
    @SpringBean private EventScheduleService eventScheduleService;

    private PerformEventPage(Long scheduleId, Long assetId, Long typeId) {
        try {
            if (scheduleId != null) {
                Event openEvent = eventService.createEventFromOpenEvent(scheduleId);
                PostFetcher.postFetchFields(openEvent, Event.ALL_FIELD_PATHS_WITH_SUB_EVENTS);
                Event clonedEvent = (Event) openEvent.clone();
                eventService.populateNewEvent(clonedEvent);
                event = Model.of(clonedEvent);
            } else {
                Event newMasterEvent = eventService.createNewMasterEvent(assetId, typeId);
                eventService.populateNewEvent(newMasterEvent);
                event = Model.of(newMasterEvent);
            }

            setEventResult(event.getObject().getEventResult());
            fileAttachments = new ArrayList<FileAttachment>();

            doAutoSchedule();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
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
        event.getObject().setEventResult(getEventResult());

        FileDataContainer fileDataContainer = proofTestEditPanel.getFileDataContainer();

        Event savedEvent = eventCreationService.createEventWithSchedules(event.getObject(), 0L, fileDataContainer, fileAttachments, createEventScheduleBundles());

        updateRecurringAssetTypeEvent();

        return savedEvent;
    }

    private void updateRecurringAssetTypeEvent() {
        RecurringAssetTypeEvent recurringEvent = event.getObject().getRecurringEvent();

        List<Event> openEvents = null;
        Event uevent = event.getObject();

        if (null != recurringEvent && recurringEvent.getAutoAssign()) {

            openEvents = eventScheduleService.getAvailableSchedulesFor(event.getObject().getAsset());

            if (null != openEvents && openEvents.size() > 0) {

                Event nextSched = null;

                // if DAILY - if same day - continue
                // if DAILY - not same day - same time
                // find next schedule at same time next day - if same day check the next one
                if (recurringEvent.getRecurrence().getType() == RecurrenceType.DAILY) {

                    for (Event sched : openEvents) {

                        GregorianCalendar cal = (GregorianCalendar) Calendar.getInstance();
                        cal.setTime(sched.getDueDate());

                        GregorianCalendar ical = (GregorianCalendar) Calendar.getInstance();
                        ical.setTime(uevent.getDueDate());

                        boolean sameDay = cal.get(Calendar.YEAR) == ical.get(Calendar.YEAR) &&
                                cal.get(Calendar.DAY_OF_YEAR) == ical.get(Calendar.DAY_OF_YEAR);

                        boolean sameHour = cal.get(Calendar.HOUR_OF_DAY) == ical.get(Calendar.HOUR_OF_DAY);


                        if (sched.getDueDate().after(uevent.getDueDate()) && !sameDay && sameHour) {

                            nextSched = sched;
                            nextSched.setAssignee(event.getObject().getPerformedBy());
                            break;

                        }

                    }  // end for

                } else {
                    nextSched = openEvents.get(0);
                    nextSched.setAssignee(event.getObject().getPerformedBy());

                } // if DAILY

                if (null != nextSched) {
                    uevent = persistenceService.update(nextSched);
                }

            }

        }
    }

    @Override
    protected Label createTitleLabel(String labelId) {
        return new Label(labelId, new FIDLabelModel("title.perform_event"));
    }

}
