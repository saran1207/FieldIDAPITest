package com.n4systems.taskscheduling.task;

import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;

import com.n4systems.model.taskconfig.TaskConfig;
import com.n4systems.services.limiters.TenantLimitService;
import com.n4systems.taskscheduling.ScheduledTask;

public class TenantLimitUpdaterTask extends ScheduledTask {
	private static Logger logger = Logger.getLogger(TenantLimitUpdaterTask.class);
	
	public TenantLimitUpdaterTask() {
		super(2 * 60, TimeUnit.SECONDS);
	}

	@Override
	protected void runTask(TaskConfig config) throws Exception {
		logger.info("Initializing TenantLimitService ... ");
		TenantLimitService.getInstance().updateAll();
	}

}
