package com.n4systems.persistence.savers.legacy;

import rfid.ejb.entity.UserBean;

import com.n4systems.ejb.PersistenceManager;
import com.n4systems.exceptions.NotImplementedException;
import com.n4systems.model.api.Saveable;
import com.n4systems.persistence.savers.Saver;
import com.n4systems.util.ServiceLocator;

/**
 * @deprecated Use {@link Saver} instead
 */
@Deprecated
abstract public class EntitySaver<T extends Saveable> {
	
	private final PersistenceManager pm;
	
	private UserBean modifiedBy;
	private Long modifiedById;
	
	public EntitySaver() {
		this(ServiceLocator.getPersistenceManager());
	}
	
	public EntitySaver(PersistenceManager pm) {
		this.pm = pm;
	}

	abstract protected void save(PersistenceManager pm, T entity);
	abstract protected T update(PersistenceManager pm, T entity);
	
	protected void remove(PersistenceManager pm, T entity) {
		throw new NotImplementedException();
	}
	
	public void save(T entity) {
		save(pm, entity);
	}
	
	public T update(T entity) {
		return update(pm, entity);
	}
	
	public void remove(T entity) {
		remove(pm, entity);
	}
	
	public T saveOrUpdate(T entity) {
		T savedEntity;
		if (entity.isNew()) {
			save(entity);
			savedEntity = entity;
		} else {
			savedEntity = update(entity);
		}
		
		return savedEntity;
	}

	public void setModifiedBy(UserBean modifiedBy) {
    	this.modifiedBy = modifiedBy;
    }

	public void setModifiedById(Long modifiedById) {
    	this.modifiedById = modifiedById;
    }

	protected Long getModifiedById() {
    	return (modifiedBy != null) ? modifiedBy.getId() : modifiedById;
    }
	
}
