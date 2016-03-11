package com.n4systems.persistence;

import com.n4systems.testutils.DummyEntityManager;
import org.junit.Before;
import org.junit.Test;

import javax.persistence.EntityManager;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;

public class FieldIdTransactionManagerTest {
	private EntityManager em;
	private FieldIdTransactionManager transMan;
	
	@Before
	public void setup() {
		em = new DummyEntityManager();
		transMan = new FieldIdTransactionManager(em);
	}
	
	@Test
	public void start_transaction_returns_transaction() {
		Transaction transaction = transMan.startTransaction();
		
		assertNotNull(transaction);
		assertSame(em, transaction.getEntityManager());
	}
	
	@Test
	public void finish_transaction_handles_null() {
		transMan.finishTransaction(null);
	}
	
	@Test
	public void finish_ends_transaction() {
		Transaction t = createMock(Transaction.class);
		t.commit();
		
		replay(t);
		transMan.finishTransaction(t);
		verify(t);
	}
	
	@Test
	public void rollback_rolls_back_transaction_handles_null() {
		Transaction t = createMock(Transaction.class);
		t.rollback();
		
		replay(t);
		transMan.rollbackTransaction(t);
		verify(t);
	}
	
	
}
