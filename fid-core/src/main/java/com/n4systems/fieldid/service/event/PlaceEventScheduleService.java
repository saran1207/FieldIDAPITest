package com.n4systems.fieldid.service.event;

import com.n4systems.fieldid.service.FieldIdPersistenceService;
import com.n4systems.model.PlaceEvent;
import org.springframework.transaction.annotation.Transactional;

public class PlaceEventScheduleService extends FieldIdPersistenceService {

    @Transactional
    public PlaceEvent updateSchedule(PlaceEvent schedule) {
        PlaceEvent updatedSchedule = persistenceService.update(schedule);
        //Update org to notify mobile of change
        updatedSchedule.getPlace().touch();
        persistenceService.update(updatedSchedule.getPlace());
        return updatedSchedule;
    }

    @Transactional
    public Long createSchedule(PlaceEvent schedule) {
        Long id = persistenceService.save(schedule);
        //Update org to notify mobile of change
        schedule.getPlace().touch();
        persistenceService.update(schedule.getPlace());
        return id;
    }

}
