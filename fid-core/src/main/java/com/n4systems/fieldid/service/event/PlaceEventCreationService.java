package com.n4systems.fieldid.service.event;

import com.n4systems.ejb.impl.EventScheduleBundle;
import com.n4systems.model.Event;
import com.n4systems.model.PlaceEvent;
import com.n4systems.model.RecurringPlaceEvent;
import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.tools.FileDataContainer;

public class PlaceEventCreationService extends EventCreationService<PlaceEvent, BaseOrg> {

    @Override
    protected PlaceEvent createEvent() {
        return new PlaceEvent();
    }

    @Override
    protected void setTargetFromScheduleBundle(PlaceEvent event, EventScheduleBundle<BaseOrg> bundle) {
        event.setPlace(bundle.getTarget());
    }

    @Override
    protected void preUpdateEvent(PlaceEvent event, FileDataContainer fileData) {
    }

    @Override
    protected void postUpdateEvent(PlaceEvent event, FileDataContainer fileData) {
        assignNextEventInSeries(event, EventEnum.PERFORM);
    }

    @Override
    protected void postSaveEvent(PlaceEvent event, FileDataContainer fileData) {
        assignNextEventInSeries(event, EventEnum.PERFORM);
    }

    @Override
    protected void doSaveSchedule(PlaceEvent openEvent) {
        nextEventScheduleService.createNextSchedule(openEvent);
    }

    public void assignNextEventInSeries(PlaceEvent event, EventEnum eventEnum) {
        Event nextEvent = null;
        Event uEvent = null;

        RecurringPlaceEvent recurringEvent = event.getRecurringEvent();

        if (recurringEvent != null && recurringEvent.isAutoAssign()) {
            nextEvent = eventScheduleService.getNextAvailableSchedule(event);

            if (eventEnum == EventEnum.PERFORM) {
                nextEvent.setAssignee(event.getPerformedBy());
            } else if (eventEnum == EventEnum.CLOSE) {
                nextEvent.setAssignee(event.getAssignee());
            }
        }
    }

}
