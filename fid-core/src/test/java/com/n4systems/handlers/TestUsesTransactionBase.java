package com.n4systems.handlers;

import com.n4systems.persistence.FieldIdTransaction;
import com.n4systems.persistence.Transaction;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.replay;

public abstract class TestUsesTransactionBase {

	protected Transaction mockTransaction;

	protected void mockTransaction() {
		mockTransaction = createMock(FieldIdTransaction.class);
		replay(mockTransaction);
	}

}