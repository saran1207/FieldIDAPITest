package com.n4systems.persistence.loaders;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.n4systems.model.builders.OrgBuilder;
import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.model.security.SecurityFilter;
import com.n4systems.testutils.DummyTransaction;
import com.n4systems.util.persistence.QueryBuilder;

public class FilteredIdLoaderTest {
	
	@Test
	public void testLoading() throws Exception {
		SecurityFilter securityFilter = createMock(SecurityFilter.class);
		
		@SuppressWarnings("unchecked")
		final QueryBuilder<BaseOrg> queryBuilder = createMock(QueryBuilder.class);
		
		DummyTransaction transaction = new DummyTransaction();
		
		FilteredIdLoader<BaseOrg> filteredIdLoader = new FilteredIdLoader<BaseOrg>(securityFilter, BaseOrg.class){
			@Override
			protected QueryBuilder<BaseOrg> createQueryBuilder(SecurityFilter filter) {
				return queryBuilder;
			}
		};
		
		BaseOrg org = OrgBuilder.aCustomerOrg().build();
		
		expect(queryBuilder.addSimpleWhere("id", 42L)).andReturn(queryBuilder);
		expect(queryBuilder.addPostFetchPaths(new String[0])).andReturn(queryBuilder);
		expect(queryBuilder.getSingleResult(transaction.getEntityManager())).andReturn(org);
		replay(queryBuilder);
		filteredIdLoader.setId(42L);
		BaseOrg result = filteredIdLoader.load(transaction);
		
		assertEquals(org, result);
	}

}
