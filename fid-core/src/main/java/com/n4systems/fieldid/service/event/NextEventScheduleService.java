package com.n4systems.fieldid.service.event;

import com.n4systems.fieldid.service.FieldIdPersistenceService;
import com.n4systems.model.Event;
import com.n4systems.model.ThingEvent;
import com.n4systems.util.DateHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public class NextEventScheduleService extends FieldIdPersistenceService {

    private @Autowired EventScheduleService eventScheduleService;

    /**
     * Creates the next event schedule for the asset.  If there is already a schedule
     * for the contained asset and event type it will simply return that one.
     * @return The newly created schedule, or the already existing one.
     */
    @Transactional
    public Event createNextSchedule(Event openEvent) {

        Event eventSchedule = findExistingSchedule(openEvent);

        if (eventSchedule == null) {
            eventSchedule = eventScheduleService.updateSchedule(openEvent);
        }

        return eventSchedule;
    }

    private Event findExistingSchedule(Event newSchedule) {
        List<ThingEvent> openEvents = eventScheduleService.getAvailableSchedulesFor(newSchedule.getAsset());

        for (ThingEvent openEvent : openEvents) {
            if (DateHelper.isEqualIgnoringTime(openEvent.getDueDate(), newSchedule.getDueDate())
                    && openEvent.getEventType().equals(newSchedule.getEventType())) {
                return openEvent;
            }
        }
        return null;
    }

}
