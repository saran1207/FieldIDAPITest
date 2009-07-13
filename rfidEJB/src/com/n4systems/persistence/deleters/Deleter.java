package com.n4systems.persistence.deleters;

import javax.persistence.EntityManager;

import com.n4systems.persistence.PersistenceManager;
import com.n4systems.persistence.Transaction;

public abstract class Deleter {
	protected abstract void delete(EntityManager manager);
	
	public void delete() {
		PersistenceManager.executeDeleter(this);
	}
	
	public void delete(Transaction transaction) {
		delete(transaction.getEntityManager());
	}
}
