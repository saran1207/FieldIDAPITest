package com.n4systems.model.eventschedule;

import static org.junit.Assert.*;

import javax.persistence.EntityManager;

import com.n4systems.model.EventSchedule;
import org.junit.Test;

import com.n4systems.model.security.OpenSecurityFilter;
import com.n4systems.model.security.SecurityFilter;
import com.n4systems.util.persistence.QueryBuilder;
import com.n4systems.util.persistence.TestingQueryBuilder;
import com.n4systems.util.persistence.TestingTransaction;
import com.n4systems.util.persistence.WhereParameter;
import com.n4systems.util.persistence.WhereParameter.Comparator;

public class EventScheduleByGuidOrIdLoaderTest {
	
	@Test
	public void should_try_to_lookup_by_mobile_guid_when_set() {
		String mobileGuid = "SOME GUID";
		
		TestableEventScheduleByGuidOrIdLoader sut = new TestableEventScheduleByGuidOrIdLoader(new OpenSecurityFilter());
		sut.setMobileGuid(mobileGuid).load(new TestingTransaction());
		
		WhereParameter<?> whereParameter = (WhereParameter<?>)sut.queryBuilder.getWhereParameter("mobileGUID");
		assertEquals(Comparator.EQ, whereParameter.getComparator());
		assertEquals(mobileGuid, whereParameter.getValue());
	}
	
	@Test
	public void should_try_by_id_if_mobile_guid_not_set() {
		TestableEventScheduleByGuidOrIdLoader sut = new TestableEventScheduleByGuidOrIdLoader(new OpenSecurityFilter());
		sut.setId(1L).load(new TestingTransaction());
		
		assertTrue(sut.findByIdCalled);
	}

	private class TestableEventScheduleByGuidOrIdLoader extends EventScheduleByGuidOrIdLoader {

		private TestingQueryBuilder<EventSchedule> queryBuilder;
		private boolean findByIdCalled = false;
		
		public TestableEventScheduleByGuidOrIdLoader(SecurityFilter filter) {
			super(filter);
		}

		@Override
		protected QueryBuilder<EventSchedule> getQueryBuilder(SecurityFilter filter) {
			queryBuilder = new TestingQueryBuilder<EventSchedule>(EventSchedule.class);
			queryBuilder.setSingleResult(new EventSchedule());
			return queryBuilder;
		}

		@Override
		protected EventSchedule findByIdUsingEntityManager(EntityManager em) {
			findByIdCalled = true;
			return new EventSchedule();
		}
	}
}
