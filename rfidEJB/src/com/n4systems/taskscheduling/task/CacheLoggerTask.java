package com.n4systems.taskscheduling.task;

import java.util.concurrent.TimeUnit;

import com.n4systems.persistence.CacheManager;
import com.n4systems.taskscheduling.ScheduledTask;

public class CacheLoggerTask extends ScheduledTask {

	public CacheLoggerTask() {
		super(60L, TimeUnit.SECONDS);
	}

	@Override
	protected void runTask() throws Exception {
		CacheManager.getInstance().logStats();
	}

}
