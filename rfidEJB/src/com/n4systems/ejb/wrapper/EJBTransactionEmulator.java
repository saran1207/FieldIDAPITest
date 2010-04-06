package com.n4systems.ejb.wrapper;

import javax.persistence.EntityManager;

import com.n4systems.persistence.FieldIdTransactionManager;
import com.n4systems.persistence.TransactionManager;

public abstract class EJBTransactionEmulator<T> {
	protected final TransactionManager transactionManager = new FieldIdTransactionManager();

	protected abstract T createManager(EntityManager em);
}
