package com.n4systems.taskscheduling.task;

import com.n4systems.taskscheduling.ScheduledTask;
import com.n4systems.util.ServiceLocator;

import java.util.concurrent.TimeUnit;

public class NotifyAssigneesTask extends ScheduledTask {

    public NotifyAssigneesTask() {
        super(60 * 30, TimeUnit.SECONDS);
    }

    @Override
    protected void runTask() throws Exception {
        ServiceLocator.getEventAssigneeService().sendNotifications();
    }

}
