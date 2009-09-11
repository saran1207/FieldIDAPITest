package com.n4systems.persistence.deleters;

import javax.persistence.EntityManager;

import com.n4systems.exceptions.EntityStillReferencedException;
import com.n4systems.model.api.Saveable;
import com.n4systems.persistence.PersistenceManager;
import com.n4systems.persistence.Transaction;

public class BaseDeleter<T extends Saveable> implements Deleter<T> {
	
	/**
	 * Subclasses should override this method if a non-default remove is required. 
	 */
	protected void remove(EntityManager em, T entity) {
		em.remove(entity);
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
}
