package com.n4systems.taskscheduling.task;

import com.n4systems.taskscheduling.ScheduledTask;
import com.n4systems.util.ServiceLocator;

import java.util.concurrent.TimeUnit;

public class NotifyProcedureAuthorizersTask extends ScheduledTask {

    public NotifyProcedureAuthorizersTask() {
        super(60 * 30, TimeUnit.SECONDS);
    }

    @Override
    protected void runTask() throws Exception {
        ServiceLocator.getProcedureAuthorizersService().notifyProcedureAuthorizers();
    }

}
