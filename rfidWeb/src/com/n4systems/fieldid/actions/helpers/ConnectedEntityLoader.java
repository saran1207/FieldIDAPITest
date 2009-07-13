package com.n4systems.fieldid.actions.helpers;

import com.n4systems.ejb.PersistenceManager;
import com.n4systems.model.parents.EntityWithTenant;
import com.n4systems.util.SecurityFilter;

public class ConnectedEntityLoader {

	private PersistenceManager persistenceManager;

	public ConnectedEntityLoader(PersistenceManager persistenceManager) {
		this.persistenceManager = persistenceManager;
	}
	
	public <T extends EntityWithTenant> T getEntity(Class<T> clazz, Long id, T currentEntity, SecurityFilter filter) {
		try {
		if (id == null) {
			return null;
		} else if (currentEntity == null || !id.equals(currentEntity.getId())) {
			
				return persistenceManager.find(clazz, id, filter);
			
		} else {
			return currentEntity;
		}
		} catch (Exception e) {
			e.printStackTrace();// TODO: handle exception
			return null;
		}
	}
	
}
