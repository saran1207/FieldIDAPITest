package com.n4systems.persistence.archivers;

import java.util.Set;

import javax.persistence.EntityManager;


import com.n4systems.model.api.Archivable;
import com.n4systems.model.parents.AbstractEntity;
import com.n4systems.model.user.User;

public class EntityListArchiver<T extends AbstractEntity & Archivable> {
	private final Class<T> archiveClass;
	private final Set<Long> ids;
	private final User modifyUser;
	
	public EntityListArchiver(Class<T> archiveClass, Set<Long> ids, User modifyUser) {
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
