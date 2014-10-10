package com.n4systems.fieldid.service.event;

import com.n4systems.fieldid.service.FieldIdPersistenceService;
import com.n4systems.model.Event;
import com.n4systems.model.PlaceEvent;
import com.n4systems.model.ThingEvent;
import com.n4systems.util.DateHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public class NextEventScheduleService extends FieldIdPersistenceService {

    @Autowired
    private EventScheduleService eventScheduleService;

    @Autowired
    private PlaceEventScheduleService placeEventScheduleService;
    /**
     * Creates the next event schedule for the asset.  If there is already a schedule
     * for the contained asset and event type it will simply return that one.
     * @return The newly created schedule, or the already existing one.
     */
    @Transactional
    public Event createNextSchedule(ThingEvent openEvent) {

        Event eventSchedule = findExistingSchedule(openEvent);

        if (eventSchedule == null) {
            eventSchedule = eventScheduleService.updateSchedule(openEvent);
        }

        return eventSchedule;
    }

    private Event findExistingSchedule(ThingEvent newSchedule) {
        List<ThingEvent> openEvents = eventScheduleService.getAvailableSchedulesFor(newSchedule.getAsset());

        for (ThingEvent openEvent : openEvents) {
            if (DateHelper.isEqualIgnoringTime(openEvent.getDueDate(), newSchedule.getDueDate())
                    && openEvent.getEventType().equals(newSchedule.getEventType())) {
                return openEvent;
            }
        }
        return null;
    }

    @Transactional
    public Event createNextSchedule(PlaceEvent openEvent) {
        Event eventSchedule = findExistingSchedule(openEvent);

        if (eventSchedule == null) {
            eventSchedule = placeEventScheduleService.updateSchedule(openEvent);
        }

        return eventSchedule;
    }

    private Event findExistingSchedule(PlaceEvent newSchedule) {
        List<PlaceEvent> openEvents = eventScheduleService.getAvailableSchedulesFor(newSchedule.getPlace());

        for (PlaceEvent openEvent : openEvents) {
            if (DateHelper.isEqualIgnoringTime(openEvent.getDueDate(), newSchedule.getDueDate())
                    && openEvent.getEventType().equals(newSchedule.getEventType())) {
                return openEvent;
            }
        }
        return null;
    }

}
