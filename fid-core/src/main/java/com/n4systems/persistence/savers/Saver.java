package com.n4systems.persistence.savers;

import com.n4systems.exceptions.EntityStillReferencedException;
import com.n4systems.model.api.Saveable;
import com.n4systems.persistence.PersistenceManager;
import com.n4systems.persistence.Transaction;
import com.n4systems.persistence.deleters.Deleter;

import javax.persistence.EntityManager;

abstract public class Saver<T extends Saveable> implements Deleter<T> {
	
	/**
	 * Subclasses should override this method if a non-default persist is required. 
	 */
	public void save(EntityManager em, T entity) {
		em.persist(entity);
	}
	
	/**
	 * Subclasses should override this method if a non-default update is required. 
	 */
	public T update(EntityManager em, T entity) {
		return em.merge(entity);
	}
	
	/**
	 * Subclasses should override this method if a non-default remove is required. 
	 */
	public void remove(EntityManager em, T entity) {
		PersistenceManager.reattach(em, entity);
		em.remove(entity);
	}
	
	/**
	 * Saves an entity.
	 */
	public void save(T entity) {
		PersistenceManager.executeSaver(this, entity);
	}

	/**
	 * Saves an entity using an existing Transaction.
	 */
	public void save(Transaction transaction, T entity) {
		save(transaction.getEntityManager(), entity);
	}
	
	/**
	 * Updates an entity.
	 */
	public T update(T entity) {
		return PersistenceManager.executeUpdater(this, entity);
	}
	
	/**
	 * Updates an entity using an existing Transaction.
	 */
	public T update(Transaction transaction, T entity) {
		return update(transaction.getEntityManager(), entity);
	}
	
	/**
	 * Removes an entity.
	 */
	public void remove(T entity) throws EntityStillReferencedException {
		PersistenceManager.executeDeleter(this, entity);
	}
	
	/**
	 * Removes an entity using an existing Transaction.
	 */
	public void remove(Transaction transaction, T entity) {
		remove(transaction.getEntityManager(), entity);
	}
	
	/**
	 * Saves or Updates an entity depending on its {@link Saveable#isNew()} flag.
	 */
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
	
	/**
	 * Saves or Updates an entity depending on its {@link Saveable#isNew()} flag using an existing transaction.
	 */
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
