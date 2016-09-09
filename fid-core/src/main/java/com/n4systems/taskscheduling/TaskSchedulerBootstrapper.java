package com.n4systems.taskscheduling;

import com.n4systems.services.Initializer;
import org.apache.log4j.Logger;

public class TaskSchedulerBootstrapper implements Initializer {
	private static Logger logger = Logger.getLogger(TaskSchedulerBootstrapper.class);
	
	public TaskSchedulerBootstrapper() {}
	
	/**
	 * Creates and starts the TaskScheduler.  Registers the WatcherTask and schedules all TaskConfigs from the database.
	 * 
	 * @throws SchedulingException
	 */
	public void initialize() {

		/*
		TaskScheduler scheduler = TaskScheduler.getInstance();
		
		scheduler.start();
		
		try {
			sched = schedFact.getScheduler();
			sched.start();
		} catch (Exception e) {
			System.err.println("QUARTZ ERROR!!!!! " + e.toString());
		}

		JobDetail job = newJob(GenericQuartzJob.class)
				.withIdentity("myJob", "group1")
				.bild();

		// Trigger the job to run now, and then every 40 seconds
		Trigger trigger = newTrigger()
				.withIdentity("myTrigger", "group1")
				.startNow()
				.withSchedule(simpleSchedule()
						.withIntervalInSeconds(10)
						.repeatForever())
				.build();

		try {
			// Tell quartz to schedule the job using our trigger
			sched.scheduleJob(job, trigger);
		} catch (Exception e) {
			System.err.println("QUARTZ ERROR!!!!! " + e.toString());
		}

		/*
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
		*/
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