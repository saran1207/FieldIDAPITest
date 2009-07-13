package com.n4systems.model.taskconfig;

import com.n4systems.ejb.PersistenceManager;
import com.n4systems.persistence.savers.legacy.EntitySaver;

public class TaskConfigSaver extends EntitySaver<TaskConfig> {

	@Override
    protected void save(PersistenceManager pm, TaskConfig entity) {
		pm.save(entity);	
    }

	@Override
	protected TaskConfig update(PersistenceManager pm, TaskConfig entity) {
		return pm.update(entity);
	}
}
