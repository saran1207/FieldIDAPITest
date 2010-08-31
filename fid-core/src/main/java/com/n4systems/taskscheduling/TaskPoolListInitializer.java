package com.n4systems.taskscheduling;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.n4systems.util.properties.HierarchicalProperties;


/**
 * Initializes a list of TaskPools from a HirarchicalProperties configuration.
 * 
 * Example config file:
 * 
 * pool.0.name=heavy_task
 * pool.0.size=2
 * pool.0.class.0=com.n4systems.taskscheduling.task.PrintAllInspectionCertificatesTask
 * 
 * pool.1.name=medium_task
 * pool.1.size=4
 * pool.1.class.0=com.n4systems.taskscheduling.task.PrintInspectionSummaryReportTask
 * 
 */
public class TaskPoolListInitializer {
	private static final Logger logger = Logger.getLogger(TaskPoolListInitializer.class);
	
	public TaskPoolListInitializer() {}
	
	/**
	 * Initializes a list of TaskPools from a HirarchicalProperties configuration (root level).
	 * @param config	Root level configuration
	 * @return			A list of initialized pools
	 */
	public List<TaskPool> initAll(HierarchicalProperties config) {
		List<TaskPool> taskPools = new ArrayList<TaskPool>();

		logger.info("Initializing task pools");
		
		for (HierarchicalProperties poolConfigs: config.getPropertiesList("pool")) {
				taskPools.add(initPool(poolConfigs));
		}

		logger.info("Initialized " + taskPools.size() + " pools");
		
		return taskPools;
    }
	
	/**
	 * Initializes a single TaskPool from a HirarchicalProperties configuration rooted at the task level.
	 * @param config	task level configuration
	 * @return			An initialized pool
	 */
	protected TaskPool initPool(HierarchicalProperties poolDef) {
		String name = poolDef.getString("name");
		int size = poolDef.getInteger("size");
		
		TaskPool pool = new TaskPool(name, size);
		
		logger.info("Initialized Pool: " + pool.toString());
		
		// pull the class list
		initClasses(poolDef.getProperties("class"), pool);

		return pool;
	}

	/**
	 * Initializes the class list from a HirarchicalProperties configuration rooted at the class level
	 * @param config	class level configuration
	 */
	@SuppressWarnings("unchecked")
    protected void initClasses(HierarchicalProperties classDefs, TaskPool pool) {
	    String className = null;
	    Class<?> taskClass;
	    
	    for (Object key: classDefs.keySet()) {
	    	try {
	    		className = classDefs.getString((String)key);
	            
	    		taskClass = Class.forName(className);
	            
	    		if (!Runnable.class.isAssignableFrom(taskClass)) {
	    			logger.warn("Taskclass [" + className + "] does not appear to be a subclass of Runnable");
	    			continue;
	    		}
	    		
	            pool.getExecutableTasks().add((Class<? extends Runnable>)taskClass);
	            logger.info("Added executable class to pool " + pool.getName() + ": " + taskClass.getName());
	            
	            
            } catch (ClassNotFoundException e) {
            	logger.warn("Unable to find Taskclass [" + className + "]", e);
            }
	    }
    }
	
	
	
}