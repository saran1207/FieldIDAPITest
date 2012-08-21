package com.n4systems.model.location;

import com.n4systems.model.security.SecurityFilter;
import com.n4systems.model.security.TenantOnlySecurityFilter;
import com.n4systems.util.persistence.OrderClause;
import com.n4systems.util.persistence.QueryBuilder;
import com.n4systems.util.persistence.TestingQueryBuilder;
import com.n4systems.util.persistence.TestingTransaction;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.junit.Test;
import org.junit.internal.matchers.TypeSafeMatcher;

import static org.hamcrest.Matchers.hasItem;
import static org.junit.Assert.assertThat;


public class PredefinedLocationListLoaderTest {

	
	private final class PredefinedLocationListLoaderTestExtension extends PredefinedLocationListLoader {
		QueryBuilder<PredefinedLocation> queryBuilder;

		private PredefinedLocationListLoaderTestExtension(SecurityFilter filter) {
			super(filter);
		}

        @Override
        protected QueryBuilder<PredefinedLocation> createQueryBuilder(SecurityFilter filter) {
			queryBuilder = new TestingQueryBuilder<PredefinedLocation>(PredefinedLocation.class);
			return queryBuilder;
        }
    }

	@Test
	public void should_create_query_builder_with_order_by_id_when_parent_first_order_is_required() throws Exception {
		
		PredefinedLocationListLoaderTestExtension sut = new PredefinedLocationListLoaderTestExtension(new TenantOnlySecurityFilter(1L));
		
		sut.withParentFirstOrder().load(new TestingTransaction());
		
		
		assertThat(sut.queryBuilder, hasOrderByClause("id", true));
	}
	
	@Test
	public void should_create_query_builder_with_no_state_filter_archived_locations_are_required() throws Exception {
		
		PredefinedLocationListLoaderTestExtension sut = new PredefinedLocationListLoaderTestExtension(new TenantOnlySecurityFilter(1L));
		
		sut.withParentFirstOrder().load(new TestingTransaction());
		
		
		//assertThat(sut.queryBuilder, hasOrderByClause("id", true));
	}
	

	private Matcher<QueryBuilder<PredefinedLocation>> hasOrderByClause(final String field, final boolean ascending) {
		return new TypeSafeMatcher<QueryBuilder<PredefinedLocation>>() {
			private Matcher<Iterable<OrderClause>> matcher = hasItem(new OrderClause(field, ascending));

			@Override
			public boolean matchesSafely(QueryBuilder<PredefinedLocation> item) {
				return matcher.matches(item.getOrderArguments());
			}

			@Override
			public void describeTo(Description description) {
				description.appendText("order clauses to contain ");
				matcher.describeTo(description);
				
				
			}
		};
	}
}
