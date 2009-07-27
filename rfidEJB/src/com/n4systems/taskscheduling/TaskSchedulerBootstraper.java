package com.n4systems.taskscheduling;

import org.jboss.logging.Logger;

import com.n4systems.model.taskconfig.TaskConfig;
import com.n4systems.persistence.loaders.AllEntityListLoader;
import com.n4systems.services.Initializer;
import com.n4systems.taskscheduling.task.WatcherTask;

public class TaskSchedulerBootstraper implements Initializer {
	private static Logger logger = Logger.getLogger(TaskSchedulerBootstraper.class);
	
	public TaskSchedulerBootstraper() {}
	
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
