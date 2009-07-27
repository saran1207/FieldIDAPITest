package com.n4systems.model.taskconfig;

import javax.persistence.EntityManager;

import com.n4systems.persistence.loaders.Loader;
import com.n4systems.util.persistence.QueryBuilder;

public class TaskConfigLoader extends Loader<TaskConfig> {
	private String id;
	
	public TaskConfigLoader() {}
	
	protected TaskConfig load(EntityManager em) {
		QueryBuilder<TaskConfig> builder = new QueryBuilder<TaskConfig>(TaskConfig.class);
		builder.addSimpleWhere("id", id);
		
		TaskConfig config = builder.getSingleResult(em);
	    
		return (config != null) ? config : new TaskConfig();
    }
	
	public void setId(String id) {
		this.id = id;
	}
}
