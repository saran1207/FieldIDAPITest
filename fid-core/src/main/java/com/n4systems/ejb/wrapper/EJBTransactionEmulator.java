package com.n4systems.ejb.wrapper;

import com.n4systems.persistence.FieldIdTransactionManager;
import com.n4systems.persistence.TransactionManager;

import javax.persistence.EntityManager;

public abstract class EJBTransactionEmulator<K> {
	protected final TransactionManager transactionManager = new FieldIdTransactionManager();

	protected abstract K createManager(EntityManager em);
}
