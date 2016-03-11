package com.n4systems.persistence.loaders;

import com.n4systems.persistence.PersistenceManager;
import com.n4systems.persistence.Transaction;

import javax.persistence.EntityManager;


abstract public class Loader<T> {
	public abstract T load(EntityManager em);

	public T load() {
		return PersistenceManager.executeLoader(this);
	}
	
	public T load(Transaction transaction) {
		return load(transaction.getEntityManager());
	}
}
