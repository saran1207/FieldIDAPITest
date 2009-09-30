package com.n4systems.model.messages;

import static org.easymock.EasyMock.*;
import static org.easymock.classextension.EasyMock.*;
import static org.junit.Assert.*;

import javax.persistence.EntityManager;

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
	private EntityManager mockEM;
	
	@Before
	public void setUp() {
		mockEM = createMock(EntityManager.class);
		replay(mockEM);
		mockTransaction = createMock(FieldIdTransaction.class);
		expect(mockTransaction.getEntityManager()).andReturn(mockEM);
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
