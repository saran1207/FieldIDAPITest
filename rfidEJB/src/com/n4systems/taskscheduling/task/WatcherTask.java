package com.n4systems.taskscheduling.task;

import com.n4systems.model.taskconfig.TaskConfig;
import com.n4systems.taskscheduling.ScheduledTask;
import com.n4systems.taskscheduling.TaskScheduler;

public class WatcherTask extends ScheduledTask {

	private final TaskScheduler scheduler;
	
	public WatcherTask(TaskScheduler scheduler) {
	    super(-1L);
	    this.scheduler = scheduler;
    }

	@Override
	protected void runTask(TaskConfig config) throws Exception {
		logger.trace("Looking for long running tasks");
		for (ScheduledTask task: scheduler.getExecutingTasks()) {
			if (task.equals(this)) {
				continue;
			}
			
			if (task.isPastMaxExecutionTime()) {
				logger.warn("Found long-running task [" + task.getConfigId() + "]");
				task.cancel();
			}
		}
	}

}
