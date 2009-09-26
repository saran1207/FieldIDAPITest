package com.n4systems.handlers;

import static org.easymock.classextension.EasyMock.*;

import com.n4systems.persistence.FieldIdTransaction;
import com.n4systems.persistence.Transaction;

public abstract class TestUsesTransactionBase {

	protected Transaction mockTransaction;

	public TestUsesTransactionBase() {
		super();
	}

	protected void mockTransaction() {
		mockTransaction = createMock(FieldIdTransaction.class);
		replay(mockTransaction);
	}

}