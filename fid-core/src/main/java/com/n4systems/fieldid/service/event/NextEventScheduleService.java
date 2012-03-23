package com.n4systems.fieldid.service.event;

import com.n4systems.fieldid.service.FieldIdPersistenceService;
import com.n4systems.model.EventSchedule;
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
    public EventSchedule createNextSchedule(EventSchedule schedule) {

        EventSchedule eventSchedule = findExistingSchedule(schedule);

        if (eventSchedule == null) {
            eventSchedule = eventScheduleService.updateSchedule(schedule);
        }

        return eventSchedule;
    }

    private EventSchedule findExistingSchedule(EventSchedule newSchedule) {
        List<EventSchedule> upcomingSchedules = eventScheduleService.getAvailableSchedulesFor(newSchedule.getAsset());

        for (EventSchedule upcomingSchedule : upcomingSchedules) {
            if (DateHelper.isEqualIgnoringTime(upcomingSchedule.getNextDate(), newSchedule.getNextDate())
                    && upcomingSchedule.getEventType().equals(newSchedule.getEventType())
                    && !upcomingSchedule.getStatus().equals(EventSchedule.ScheduleStatus.IN_PROGRESS)) {
                return upcomingSchedule;
            }
        }
        return null;
    }

}
