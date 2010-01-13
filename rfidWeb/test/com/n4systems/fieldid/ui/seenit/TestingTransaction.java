/**
 * 
 */
package com.n4systems.fieldid.ui.seenit;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import com.n4systems.exceptions.NotImplementedException;
import com.n4systems.persistence.Transaction;

class TestingTransaction implements Transaction {
	public EntityManager entityManager;
	public EntityTransaction entityTransaction;
	
	public void commit() {
		invalidCall();
	}

	public EntityManager getEntityManager() {
		return entityManager;
	}

	public EntityTransaction getEntityTransaction() {
		return entityTransaction;
	}

	public void rollback() {
		invalidCall();
	}

	private void invalidCall() {
		throw new NotImplementedException("this type of transaction should not have calls to commit or rollback on it");
	}
}