package com.n4systems.persistence;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

public class FieldIdTransaction implements Transaction {
	private final EntityManager entityManager;
	private final EntityTransaction entityTransaction;
	
	public FieldIdTransaction(final EntityManager manager) {
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
	
	public void rollback() {
		try {
			if (entityTransaction != null && entityTransaction.isActive()) {
				entityTransaction.rollback();
			}
		} finally {
			closeEntityManager();
		}
	}
	
	public void commit() {
		try {
			if (entityTransaction != null && entityTransaction.isActive()) {
				if (entityTransaction.getRollbackOnly()) {
					entityTransaction.rollback();
				} else {
					entityTransaction.commit();
				}
			}
		} finally {
			closeEntityManager();
		}
	}

	private void closeEntityManager() {
		if (entityManager != null && entityManager.isOpen()) {
			entityManager.close();
		}
	}
}
