package com.n4systems.persistence.loaders;

import javax.persistence.EntityManager;

import com.n4systems.persistence.PersistenceManager;
import com.n4systems.persistence.Transaction;


abstract public class Loader<T> {
	protected abstract T load(EntityManager em);

	public T load() {
		return PersistenceManager.executeLoader(this);
	}
	
	public T load(Transaction transaction) {
		return load(transaction.getEntityManager());
	}
}
