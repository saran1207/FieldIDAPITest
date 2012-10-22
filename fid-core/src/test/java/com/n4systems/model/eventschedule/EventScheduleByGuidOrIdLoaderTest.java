package com.n4systems.model.eventschedule;

import com.n4systems.model.Event;
import com.n4systems.model.security.OpenSecurityFilter;
import com.n4systems.model.security.SecurityFilter;
import com.n4systems.util.persistence.QueryBuilder;
import com.n4systems.util.persistence.TestingQueryBuilder;
import com.n4systems.util.persistence.TestingTransaction;
import com.n4systems.util.persistence.WhereParameter;
import com.n4systems.util.persistence.WhereParameter.Comparator;
import org.junit.Test;

import javax.persistence.EntityManager;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

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
    public void should_be_null_if_mobile_guid_not_set() {
        TestableEventScheduleByGuidOrIdLoader sut = new TestableEventScheduleByGuidOrIdLoader(new OpenSecurityFilter());
        Event result = sut.setId(1L).setMobileGuid(null).load(new TestingTransaction());
        assertNull(result);
    }

    @Test
    public void should_be_null_if_mobile_guid_not_found() {
        TestableEventScheduleByGuidOrIdLoader sut = new TestableEventScheduleByGuidOrIdLoader(new OpenSecurityFilter()) {
            @Override protected Event loadByGuid(EntityManager em, SecurityFilter filter) {
                return null;
            }
        };
        Event result = sut.setId(1L).setMobileGuid("willReturnNull").load(new TestingTransaction());
        assertNull(result);
    }

    private class TestableEventScheduleByGuidOrIdLoader extends EventScheduleByGuidOrIdLoader {

		private TestingQueryBuilder<Event> queryBuilder;

		public TestableEventScheduleByGuidOrIdLoader(SecurityFilter filter) {
			super(filter);
		}

		@Override
		protected QueryBuilder<Event> getQueryBuilder(SecurityFilter filter) {
			queryBuilder = new TestingQueryBuilder<Event>(Event.class);
			queryBuilder.setSingleResult(new Event());
			return queryBuilder;
		}

    }
}
