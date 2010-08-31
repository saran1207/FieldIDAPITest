package com.n4systems.testutils;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import org.apache.commons.lang.NotImplementedException;

import com.n4systems.persistence.Transaction;

public class NoCommitAndRollBackTransaction implements Transaction {

	private DummyEntityManager em = new DummyEntityManager();
	private DummyEntityTransaction et = new DummyEntityTransaction();
	
	public EntityManager getEntityManager() {
		return em;
	}

	public EntityTransaction getEntityTransaction() {
		return et;
	}
	
	public void commit() {
		notImplemented();
	}

	public void rollback() {
		notImplemented();
	}
	
	private void notImplemented() {
		throw new NotImplementedException("This should not be called on this kind of transaction have a look at what you are testing.");
	}

	

	

}
