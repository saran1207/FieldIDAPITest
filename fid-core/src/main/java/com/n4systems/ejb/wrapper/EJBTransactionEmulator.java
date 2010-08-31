package com.n4systems.ejb.wrapper;

import javax.persistence.EntityManager;

import com.n4systems.persistence.FieldIdTransactionManager;
import com.n4systems.persistence.TransactionManager;

public abstract class EJBTransactionEmulator<K> {
	protected final TransactionManager transactionManager = new FieldIdTransactionManager();

	protected abstract K createManager(EntityManager em);
}
