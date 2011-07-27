package com.n4systems.taskscheduling;

import org.apache.log4j.Logger;

import com.n4systems.model.taskconfig.TaskConfig;
import com.n4systems.persistence.loaders.AllEntityListLoader;
import com.n4systems.services.Initializer;
import com.n4systems.taskscheduling.task.WatcherTask;

public class TaskSchedulerBootstrapper implements Initializer {
	private static Logger logger = Logger.getLogger(TaskSchedulerBootstrapper.class);
	
	public TaskSchedulerBootstrapper() {}
	
	/**
	 * Creates and starts the TaskScheduler.  Registers the WatcherTask and schedules all TaskConfigs from the database.
	 * 
	 * @throws SchedulingException
	 */
	public void initialize() {
		TaskScheduler scheduler = TaskScheduler.getInstance();
		
		scheduler.start();
		
		try {
	        scheduler.register("watcher-task", "* * * * *", new WatcherTask(scheduler));
        } catch (SchedulingException e) {
        	logger.error("Could not schedule watcher task", e);
        }
		
		AllEntityListLoader<TaskConfig> loader = new AllEntityListLoader<TaskConfig>(TaskConfig.class);
		
		for (TaskConfig task: loader.load()) {
			try {
				if (task.isEnabled()) {
					scheduler.schedule(task);
				}
            } catch (SchedulingException e) {
            	logger.error("Could not schedule task " + task.getId(), e);
            }
		}
	}
	
	public void uninitialize() {	
		TaskScheduler scheduler = TaskScheduler.getInstance();
		
		if (scheduler.isRunning()) {
			scheduler.stop();
		}
		
		for (String taskId: scheduler.getScheduledTaskIds()) {
			scheduler.unschedule(taskId);
		}
	}
	
}
