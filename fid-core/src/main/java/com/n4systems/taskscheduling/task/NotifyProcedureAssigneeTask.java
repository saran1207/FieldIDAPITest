package com.n4systems.taskscheduling.task;

import com.n4systems.taskscheduling.ScheduledTask;
import com.n4systems.util.ServiceLocator;

import java.util.concurrent.TimeUnit;

/**
 * This is the ScheduledTask responsible for notifying the assignees of a scheduled Procedure that they have work to do.
 *
 * It should be scheduled to run every 5 minutes and executes a method in the NotifyProcedureAssigneeService to
 * send the notifications.
 *
 * Created by Jordan Heath on 15-04-27.
 */
public class NotifyProcedureAssigneeTask extends ScheduledTask {
    public NotifyProcedureAssigneeTask() {
        super(60 * 30, TimeUnit.SECONDS);
    }

    @Override
    protected void runTask() throws Exception {
        ServiceLocator.getProcedureAssigneeService().sendNotifications();
    }
}
