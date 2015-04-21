package com.n4systems.fieldid.service.event;

import com.n4systems.fieldid.service.FieldIdPersistenceService;
import com.n4systems.model.PlaceEvent;
import com.n4systems.model.notification.AssigneeNotification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

public class PlaceEventScheduleService extends FieldIdPersistenceService {

    @Autowired
    private NotifyEventAssigneeService notifyEventAssigneeService;

    @Transactional
    public PlaceEvent updateSchedule(PlaceEvent schedule) {
        if(schedule.isSendEmailOnUpdate() && schedule.getAssigneeOrDateUpdated()) {
            if(!notifyEventAssigneeService.notificationExists(schedule)) {
                AssigneeNotification assigneeNotification = new AssigneeNotification();
                assigneeNotification.setEvent(schedule);
                persistenceService.save(assigneeNotification);
                schedule.setAssigneeNotification(assigneeNotification);
            }
        }

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

    @Transactional
    public PlaceEvent retireSchedule(PlaceEvent schedule) {
        schedule.retireEntity();
        schedule = persistenceService.update(schedule);
        schedule.getPlace().touch();
        persistenceService.update(schedule.getPlace());
        return schedule;
    }

}
