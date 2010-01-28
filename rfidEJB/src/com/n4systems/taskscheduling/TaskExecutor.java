package com.n4systems.taskscheduling;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;

import org.jboss.logging.Logger;

import com.n4systems.util.ConfigContext;
import com.n4systems.util.ConfigEntry;
import com.n4systems.util.properties.HierarchicalProperties;
import com.n4systems.util.properties.HirarchicalPropertiesLoader;

/**
 * 
 */
public class TaskExecutor implements Executor {
	private static final Logger logger = Logger.getLogger(TaskExecutor.class);
	private static final String DEFAULT_POOL_NAME = "DEFAULT";
	
	private static TaskExecutor self;
	
	/** @return The singleton instance of this TaskExecutor */
	public static Executor getInstance() {
		if (self == null) {
			self = new TaskExecutor();
		}
		return self;
	}
	
	/** The default pool to use when no configuration has been specified for a task **/
	private final TaskPool defaultPool;
	private final List<TaskPool> taskPools = new ArrayList<TaskPool>();
	
	/** Use {@link TaskExecutor#getInstance()} */
	private TaskExecutor() {
		// create the default pool
		defaultPool = createDefaultExecutor();
		
		logger.info("Default pool initialized: " + defaultPool.toString());
		
		initialize();
	}
	
	/**
	 * Creates a default executor pool based on config settings
	 * @return	A default ThreadPoolExecutor
	 */
	private TaskPool createDefaultExecutor() {
		int defaultPoolSize = ConfigContext.getCurrentContext().getInteger(ConfigEntry.DEFAULT_EXECUTOR_POOL_SIZE);
		
		return new TaskPool(DEFAULT_POOL_NAME, defaultPoolSize);
	}
	
	/**
	 * Initializes the task pools from Properties configuration
	 */
	private void initialize() {
	    // load custom pool settings from a HirarchicalPropertiesLoader (with global overrides)
		HierarchicalProperties config = HirarchicalPropertiesLoader.loadGlobal(TaskExecutor.class);
		
		TaskPoolListInitializer taskPoolInit = new TaskPoolListInitializer();
		
		taskPools.addAll(taskPoolInit.initAll(config));
	}
	
	/**
	 * Searches the task pool list for a pool capible of executing the given
	 * task.  Returns the default pool if none could be found.
	 * @param task	A runnable task
	 * @see TaskPool#canExecute(Class)
	 * @see #defaultPool
	 * @return	The first pool capable of executing the task or the default pool
	 */
	private TaskPool findPoolForTask(Runnable task) {
		TaskPool pool = defaultPool;
		
		for (TaskPool customPool: taskPools) {
			if (customPool.canExecute(task.getClass())) {
				pool = customPool;
				break;
			}
		}
		
		return pool;
	}

	/**
	 * Executes a Runnable task sometime in the future.  The task will executed by the pool defined for it in 
	 * the task executor config file or the default pool if not defined.
	 */
	public void execute(Runnable task) {
		TaskPool pool = findPoolForTask(task);
		
		logger.info("Executing task [" + task.getClass().getSimpleName() + "], in pool [" + pool.toString() + "]");
		pool.getExecutor().execute(task);
    }
	
}
