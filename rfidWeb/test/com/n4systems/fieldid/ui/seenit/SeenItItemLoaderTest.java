package com.n4systems.fieldid.ui.seenit;


import static org.junit.Assert.*;

import javax.persistence.EntityManager;

import org.junit.Test;

import com.n4systems.exceptions.InvalidArgumentException;
import com.n4systems.model.ui.seenit.SeenItStorageItem;
import com.n4systems.testutils.DummyEntityManager;
import com.n4systems.util.persistence.QueryBuilder;


public class SeenItItemLoaderTest {
	
	private static final long SOME_USER_ID = 1L;

	@Test(expected=InvalidArgumentException.class)
	public void should_require_user_to_load_setup_wizard_use() throws Exception {
		new SeenItItemLoader().load(new TestingTransaction());
	}
	
	@Test
	public void should_create_a_query_builder_filtering_by_user_id() throws Exception {
		TestingTransaction transaction = new TestingTransaction();
		transaction.entityManager = new DummyEntityManager();
		SeenItItemLoaderWithOverriddenQueryExecution sut = new SeenItItemLoaderWithOverriddenQueryExecution();
		
		sut.setUserId(SOME_USER_ID).load(transaction);
		
		assertEquals(1, sut.capturedQueryBuilder.getWhereParameters().size());
		assertEquals(SOME_USER_ID, sut.capturedQueryBuilder.getWhereParameterValue("userId"));
	}
	
	
	private class SeenItItemLoaderWithOverriddenQueryExecution extends SeenItItemLoader {
		QueryBuilder<SeenItStorageItem> capturedQueryBuilder;
		protected SeenItStorageItem executeQuery(EntityManager em, QueryBuilder<SeenItStorageItem> queryBuilder) {
			capturedQueryBuilder = queryBuilder;
			return null;
		}
	}
}
