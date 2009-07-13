package com.n4systems.taskscheduling;

import java.lang.Thread.UncaughtExceptionHandler;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicLong;

import org.apache.log4j.Logger;

public class TaskThreadFactory implements ThreadFactory, UncaughtExceptionHandler {
	private Logger logger = Logger.getLogger(TaskThreadFactory.class);
	
	private static final String THREAD_GROUP_PREFIX = "group-";
	private static final String THREAD_NAME_PREFIX = "thread-";
	private static final AtomicLong threadNumber = new AtomicLong(1);
	
	private final String poolName;
	private final ThreadGroup threadGroup; 
	
	public TaskThreadFactory(String poolName) {
		this.poolName = poolName;
		threadGroup = new ThreadGroup(THREAD_GROUP_PREFIX + poolName);
	}
	
	public Thread newThread(Runnable task) {
		String taskClassName = task.getClass().getSimpleName();
		
		String threadName = THREAD_NAME_PREFIX + poolName + "-" + taskClassName + "-" + threadNumber.getAndIncrement();
		
		Thread thread = new Thread(threadGroup, task, threadName);
		thread.setDaemon(false);
		thread.setUncaughtExceptionHandler(this);
		
		logger.info("Created thread: " + threadName);
		
		return thread;
	}

	public void uncaughtException(Thread thread, Throwable throwable) {
		logger.error("Task: " + thread.getName() + " terminated unexpectedly", throwable);
    }
}
