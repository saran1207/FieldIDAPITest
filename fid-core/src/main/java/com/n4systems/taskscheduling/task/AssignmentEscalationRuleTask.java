package com.n4systems.taskscheduling.task;

import com.n4systems.taskscheduling.ScheduledTask;
import com.n4systems.util.ServiceLocator;

import java.util.concurrent.TimeUnit;

/**
 * This is the Scheduled Task stub that fires off the Assignment Escalation Rule notification process.  Nothing fancy
 * to see here.  All of the logic lives inside of the AssignmentEscalationRuleProcessingService class.
 *
 * Created by Jordan Heath on 2015-08-19.
 */
public class AssignmentEscalationRuleTask extends ScheduledTask {
    public AssignmentEscalationRuleTask()  {
        super(60 * 30, TimeUnit.SECONDS);
    }

    @Override
    protected void runTask() throws Exception {
        ServiceLocator.getAssignmentEscalationRuleProcessingService().processQueue();
    }
}
