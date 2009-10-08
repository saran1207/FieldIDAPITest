package com.n4systems.persistence;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import com.n4systems.exceptions.NotImplementedException;

public class EntityManagerTransactionWrapper implements Transaction {

	private final EntityManager em;
	
	public EntityManagerTransactionWrapper(EntityManager em) {
		super();
		this.em = em;
	}

	public void commit() {
		throw new NotImplementedException();
	}

	public EntityManager getEntityManager() {
		return em;
	}

	public EntityTransaction getEntityTransaction() {
		throw new NotImplementedException();
	}

	public void rollback() {
		throw new NotImplementedException();
	}

}
