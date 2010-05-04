package com.n4systems.model.inspectionschedule;

import static org.junit.Assert.*;

import javax.persistence.EntityManager;

import org.junit.Test;

import com.n4systems.model.InspectionSchedule;
import com.n4systems.model.security.OpenSecurityFilter;
import com.n4systems.model.security.SecurityFilter;
import com.n4systems.util.persistence.QueryBuilder;
import com.n4systems.util.persistence.TestingQueryBuilder;
import com.n4systems.util.persistence.TestingTransaction;
import com.n4systems.util.persistence.WhereParameter;
import com.n4systems.util.persistence.WhereParameter.Comparator;

public class InspectionScheduleByGuidOrIdLoaderTest {
	
	@Test
	public void should_try_to_lookup_by_mobile_guid_when_set() {
		String mobileGuid = "SOME GUID";
		
		TestableInspectionScheduleByGuidOrIdLoader sut = new TestableInspectionScheduleByGuidOrIdLoader(new OpenSecurityFilter());
		sut.setMobileGuid(mobileGuid).load(new TestingTransaction());
		
		WhereParameter<?> whereParameter = (WhereParameter<?>)sut.queryBuilder.getWhereParameter("mobileGUID");
		assertEquals(Comparator.EQ, whereParameter.getComparator());
		assertEquals(mobileGuid, whereParameter.getValue());
	}
	
	@Test
	public void should_try_by_id_if_mobile_guid_not_set() {
		TestableInspectionScheduleByGuidOrIdLoader sut = new TestableInspectionScheduleByGuidOrIdLoader(new OpenSecurityFilter());
		sut.setId(1L).load(new TestingTransaction());
		
		assertTrue(sut.findByIdCalled);
	}

	private class TestableInspectionScheduleByGuidOrIdLoader extends InspectionScheduleByGuidOrIdLoader {

		private TestingQueryBuilder<InspectionSchedule> queryBuilder;
		private boolean findByIdCalled = false;
		
		public TestableInspectionScheduleByGuidOrIdLoader(SecurityFilter filter) {
			super(filter);
		}

		@Override
		protected QueryBuilder<InspectionSchedule> getQueryBuilder(SecurityFilter filter) {	
			queryBuilder = new TestingQueryBuilder<InspectionSchedule>(InspectionSchedule.class);
			queryBuilder.setSingleResult(new InspectionSchedule());
			return queryBuilder;
		}

		@Override
		protected InspectionSchedule findByIdUsingEntityManager(EntityManager em) {
			findByIdCalled = true;
			return new InspectionSchedule();
		}
	}
}
