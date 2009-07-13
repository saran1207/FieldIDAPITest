package com.n4systems.taskscheduling;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.jboss.logging.Logger;

public class ThreadPoolExecutorFactory {
	private static final Logger logger = Logger.getLogger(ThreadPoolExecutorFactory.class);
	
	/**
	 * These are required for ThreadPoolExecutor creation, however, are ignored since we're using
	 * unbounded queues and will never exceed corePoolSize
	 * @see  ThreadPoolExecutor
	 */
	public static final int POOL_MAX_SIZE = Integer.MAX_VALUE;
	public static final long POOL_KEEP_ALIVE_TIME = 0;					
	public static final TimeUnit POOL_KEEP_ALIVE_UNIT = TimeUnit.NANOSECONDS;
	
	/**
	 * Creates a ThreadPoolExecutor with corePoolSize = poolSize and backed by a LinkedBlockingQueue.  Name is used only for logging purposes.
	 * @param name		The name of this thread pool group
	 * @param poolSize	Size of this pool
	 * @see ThreadPoolExecutor
	 * @see LinkedBlockingQueue
	 * @return			ThreadPoolExecutor constructed with poolSize and defaults.
	 */
	public static ThreadPoolExecutor createUnboundExecutor(String name, int poolSize, ThreadFactory threadFactory) {
		logger.info("Creating Pool Executor: name [" + name + "], poolSize [" + poolSize + "]");
		return new ThreadPoolExecutor(poolSize, POOL_MAX_SIZE, POOL_KEEP_ALIVE_TIME, POOL_KEEP_ALIVE_UNIT, new LinkedBlockingQueue<Runnable>(), threadFactory);
	}
	
}
