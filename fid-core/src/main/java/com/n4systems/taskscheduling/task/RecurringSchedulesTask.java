package com.n4systems.taskscheduling.task;

import com.n4systems.fieldid.service.schedule.RecurringScheduleService;
import com.n4systems.model.RecurringAssetTypeEvent;
import com.n4systems.model.RecurringPlaceEvent;
import com.n4systems.model.procedure.RecurringLotoEvent;
import com.n4systems.taskscheduling.ScheduledTask;
import com.n4systems.util.ServiceLocator;
import com.n4systems.util.time.MethodTimer;
import org.apache.log4j.Logger;
import org.joda.time.LocalDateTime;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class RecurringSchedulesTask extends ScheduledTask {

    private static Logger logger = Logger.getLogger(RecurringSchedulesTask.class);

    public RecurringSchedulesTask() {
        super(60 * 30, TimeUnit.SECONDS);
    }

    public RecurringScheduleService getRecurringScheduleService() {
        return ServiceLocator.getRecurringScheduleService();
    }

    @Override
    protected void runTask() throws Exception {
        MethodTimer timer = new MethodTimer().withLogger(logger).start();

        List<RecurringAssetTypeEvent> recurringAssetTypeEventList = getRecurringScheduleService().getRecurringAssetTypeEvents();
        for(RecurringAssetTypeEvent event: recurringAssetTypeEventList) {
            for (LocalDateTime dateTime : getRecurringScheduleService().getBoundedScheduledTimesIterator(event.getRecurrence())) {
                getRecurringScheduleService().scheduleAnEventFor(event, dateTime);
            }
        }

        List<RecurringPlaceEvent> recurringPlaceEventList = getRecurringScheduleService().getAllRecurringPlaceEvents();
        for(RecurringPlaceEvent event: recurringPlaceEventList) {
            for (LocalDateTime dateTime : getRecurringScheduleService().getBoundedScheduledTimesIterator(event.getRecurrence())) {
                getRecurringScheduleService().scheduleAPlaceEventFor(event, dateTime);
            }
        }

        List<RecurringLotoEvent> recurringLotoEventList = getRecurringScheduleService().getAllRecurringLotoEvents();
        for(RecurringLotoEvent event: recurringLotoEventList) {
            for (LocalDateTime dateTime : getRecurringScheduleService().getBoundedScheduledTimesIterator(event.getRecurrence())) {
                if(event.isRecurringLockout()) {
                    getRecurringScheduleService().scheduleALotoEventFor(event, dateTime);
                } else {
                    getRecurringScheduleService().scheduleAnAuditEventFor(event, dateTime);
                }
            }
        }

        timer.stop();
    }


}
