package com.n4systems.taskscheduling.task;

import com.n4systems.persistence.CacheManager;
import com.n4systems.taskscheduling.ScheduledTask;

import java.util.concurrent.TimeUnit;

public class CacheLoggerTask extends ScheduledTask {

	public CacheLoggerTask() {
		super(60L, TimeUnit.SECONDS);
	}

	@Override
	protected void runTask() throws Exception {
		CacheManager.getInstance().logStats();
	}

}
