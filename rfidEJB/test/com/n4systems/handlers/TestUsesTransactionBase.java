package com.n4systems.handlers;

import static org.easymock.EasyMock.*;

import com.n4systems.persistence.FieldIdTransaction;
import com.n4systems.persistence.Transaction;

public abstract class TestUsesTransactionBase {

	protected Transaction mockTransaction;

	protected void mockTransaction() {
		mockTransaction = createMock(FieldIdTransaction.class);
		replay(mockTransaction);
	}

}