package com.n4systems.persistence.archivers;

import java.util.Set;

import javax.persistence.EntityManager;

import rfid.ejb.entity.UserBean;

import com.n4systems.model.api.Archivable;
import com.n4systems.model.parents.AbstractEntity;

public class EntityListArchiver<T extends AbstractEntity & Archivable> {
	private final Class<T> archiveClass;
	private final Set<Long> ids;
	private final UserBean modifyUser;
	
	public EntityListArchiver(Class<T> archiveClass, Set<Long> ids, UserBean modifyUser) {
		this.archiveClass = archiveClass;
		this.modifyUser = modifyUser;
		this.ids = ids;
	}
	
	public EntityListArchiver(Class<T> archiveClass, Set<Long> ids) {
		this(archiveClass, ids, null);
	}
	
	public void archive(EntityManager em) {
		if (ids.isEmpty()) {
			return;
		}
		
		T archiveEntity;
		for (Long id: ids) {
			archiveEntity = em.find(archiveClass, id);
			archiveEntity.setModifiedBy(modifyUser);
			archiveEntity.archiveEntity();
			
			em.merge(archiveEntity);
		}	
	}
	
}
