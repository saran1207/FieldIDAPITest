package com.n4systems.persistence;

public class MultipleUseTransactionManager implements TransactionManager {

	@Override
	public void finishTransaction(Transaction transaction) {
		PersistenceManager.finishTransaction(transaction);

	}

	@Override
	public void rollbackTransaction(Transaction transaction) {
		PersistenceManager.rollbackTransaction(transaction);

	}

	@Override
	public Transaction startTransaction() {
		return PersistenceManager.startTransaction();
	}

}
