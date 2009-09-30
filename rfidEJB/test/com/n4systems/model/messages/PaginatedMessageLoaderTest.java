package com.n4systems.model.messages;

import static org.easymock.classextension.EasyMock.*;
import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import com.n4systems.model.security.OpenSecurityFilter;
import com.n4systems.persistence.FieldIdTransaction;
import com.n4systems.persistence.Transaction;
import com.n4systems.util.persistence.OrderClause;
import com.n4systems.util.persistence.QueryBuilder;
import com.n4systems.util.persistence.TestingQueryBuilder;


public class PaginatedMessageLoaderTest {
	
	private Transaction mockTransaction;
	
	@Before
	public void setUp() {
		mockTransaction = createMock(FieldIdTransaction.class);
		replay(mockTransaction);
	}
	
	@Test
	public void should_add_order_only_by_created_desc() throws Exception {
		
		PaginatedMessageLoaderTestExtention sut = new PaginatedMessageLoaderTestExtention(new OpenSecurityFilter());
														
		QueryBuilder<Message> testQueryBuilder = new TestingQueryBuilder<Message>(Message.class);
		sut.setTestQueryBuilder(testQueryBuilder);
		
		OrderClause expectedOrderBy = new OrderClause("created", false);
		
		sut.load(mockTransaction);
		
		assertEquals(1, testQueryBuilder.getOrderArguments().size());
		assertTrue(testQueryBuilder.getOrderArguments().contains(expectedOrderBy));
		
	}
	
}
