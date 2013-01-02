package com.n4systems.taskscheduling.task;

import com.n4systems.fieldid.service.schedule.RecurringScheduleService;
import com.n4systems.model.RecurringAssetTypeEvent;
import com.n4systems.taskscheduling.ScheduledTask;
import com.n4systems.util.ServiceLocator;
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
        logger.info("Starting RecurringSchedulesTask...");

        List<RecurringAssetTypeEvent> list = getRecurringScheduleService().getRecurringAssetTypeEvents();
        for(RecurringAssetTypeEvent event: list) {
            for (LocalDateTime dateTime : getRecurringScheduleService().getBoundedScheduledTimesIterator(event.getRecurrence())) {
                getRecurringScheduleService().scheduleAnEventFor(event, dateTime);            }
        }

        logger.info("...Ending RecurringSchedulesTask");
    }


}
