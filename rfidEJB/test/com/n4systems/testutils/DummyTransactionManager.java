package com.n4systems.testutils;

import com.n4systems.persistence.Transaction;
import com.n4systems.persistence.TransactionManager;

public class DummyTransactionManager implements TransactionManager {

	@Override
	public void finishTransaction(Transaction transaction) {}

	@Override
	public void rollbackTransaction(Transaction transaction) {}

	@Override
	public Transaction startTransaction() {
		return new DummyTransaction();
	}

}
