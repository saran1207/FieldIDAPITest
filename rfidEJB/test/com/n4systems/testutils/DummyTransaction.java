package com.n4systems.testutils;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import com.n4systems.persistence.Transaction;

public class DummyTransaction implements Transaction {

	private DummyEntityManager em = new DummyEntityManager();
	private DummyEntityTransaction et = new DummyEntityTransaction();
	
	public void commit() {}

	public EntityManager getEntityManager() {
		return em;
	}

	public EntityTransaction getEntityTransaction() {
		return et;
	}

	public void rollback() {}

}
