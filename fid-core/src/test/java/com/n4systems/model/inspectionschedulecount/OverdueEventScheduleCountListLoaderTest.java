package com.n4systems.model.inspectionschedulecount;

import static com.n4systems.model.builders.NotificationSettingBuilder.*;
import static org.junit.Assert.*;

import com.n4systems.model.EventSchedule;
import org.junit.Before;
import org.junit.Test;

import com.n4systems.exceptions.InvalidArgumentException;
import com.n4systems.model.EventSchedule.ScheduleStatus;
import com.n4systems.model.notificationsettings.NotificationSetting;
import com.n4systems.model.security.OpenSecurityFilter;
import com.n4systems.model.security.SecurityFilter;
import com.n4systems.util.persistence.QueryBuilder;
import com.n4systems.util.persistence.TestingQueryBuilder;
import com.n4systems.util.persistence.TestingTransaction;
import com.n4systems.util.persistence.WhereParameter;
import com.n4systems.util.persistence.WhereParameter.Comparator;
import com.n4systems.util.time.Clock;
import com.n4systems.util.time.StoppedClock;


public class OverdueEventScheduleCountListLoaderTest {

	private NotificationSetting notificationSettings;


	@Before
	public void createNotificationSettings() throws Exception {
		notificationSettings = aNotificationSetting().build();
	}

	@Test
	public void should_apply_non_completed_schedule_filter_to_query_builder() throws Exception {
		OverdueEventScheduleCountListLoaderExtention sut = new OverdueEventScheduleCountListLoaderExtention(new OpenSecurityFilter());
		sut.setClock(new StoppedClock()).setNotificationSetting(notificationSettings);
		
		sut.load(new TestingTransaction());
		
		WhereParameter<?> whereParameter = (WhereParameter<?>)sut.queryBuilder.getWhereParameter("status");
		assertEquals(Comparator.NE, whereParameter.getComparator());
		assertEquals(ScheduleStatus.COMPLETED, whereParameter.getValue());
	}
	
	@Test
	public void should_apply_a_filter_to_find_all_schedules_before_today() throws Exception {
		Clock clock = new StoppedClock();
		
		OverdueEventScheduleCountListLoaderExtention sut = new OverdueEventScheduleCountListLoaderExtention(new OpenSecurityFilter());
		sut.setClock(clock).setNotificationSetting(notificationSettings);

		sut.load(new TestingTransaction());
		
		WhereParameter<?> whereParameter = (WhereParameter<?>)sut.queryBuilder.getWhereParameter("toDate");
		assertEquals(Comparator.LT, whereParameter.getComparator());
		assertEquals(clock.currentTime(), whereParameter.getValue());
	}
	
	
	@Test(expected=InvalidArgumentException.class)
	public void should_not_allow_use_if_a_clock_is_not_handed_in() throws Exception {
		OverdueEventScheduleCountListLoaderExtention sut = new OverdueEventScheduleCountListLoaderExtention(new OpenSecurityFilter());
		sut.setNotificationSetting(notificationSettings);
		
		sut.load(new TestingTransaction());
	}
	
	
	private class OverdueEventScheduleCountListLoaderExtention extends OverdueEventScheduleCountListLoader {

		private TestingQueryBuilder<EventScheduleCount> queryBuilder;

		public OverdueEventScheduleCountListLoaderExtention(SecurityFilter filter) {
			super(filter);
		}



		@Override
		protected QueryBuilder<EventScheduleCount> getQueryBuilder(SecurityFilter filter) {
			queryBuilder = new TestingQueryBuilder<EventScheduleCount>(EventSchedule.class);
			return queryBuilder;
		}
		
	}
}
