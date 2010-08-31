package com.n4systems.handlers.remover;

import com.n4systems.handlers.remover.summary.RemovalSummary;
import com.n4systems.persistence.Transaction;

public interface RemovalHandler<T extends RemovalSummary> {
	
	
	public void remove(Transaction transaction);
	
	public T summary(Transaction transaction);
}
