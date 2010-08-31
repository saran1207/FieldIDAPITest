package com.n4systems.handlers.creator;

import static com.n4systems.exceptions.Exceptions.*;

import com.n4systems.persistence.Transaction;
import com.n4systems.persistence.TransactionManager;

public abstract class BasicTransactionManagement {

	protected final TransactionManager transactionManager;


	public BasicTransactionManagement(TransactionManager transactionManager) {
		this.transactionManager = transactionManager;
	}

	protected void run() {
		Transaction transaction = transactionManager.startTransaction();
		
		try {
			doProcess(transaction);
			success();
		} catch (Exception e) {
			transactionManager.rollbackTransaction(transaction);
			failure(e);
			throw exceptionWrapper(e);
		} finally {
			transactionManager.finishTransaction(transaction);
			finished();
		}
	}

	protected RuntimeException exceptionWrapper(Exception e) {
		return convertToRuntimeException(e);
	}

	protected abstract void finished();

	protected abstract void failure(Exception e);

	protected abstract void success();

	protected abstract void doProcess(Transaction transaction);

}