package com.n4systems.persistence.savers;

import javax.persistence.EntityManager;

import com.n4systems.model.api.Saveable;
import com.n4systems.persistence.PersistenceManager;
import com.n4systems.persistence.Transaction;

abstract public class Saver<T extends Saveable> {
	
	protected void save(EntityManager em, T entity) {
		em.persist(entity);
	}
	
	protected T update(EntityManager em, T entity) {
		return em.merge(entity);
	}
	
	protected void remove(EntityManager em, T entity) {
		em.remove(entity);
	}
	
	public void save(T entity) {
		PersistenceManager.executeSaver(this, entity);
	}

	public void save(Transaction transaction, T entity) {
		save(transaction.getEntityManager(), entity);
	}
	
	public T update(T entity) {
		return PersistenceManager.executeUpdater(this, entity);
	}
	
	public T update(Transaction transaction, T entity) {
		return update(transaction.getEntityManager(), entity);
	}
	
	public void remove(T entity) {
		PersistenceManager.executeDeleter(this, entity);
	}
	
	public void remove(Transaction transaction, T entity) {
		remove(transaction.getEntityManager(), entity);
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
	
	public T saveOrUpdate(Transaction transaction, T entity) {
		T savedEntity;
		if (entity.isNew()) {
			save(transaction, entity);
			savedEntity = entity;
		} else {
			savedEntity = update(transaction, entity);
		}
		
		return savedEntity;
	}
	
}
