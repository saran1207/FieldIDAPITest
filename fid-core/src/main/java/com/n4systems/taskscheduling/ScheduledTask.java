package com.n4systems.taskscheduling;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;

/**
 * The top-level class for Task execution.  Extending classes
 * must implement the abstract runTask method and a default no-argument
 * constructor.  Tasks may be asked to be canceled, in which case, 
 * the executing thread will be notified.  Implementations of runTask should
 * allow InterruptedException's to pass through (or at least re-throw them).
 */
abstract public class ScheduledTask implements Runnable {
	protected static final Logger logger = Logger.getLogger(ScheduledTask.class);
	
	private String configId;
	private String cronExpression;
	private Date lastExecutionDate;
	private Long lastTimeElapsed;
	private Exception lastException;
	private TaskStatus status = TaskStatus.NEVER_EXECUTED;
	private long startTime;
	
	/** The maximum execution time in milliseconds that a task may execute before being canceled */
	private long maxExecutionTime;
	
	/** the following are used in task canceling */
	private volatile Thread taskThread;
	private volatile boolean canceled;
	
	/**
	 * The single constructor for a Task.  Extending classes must provide a maxExecutionTime in milliseconds
	 * as a default value.  Negative values of maxExecutionTime will cause isPastMaxExecutionTime to always
	 * return false and will effectively disable long running Task cancellation.
	 * @param maxExecutionTime	The default maximum execution time in milliseconds
	 */
	protected ScheduledTask(long maxExecutionTime) {
		this.maxExecutionTime = maxExecutionTime;
	}
	
	/**
	 * The single constructor for a Task.  Extending classes must provide a maxExecutionTime in milliseconds
	 * as a default value.  Negative values of maxExecutionTime will 
	 * @param maxExecutionTime	The default maximum execution time in milliseconds
	 * @param unit				The unit of maximum execution time
	 */
	protected ScheduledTask(long maxExecutionTime, TimeUnit unit) {
		this(TimeUnit.MILLISECONDS.convert(maxExecutionTime, unit));
	}
	
	/**
	 * Provides the main implementation logic for this job.  Implementations of this method should
	 * allow InterruptedException's to pass through (or at least re-throw them).
	 * @throws Exception On job failure
	 */
	abstract protected void runTask() throws Exception;
	
	/** sets up the task for execution */
	private synchronized void taskPreExecute() {
	    status = TaskStatus.EXECUTING;
	    lastExecutionDate = new Date();
	    
	    logger.info("Task [" + configId + "] executing at " + lastExecutionDate);
	    
	    lastException = null;
	    startTime = System.currentTimeMillis(); 
	    canceled = false;
	    taskThread = Thread.currentThread();
    }

	/** sets fields after a successful execution */
	private synchronized void taskPostExecute() {
	    lastTimeElapsed = System.currentTimeMillis() - startTime;
	    status = TaskStatus.SUCESSFUL;
	    taskThread = null;
	    
	    logger.info("Task [" + configId + "] completed at " + (new Date()));
    }
	
	/** Implements the job launching logic.  Subclasses MUST NOT override this method. */
	public void run() {
		try {
			taskPreExecute();
			
			runTask();
			
			taskPostExecute();
			
		} catch(InterruptedException ie) {
			logger.info("Task [" + configId + "] interruped.", ie);
			if (canceled) {
				logger.info("Task [" + configId + "] was cancled during execution.");
				status = TaskStatus.CANCELED;
			}
			
		} catch(Exception e) {
			status = TaskStatus.FAILED;
			lastException = e;
			logger.info("Task [" + configId + "] failed", e);
		}
	}
	
	/**
	 * Cancels a task during execution.  The task is interrupted, and told to shutdown.
	 */
	public void cancel() {
		if (isExecuting()) {
			logger.info("Cancel called on active task [" + configId + "], elapsed run time [" + getCurrentTimeElapsed() + "ms]");
			synchronized(taskThread) {
				canceled = true;
				taskThread.interrupt();
			}
		}
	}
	
	public void setCanceled(boolean canceled) {
		this.canceled = canceled;
	}
	
	public boolean isCanceled() {
		return canceled;
	}
	
	public String getConfigId() {
    	return configId;
    }

	public void setConfigId(String configId) {
    	this.configId = configId;
    }

	public boolean isExecuting() {
		return (status == TaskStatus.EXECUTING);
	}

	public Date getLastExecutionDate() {
		return lastExecutionDate;
	}
	
	public Long getLastTimeElapsed() {
    	return lastTimeElapsed;
    }

	public Exception getLastException() {
		return lastException;
	}
	
	/**
	 * @return The elapsed execution time for an executing Task.  null if the task is not executing.
	 */
	public Long getCurrentTimeElapsed() {
		return isExecuting() ? System.currentTimeMillis() - startTime : null;
	}
	
	/**
	 * @return true if a task is currently executing and the {@link #getCurrentTimeElapsed()} > {@link #maxExecutionTime}
	 * and {@link #maxExecutionTime} is > -1;
	 */
	public boolean isPastMaxExecutionTime() {
		if (maxExecutionTime < 0) {
			return false;
		}
		
		Long elapsed = getCurrentTimeElapsed();
		return (elapsed != null) ? (elapsed > maxExecutionTime) : false;
	}
	
	public TaskStatus getStatus() {
		return status;
	}

	public long getMaxExecutionTime() {
		return maxExecutionTime;
	}
	
	public void setMaxExecutionTime(long maxExecutionTime) {
		this.maxExecutionTime = maxExecutionTime;
	}

	public String getCronExpression() {
    	return cronExpression;
    }

	public void setCronExpression(String cronPattern) {
    	this.cronExpression = cronPattern;
    }
}
