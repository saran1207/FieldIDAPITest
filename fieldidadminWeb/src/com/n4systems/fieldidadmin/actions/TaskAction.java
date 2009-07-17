package com.n4systems.fieldidadmin.actions;

import java.util.List;

import org.apache.log4j.Logger;

import com.n4systems.model.taskconfig.TaskConfig;
import com.n4systems.model.taskconfig.TaskConfigListLoader;
import com.n4systems.model.taskconfig.TaskConfigLoader;
import com.n4systems.model.taskconfig.TaskConfigSaver;
import com.n4systems.services.Initializer;
import com.n4systems.taskscheduling.ScheduledTask;
import com.n4systems.taskscheduling.SchedulingException;
import com.n4systems.taskscheduling.TaskScheduler;
import com.n4systems.taskscheduling.TaskSchedulerBootstraper;

public class TaskAction extends AbstractAdminAction {
	private static final long serialVersionUID = 1L;
	private static Logger logger = Logger.getLogger(TaskAction.class);
	
	private final TaskScheduler scheduler = TaskScheduler.getInstance();
	
	private List<ScheduledTask> scheduledTasks;
	private List<TaskConfig> taskConfigs;
	
	private String configId;
	private Boolean enable;

	public String doLoad() {
		scheduledTasks = scheduler.getScheduledTasks();
		
		TaskConfigListLoader loader = new TaskConfigListLoader(null);
		taskConfigs = loader.load();
		
		return SUCCESS;
	}
	
	public String doEnableDisableTask() {

		TaskConfigLoader configLoader = new TaskConfigLoader();
	    configLoader.setId(configId);
	    
	    TaskConfig conf = configLoader.load();

	    if (conf == null) {
	    	addActionError("Could not load TaskConfig with id " + configId);
	    	return ERROR;
	    }
	    
	    conf.setEnabled(enable);
	    
	    try {
		    
	    	if (scheduler.isScheduled(configId)) {
	    		scheduler.unschedule(configId);
	    	}
	    	
	    	if (enable) {
		    	scheduler.schedule(conf);
		    }
		    
        } catch (SchedulingException e) {
        	addActionError("Could not schedule TaskConfig with id " + configId);
        	logger.error("Could not schedule TaskConfig with id " + configId, e);
        	
	    	return ERROR;
        }
        
        TaskConfigSaver configSaver = new TaskConfigSaver();
        
        configSaver.update(conf);
	    
		return SUCCESS;
	}
	
	public String doEnableDisableScheduler() {
		try {
			if (enable) {
				scheduler.start();
			} else {
				scheduler.stop();
			}
		} catch(Exception e) {
			addActionError("Could not enable/disable scheduler");
			logger.error("Could not enable/dislabe scheduler", e);
			return ERROR;
		}
		
		return SUCCESS;
	}
	
	public String doReloadScheduler() {
		Initializer taskInit = new TaskSchedulerBootstraper();
		taskInit.uninitialize();
		taskInit.initialize();
		
		return SUCCESS;
	}
	
	public List<ScheduledTask> getScheduledTasks() {
		return scheduledTasks;
	}

	public List<TaskConfig> getTaskConfigs() {
    	return taskConfigs;
    }
	
	public String getConfigId() {
    	return configId;
    }

	public void setConfigId(String configId) {
    	this.configId = configId;
    }

	public Boolean getEnable() {
    	return enable;
    }

	public void setEnable(Boolean enable) {
    	this.enable = enable;
    }
	
	public boolean isSchedulerEnabled() {
		return scheduler.isRunning();
	}
}
