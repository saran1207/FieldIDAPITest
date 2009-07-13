package com.n4systems.persistence.savers;

import javax.persistence.EntityManager;

import com.n4systems.persistence.PersistenceManager;
import com.n4systems.persistence.Transaction;

public abstract class Saver<T> {
	protected abstract void save(EntityManager manager);
	protected abstract T update(EntityManager manager);
	
	public void save() {
		PersistenceManager.executeSaver(this);
	}

	public void save(Transaction transaction) {
		save(transaction.getEntityManager());
	}
	
	public T update() {
		return PersistenceManager.executeUpdater(this);
	}
	
	public T update(Transaction transaction) {
		return update(transaction.getEntityManager());
	}
	
}
