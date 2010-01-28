package com.n4systems.persistence;

public interface TransactionManager {
	public Transaction startTransaction();
	public void rollbackTransaction(Transaction transaction);
	public void finishTransaction(Transaction transaction);
}
