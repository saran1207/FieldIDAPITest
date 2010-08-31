package com.n4systems.handlers.creator;

import com.n4systems.persistence.Transaction;

public interface ReversableCreateHandler<T> extends CreateHandler {
	
	public T createWithUndoInformation(Transaction transaction);
	public void undo(Transaction transaction, T creationResult);
}
