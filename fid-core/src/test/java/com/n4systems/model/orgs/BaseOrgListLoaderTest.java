package com.n4systems.model.orgs;

import com.n4systems.model.security.OpenSecurityFilter;
import com.n4systems.persistence.FieldIdTransaction;
import com.n4systems.persistence.Transaction;
import com.n4systems.tools.SillyPager;
import com.n4systems.util.persistence.QueryBuilder;
import com.n4systems.util.persistence.WhereParameter;
import com.n4systems.util.persistence.WhereParameter.Comparator;
import org.junit.Before;
import org.junit.Test;

import javax.persistence.EntityManager;
import java.util.ArrayList;

import static org.easymock.EasyMock.*;


public class BaseOrgListLoaderTest  {

	private static final String SOME_NAME_TO_LOOK_UP = "lookup Name";
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
	
	@SuppressWarnings("unchecked")
	@Test
	public void should_not_use_name_in_query() {
		//setup
				
		QueryBuilder<BaseOrg> mockQueryBuilder = createMock(QueryBuilder.class);
		expect(mockQueryBuilder.addOrder("name")).andReturn(mockQueryBuilder);
		expect(mockQueryBuilder.getPaginatedResults(mockEM, 1, 10)).andReturn(new SillyPager<BaseOrg>(new ArrayList<BaseOrg>()));
		replay(mockQueryBuilder);
		
		BaseOrgListLoaderTestExtention sut = new BaseOrgListLoaderTestExtention(new OpenSecurityFilter()); 
		sut.setQueryBuild(mockQueryBuilder);

		//execute
		sut.load(mockTransaction);
		
		
		//verify
		verify(mockTransaction);
		verify(mockQueryBuilder);
	}
	
	@SuppressWarnings("unchecked")
	@Test 
	public void should_use_name_in_query() {
		//setup
		
		QueryBuilder<BaseOrg> mockQueryBuilder = createMock(QueryBuilder.class);
		expect(mockQueryBuilder.addOrder("name")).andReturn(mockQueryBuilder);
		expect(mockQueryBuilder.addWhere(same(Comparator.LIKE), (String)anyObject(), same("name"), same(SOME_NAME_TO_LOOK_UP), same(WhereParameter.WILDCARD_BOTH | WhereParameter.TRIM | WhereParameter.IGNORE_CASE))).andReturn(mockQueryBuilder);
		expect(mockQueryBuilder.getPaginatedResults(mockEM, 1, 10)).andReturn(new SillyPager<BaseOrg>(new ArrayList<BaseOrg>()));
		replay(mockQueryBuilder);
		
		BaseOrgListLoaderTestExtention sut = new BaseOrgListLoaderTestExtention(new OpenSecurityFilter()); 
		sut.setQueryBuild(mockQueryBuilder);
		sut.setSearchName(SOME_NAME_TO_LOOK_UP);

		//execute
		sut.load(mockTransaction);
		
		
		//verify
		verify(mockTransaction);
		verify(mockQueryBuilder);
	}
	
	
}
