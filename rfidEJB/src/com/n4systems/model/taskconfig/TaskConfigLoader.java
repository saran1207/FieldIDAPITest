package com.n4systems.model.taskconfig;

import com.n4systems.ejb.PersistenceManager;
import com.n4systems.persistence.loaders.legacy.EntityLoader;

//TODO: Update this class to extend Loader
public class TaskConfigLoader extends EntityLoader<TaskConfig> {
	private String id;
	
	public TaskConfigLoader() {
		super();
	}

	public TaskConfigLoader(PersistenceManager pm) {
		super(pm);
	}
	
	protected TaskConfig load(PersistenceManager pm) {
		TaskConfig config = pm.find(TaskConfig.class, id);
	    return (config != null) ? config : new TaskConfig();
    }
	
	public void setId(String id) {
		this.id = id;
	}
}
