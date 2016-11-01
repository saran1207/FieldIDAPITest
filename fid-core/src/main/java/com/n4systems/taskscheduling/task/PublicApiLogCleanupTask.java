package com.n4systems.taskscheduling.task;

import com.n4systems.taskscheduling.ScheduledTask;
import com.n4systems.util.ServiceLocator;

import java.util.concurrent.TimeUnit;

/**
 * Created by rrana on 2016-10-31.
 */
public class PublicApiLogCleanupTask extends ScheduledTask {

    public PublicApiLogCleanupTask() {
        super(15, TimeUnit.MINUTES);
    }

    @Override
    protected void runTask() throws Exception {
        ServiceLocator.getAuthService().clearRequestLog();
    }


}
