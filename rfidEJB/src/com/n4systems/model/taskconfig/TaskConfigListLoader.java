package com.n4systems.model.taskconfig;

import java.util.HashMap;
import java.util.List;

import com.n4systems.ejb.PersistenceManager;
import com.n4systems.persistence.loaders.legacy.ListLoader;
import com.n4systems.util.SecurityFilter;

//TODO: Update this class to extend FilteredListLoader
public class TaskConfigListLoader extends ListLoader<TaskConfig> {

	public TaskConfigListLoader(PersistenceManager pm, SecurityFilter filter) {
		super(pm, filter);
	}

	public TaskConfigListLoader(SecurityFilter filter) {
		super(filter);
	}

	@Override
	protected List<TaskConfig> load(PersistenceManager pm, SecurityFilter filter) {
		return pm.findAll(TaskConfig.class, "from " + TaskConfig.class.getName() + " t", new HashMap<String, Object>(0));
	}

}
