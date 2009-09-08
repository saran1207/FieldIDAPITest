package com.n4systems.model.orgs;

import static org.easymock.EasyMock.*;
import static org.easymock.classextension.EasyMock.*;

import javax.persistence.EntityManager;

import org.junit.Before;
import org.junit.Test;

import com.n4systems.model.security.OpenSecurityFilter;
import com.n4systems.model.security.SecurityFilter;
import com.n4systems.persistence.Transaction;
import com.n4systems.util.persistence.QueryBuilder;


public class BaseOrgListLoaderTest  {

	private Transaction mockTransaction;
	
	@Before
	protected void mockTransaction() {
		EntityManager mockEM = createMock(EntityManager.class);
		replay(mockEM);
		mockTransaction = createMock(Transaction.class);
		expect(mockTransaction.getEntityManager()).andReturn(mockEM);
		replay(mockTransaction);
	}
	
	@Test
	public void should_not_use_name_in_query() {
		//setup
				
		BaseOrgListLoader sut = new BaseOrgListLoader(new OpenSecurityFilter()) {
								private QueryBuilder<BaseOrg> queryBuilder;
								
								@SuppressWarnings("unused")
								protected QueryBuilder<BaseOrg> getQueryBuild(SecurityFilter filter) {
									return queryBuilder;
								}
								@SuppressWarnings("unused")
								public void setQueryBuild(QueryBuilder<BaseOrg> builder) {
									this.queryBuilder = builder;
								}
						};
		//execute
		sut.load(mockTransaction);
		
		
		//verify
		verify(mockTransaction);
	}
	
	
}
