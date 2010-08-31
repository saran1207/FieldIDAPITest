package com.n4systems.taskscheduling.task;


import com.n4systems.persistence.Transaction;
import com.n4systems.persistence.TransactionManager;

public abstract class TransactionalTask implements Runnable {
	public final TransactionManager transactionManager;
	
	public TransactionalTask(TransactionManager transactionManager) {
		this.transactionManager = transactionManager;
	}
	
	@Override
	public void run() {
		Transaction transaction = null;
		try {
			transaction = transactionManager.startTransaction();
			run(transaction);
			transactionManager.finishTransaction(transaction);
		} catch(Exception e) {
			transactionManager.rollbackTransaction(transaction);
			onException(e);
		}
	}

	protected abstract void run(Transaction transaction) throws Exception;
	protected abstract void onException(Exception e);
}
