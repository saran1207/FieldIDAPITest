package com.n4systems.persistence;

public interface PersistenceProvider {
	public Transaction startTransaction();
	public void rollbackTransaction(Transaction transaction);
	public void finishTransaction(Transaction transaction);
		
}
