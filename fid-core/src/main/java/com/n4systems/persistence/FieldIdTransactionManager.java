package com.n4systems.persistence;

import javax.persistence.EntityManager;

public class FieldIdTransactionManager implements TransactionManager {
	private final EntityManager em;
	
	public FieldIdTransactionManager() {
		this(PersistenceManager.getEntityManager());
	}
	
	public FieldIdTransactionManager(EntityManager em) {
		this.em = em;
	}
	
	public Transaction startTransaction() {
		return new FieldIdTransaction(em);
	}
	
	public void rollbackTransaction(Transaction transaction) {
		if (transaction != null) {
			transaction.rollback();
		}
	}
	
	public void finishTransaction(Transaction transaction) {
		if (transaction != null) {
			transaction.commit();
		}
	}
	
}
