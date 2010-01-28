package com.n4systems.persistence;

/** @deprecated Use FieldIdTransactionManager */
@Deprecated
public class StandardPersistenceProvider implements PersistenceProvider {

	public void finishTransaction(Transaction transaction) {
		PersistenceManager.finishTransaction(transaction);
	}

	public void rollbackTransaction(Transaction transaction) {
		PersistenceManager.rollbackTransaction(transaction);
	}

	public Transaction startTransaction() {
		return PersistenceManager.startTransaction();
	}

}
