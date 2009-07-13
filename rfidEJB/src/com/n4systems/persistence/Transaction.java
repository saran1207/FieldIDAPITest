package com.n4systems.persistence;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

public class Transaction {
	private final EntityManager entityManager;
	private final EntityTransaction entityTransaction;
	
	public Transaction(final EntityManager manager) {
		this.entityManager = manager;
		this.entityTransaction = manager.getTransaction();
		entityTransaction.begin();
	}

	public EntityManager getEntityManager() {
		return entityManager;
	}

	public EntityTransaction getEntityTransaction() {
		return entityTransaction;
	}
	
	public void commit() {
		try {
			if (entityTransaction != null && entityTransaction.getRollbackOnly()) {
				entityTransaction.rollback();
			} else {
				entityTransaction.commit();
			}
		} finally {
			if (entityManager != null) {
				entityManager.close();
			}
		}
	}
}
