package com.n4systems.testutils;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import com.n4systems.persistence.Transaction;

public class DummyTransaction implements Transaction {
	private DummyEntityManager em = new DummyEntityManager();
	private DummyEntityTransaction et = new DummyEntityTransaction();
	
	@Override
	public EntityManager getEntityManager() {
		return em;
	}

	@Override
	public EntityTransaction getEntityTransaction() {
		return et;
	}

	@Override
	public void commit() {}
	
	@Override
	public void rollback() {}

}
