package com.n4systems.testutils;

import com.n4systems.persistence.Transaction;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

public class DummyTransaction implements Transaction {
	private DummyEntityManager em = new DummyEntityManager();
	private DummyEntityTransaction et = new DummyEntityTransaction();
	
	public EntityManager getEntityManager() {
		return em;
	}

	public EntityTransaction getEntityTransaction() {
		return et;
	}

	public void commit() {}
	
	public void rollback() {}

}
