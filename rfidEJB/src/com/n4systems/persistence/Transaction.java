package com.n4systems.persistence;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

public interface Transaction {

	public EntityManager getEntityManager();

	public EntityTransaction getEntityTransaction();

	public void rollback();

	public void commit();

}