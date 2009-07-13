package com.n4systems.taskscheduling;

import com.n4systems.model.taskconfig.TaskConfig;
import com.n4systems.model.taskconfig.TaskConfigSaver;

import it.sauronsoftware.cron4j.Scheduler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jboss.logging.Logger;

public class TaskScheduler {
	private static final Logger logger = Logger.getLogger(TaskScheduler.class);
	
	private static TaskScheduler self;
	
	public static TaskScheduler getInstance() {
		if (self == null) {
			self = new TaskScheduler();
		}
		
		return self;
	}
	
	private final Scheduler scheduler;
	private final Map<String, Object> scheduledJobs = new HashMap<String, Object>();
	private boolean running;
	
	public TaskScheduler() {
		this(new Scheduler());
	}
	
	public TaskScheduler(Scheduler scheduler) {
		this.scheduler = scheduler;
	}
	
	public void start() {
		synchronized(scheduler) {
			scheduler.start();
			running = true;
		}
	}
	
	public void stop() {
		synchronized(scheduler) {
			for (ScheduledTask task: getExecutingTasks()) {
				task.cancel();
			}
	
			scheduler.stop();
			running = false;
		}
	}
	
	private Object getSchedulerKey(String id) {
		return scheduledJobs.get(id);
	}
	
	public List<String> getScheduledTaskIds() {
		return new ArrayList<String>(scheduledJobs.keySet());
	}
	
	public boolean isScheduled(String id) {
		return scheduledJobs.containsKey(id);
	}
	
	public ScheduledTask getScheduledTask(String id) {
		Object key = getSchedulerKey(id);
		return (key != null) ? (ScheduledTask)scheduler.getTaskRunnable(key) : null;
	}
	
	public List<ScheduledTask> getExecutingTasks() {
		List<ScheduledTask> execTasks = new ArrayList<ScheduledTask>();
		
		ScheduledTask task;
		for (String id: getScheduledTaskIds()) {
			task = getScheduledTask(id);
			if (task.isExecuting()) {
				execTasks.add(task);
			}
		}
		
		return execTasks;
	}
	
	public List<ScheduledTask> getScheduledTasks() {
		List<ScheduledTask> scheduledTasks = new ArrayList<ScheduledTask>();
		
		for (String id: getScheduledTaskIds()) {
			scheduledTasks.add(getScheduledTask(id));
		}
		
		return scheduledTasks;
	}
	
	public List<String> getExecutingTaskIds() {
		List<String> execTasksIds = new ArrayList<String>();
		
		for (String id: getScheduledTaskIds()) {
			if (getScheduledTask(id).isExecuting()) {
				execTasksIds.add(id);
			}
		}
		
		return execTasksIds;
	}
	
	@SuppressWarnings("unchecked")
    public void schedule(TaskConfig config) throws SchedulingException {
		try {
			TaskConfigSaver saver = new TaskConfigSaver();
			saver.saveOrUpdate(config);
			
			Class<ScheduledTask> taskClass = (Class<ScheduledTask>)Class.forName(config.getClassName());
			ScheduledTask task = taskClass.newInstance();
			
			register(config.getId(), config.getCronExpression(), task);
		} catch(Exception e) {
			throw new SchedulingException("Unable to schedule task " + config.getId(), e);
		}
	}
	
	public <T extends ScheduledTask> void register(String id, String cronExpression, T task) throws SchedulingException {
		synchronized(scheduledJobs) {
			try {
				if (isScheduled(id)) {
					throw new DuplicateTaskIdException(id);
				} else {
					task.setConfigId(id);
					task.setCronExpression(cronExpression);
					
					scheduledJobs.put(id, scheduler.schedule(cronExpression, task));
					logger.info("Scheduled Task: [" + id + "], cron [" + cronExpression + "], class [" + task.getClass().getSimpleName() + "]");
				}
			} catch(Exception e) {
				throw new SchedulingException("Unable to schedule task " + id, e);
			}
		}
	}
	
	public ScheduledTask unschedule(String id) {
		ScheduledTask task = null;
		
		synchronized(scheduledJobs) {
			if (isScheduled(id)) {
				Object schedulerKey = getSchedulerKey(id); 
				
				task = (ScheduledTask)scheduler.getTaskRunnable(schedulerKey);
				scheduler.deschedule(schedulerKey);
				scheduledJobs.remove(id);
				
				logger.info("Unscheduled Task: [" + id + "], class [" + task.getClass().getSimpleName() + "]");
			}
		}
		
		return task;
	}
	
	public void killTask(String id) {
		logger.info("Killing Task: [" + id + "]");
		getScheduledTask(id).cancel();
	}
	
	public boolean isRunning() {
		return running;
	}
}
