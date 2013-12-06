package com.n4systems.taskscheduling.task;

import com.n4systems.fieldid.service.schedule.RecurringScheduleService;
import com.n4systems.model.RecurringPlaceEvent;
import com.n4systems.taskscheduling.ScheduledTask;
import com.n4systems.util.ServiceLocator;
import com.n4systems.util.time.MethodTimer;
import org.apache.log4j.Logger;
import org.joda.time.LocalDateTime;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class RecurringPlaceSchedulesTask extends ScheduledTask {

    private static Logger logger = Logger.getLogger(RecurringSchedulesTask.class);

    public RecurringPlaceSchedulesTask() {
        super(30, TimeUnit.MINUTES);
    }

    public RecurringScheduleService getRecurringScheduleService() {
        return ServiceLocator.getRecurringScheduleService();
    }

    @Override
    protected void runTask() throws Exception {
        MethodTimer timer = new MethodTimer().withLogger(logger).start();

        List<RecurringPlaceEvent> list = getRecurringScheduleService().getRecurringPlaceEvents();
        for(RecurringPlaceEvent event: list) {
            for (LocalDateTime dateTime : getRecurringScheduleService().getBoundedScheduledTimesIterator(event.getRecurrence())) {
                getRecurringScheduleService().schedulePlaceAnEventFor(event, dateTime);
            }
        }

        timer.stop();
    }


}
