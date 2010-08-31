/**
 * 
 */
package com.n4systems.handlers.creator;

import com.n4systems.exceptions.Defect;
import com.n4systems.persistence.Transaction;
import com.n4systems.persistence.TransactionManager;
import com.n4systems.util.persistence.TestingTransaction;

public final class TestDoubleTransactionManager implements TransactionManager {
	
	private TestingTransaction testingTransaction;

	
	
	@Override
	public Transaction startTransaction() {
		return getTransaction();
	}

	public Transaction getTransaction() {
		if (testingTransaction == null) {
			testingTransaction = new TestingTransaction();
		}
		return testingTransaction;
	}
	

	@Override
	public void rollbackTransaction(Transaction transaction) {
		gaurd(transaction);
		
		
	}

	private void gaurd(Transaction transaction) {
		if (transaction != testingTransaction) {
			throw new Defect("you shouldn't be passing in a transaction that wasn't created by this manager");
			
		}
	}

	@Override
	public void finishTransaction(Transaction transaction) {
		gaurd(transaction);
	}
}