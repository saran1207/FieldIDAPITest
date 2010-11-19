package com.n4systems.notificationsetting.reports;

import static com.n4systems.model.builders.NotificationSettingBuilder.*;
import static org.easymock.EasyMock.*;
import static org.junit.Assert.*;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import com.n4systems.model.Event;
import com.n4systems.model.event.FailedEventListLoader;
import com.n4systems.model.eventschedulecount.EventScheduleCount;
import com.n4systems.model.eventschedulecount.OverdueEventScheduleCountListLoader;
import com.n4systems.model.eventschedulecount.UpcomingEventScheduleCountListLoader;
import org.junit.Test;

import com.n4systems.ejb.MailManagerTestDouble;
import com.n4systems.model.notificationsettings.NotificationSetting;
import com.n4systems.test.helpers.FluentArrayList;
import com.n4systems.util.mail.TemplateMailMessage;
import com.n4systems.util.time.Clock;
import com.n4systems.util.time.StoppedClock;

public class EventScheduleCountGeneratorTest {

	@Test
	public void should_create_the_correct_subject_when_overdue_schedules_are_included() throws Exception {
		MailManagerTestDouble mailManager = new MailManagerTestDouble();
		EventScheduleCountGenerator sut = new EventScheduleCountGenerator(new SimpleDateFormat(), createSuccessfulUpcomingLoader(new ArrayList<EventScheduleCount>()), null, null, mailManager);
		
		NotificationSetting notificationSetting = aNotificationSetting().doNotIncludeOverdue().build();
		sut.sendReport(notificationSetting, new StoppedClock());
		assertTrue("incorrect subject got " + mailManager.message.getSubject(), mailManager.message.getSubject().startsWith("Events Report: " + notificationSetting.getName()));
	}

	@Test
	public void should_not_send_null_to_mail_message_for_overdue_schedules() throws Exception {
		MailManagerTestDouble mailManager = new MailManagerTestDouble();
		EventScheduleCountGenerator sut = new EventScheduleCountGenerator(new SimpleDateFormat(), createSuccessfulUpcomingLoader(new ArrayList<EventScheduleCount>()), null, null, mailManager);
		
		NotificationSetting notificationSetting = aNotificationSetting().doNotIncludeOverdue().build();
		sut.sendReport(notificationSetting, new StoppedClock());
		
		TemplateMailMessage message = (TemplateMailMessage) mailManager.message;
		assertNull(message.getTemplateMap().get("overdueEvents"));
	}
	
	
	
	@Test
	public void should_you_load_the_overdue_schedules_when_the_notifications_set_to_include_overdue() throws Exception {
		MailManagerTestDouble mailManager = new MailManagerTestDouble();
		NotificationSetting notificationSetting = aNotificationSetting().includeOverdue().build();
		
		FluentArrayList<EventScheduleCount> overdueInspecitonCounts = new FluentArrayList<EventScheduleCount>();
		
		OverdueEventScheduleCountListLoader overdueLoader = createMock(OverdueEventScheduleCountListLoader.class);
		overdueLoader.setNotificationSetting(notificationSetting);
		expect(overdueLoader.setClock((Clock)anyObject())).andReturn(overdueLoader);
		expect(overdueLoader.load()).andReturn(overdueInspecitonCounts);
		replay(overdueLoader);
		
		EventScheduleCountGenerator sut = new EventScheduleCountGenerator(new SimpleDateFormat(), createSuccessfulUpcomingLoader(new ArrayList<EventScheduleCount>()), overdueLoader, null, mailManager);
		
		sut.sendReport(notificationSetting, new StoppedClock());
		
		verify(overdueLoader);
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void should_you_load_the_overdue_schedules_when_the_notifications_set_to_include_overdue_in_mail() throws Exception {
		MailManagerTestDouble mailManager = new MailManagerTestDouble();
		NotificationSetting notificationSetting = aNotificationSetting().includeOverdue().build();
		
		FluentArrayList<EventScheduleCount> overdueInspecitonCounts = new FluentArrayList<EventScheduleCount>(new EventScheduleCount(new Date(), notificationSetting.getOwner(), null, null, 1L));
		OverdueEventScheduleCountListLoader overdueLoader = createSuccessfulOverdueLoader(notificationSetting, overdueInspecitonCounts);
		
		EventScheduleCountGenerator sut = new EventScheduleCountGenerator(new SimpleDateFormat(), createSuccessfulUpcomingLoader(new ArrayList<EventScheduleCount>()), overdueLoader, null, mailManager);
		
		sut.sendReport(notificationSetting, new StoppedClock());
		
		TemplateMailMessage message = (TemplateMailMessage) mailManager.message;
		
		assertEquals(overdueInspecitonCounts, new FluentArrayList<EventScheduleCount>((Set)message.getTemplateMap().get("overdueEvents")));
	}
	
	@Test
	public void should_lookup_upcoming_schedules_when_upcoming_report_is_marked_as_included() throws Exception {
		MailManagerTestDouble mailManager = new MailManagerTestDouble();
		NotificationSetting notificationSetting = aNotificationSetting().includeUpcoming().build();
		
		UpcomingEventScheduleCountListLoader upcomingLoader = createMock(UpcomingEventScheduleCountListLoader.class);
		upcomingLoader.setFromDate((Date)anyObject());
		upcomingLoader.setToDate((Date)anyObject());
		upcomingLoader.setNotificationSetting((NotificationSetting)anyObject());
		expect(upcomingLoader.load()).andReturn(new ArrayList<EventScheduleCount>());
		replay(upcomingLoader);
		
		EventScheduleCountGenerator sut = new EventScheduleCountGenerator(new SimpleDateFormat(), upcomingLoader,
					createSuccessfulOverdueLoader(notificationSetting, new ArrayList<EventScheduleCount>()), null, mailManager);
		
		Clock clock = new StoppedClock();
		sut.sendReport(notificationSetting, clock);
		
		verify(upcomingLoader);
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void should_send_upcoming_schedules_to_mail_when_upcoming_report_is_marked_as_included() throws Exception {
		MailManagerTestDouble mailManager = new MailManagerTestDouble();
		NotificationSetting notificationSetting = aNotificationSetting().includeUpcoming().build();
		
		List<EventScheduleCount> upcomingEventCounts = new FluentArrayList<EventScheduleCount>(new EventScheduleCount(new Date(), notificationSetting.getOwner(), null, null, 1L));
		EventScheduleCountGenerator sut = new EventScheduleCountGenerator(new SimpleDateFormat(), createSuccessfulUpcomingLoader(upcomingEventCounts),
					createSuccessfulOverdueLoader(notificationSetting, upcomingEventCounts), null, mailManager);
		
		sut.sendReport(notificationSetting, new StoppedClock());
		
		TemplateMailMessage message = (TemplateMailMessage) mailManager.message;
		
		assertEquals(upcomingEventCounts, new ArrayList<EventScheduleCount>((Set)message.getTemplateMap().get("upcomingEvents")));
	}
	
	@Test
	public void should_not_send_upcoming_schedules_to_mail_when_upcoming_report_is_marked_as_included() throws Exception {
		MailManagerTestDouble mailManager = new MailManagerTestDouble();
		NotificationSetting notificationSetting = aNotificationSetting().doNotIncludeUpcoming().build();
		
		List<EventScheduleCount> upcomingEventCounts = new FluentArrayList<EventScheduleCount>(new EventScheduleCount(new Date(), notificationSetting.getOwner(), null, null, 1L));
		EventScheduleCountGenerator sut = new EventScheduleCountGenerator(new SimpleDateFormat(), createSuccessfulUpcomingLoader(upcomingEventCounts),
					createSuccessfulOverdueLoader(notificationSetting, upcomingEventCounts), null, mailManager);
		
		sut.sendReport(notificationSetting, new StoppedClock());
		
		TemplateMailMessage message = (TemplateMailMessage) mailManager.message;
		
		assertNull(message.getTemplateMap().get("upcomingEvents"));
	}
	
	@Test
	public void should_not_include_the_date_range_in_the_subject_if_the_upcoming_report_is_not_included() throws Exception {
		MailManagerTestDouble mailManager = new MailManagerTestDouble();
		NotificationSetting notificationSetting = aNotificationSetting().doNotIncludeUpcoming().build();
		
		List<EventScheduleCount> upcomingEventCounts = new FluentArrayList<EventScheduleCount>(new EventScheduleCount(new Date(), notificationSetting.getOwner(), null, null, 1L));
		EventScheduleCountGenerator sut = new EventScheduleCountGenerator(new SimpleDateFormat(), createSuccessfulUpcomingLoader(upcomingEventCounts),
					createSuccessfulOverdueLoader(notificationSetting, upcomingEventCounts), null, mailManager);
		
		sut.sendReport(notificationSetting, new StoppedClock());

		assertEquals("Events Report: " + notificationSetting.getName(), mailManager.message.getSubject());
	}
	
	@Test
	public void should_include_failed_events() throws Exception {
		MailManagerTestDouble mailManager = new MailManagerTestDouble();
		NotificationSetting notificationSetting = aNotificationSetting().includeFailed().build();
		
		List<Event> failedEvents = new FluentArrayList<Event>(new Event());
		List<EventScheduleCount> upcomingEventCounts = new FluentArrayList<EventScheduleCount>(new EventScheduleCount(new Date(), notificationSetting.getOwner(), null, null, 1L));
		
		EventScheduleCountGenerator sut = new EventScheduleCountGenerator(new SimpleDateFormat(), createSuccessfulUpcomingLoader(upcomingEventCounts),
				null, createSuccessfulFailedEventsLoader(notificationSetting, failedEvents), mailManager);
		sut.sendReport(notificationSetting, new StoppedClock());

		TemplateMailMessage message = (TemplateMailMessage) mailManager.message;
		
		assertNotNull(message.getTemplateMap().get("failedEvents"));
	}
	
	@Test
	public void should_not_include_failed_events() throws Exception {
		MailManagerTestDouble mailManager = new MailManagerTestDouble();
		NotificationSetting notificationSetting = aNotificationSetting().doNotIncludeFailed().build();
		
		List<Event> failedEvents = new FluentArrayList<Event>(new Event());
		List<EventScheduleCount> upcomingEventCounts = new FluentArrayList<EventScheduleCount>(new EventScheduleCount(new Date(), notificationSetting.getOwner(), null, null, 1L));
		
		EventScheduleCountGenerator sut = new EventScheduleCountGenerator(new SimpleDateFormat(), createSuccessfulUpcomingLoader(upcomingEventCounts),
				null, createSuccessfulFailedEventsLoader(notificationSetting, failedEvents), mailManager);
		sut.sendReport(notificationSetting, new StoppedClock());

		TemplateMailMessage message = (TemplateMailMessage) mailManager.message;
		
		assertNull(message.getTemplateMap().get("failedEvents"));
	}
	
	@Test
	public void should_send_blank_report() throws Exception {
		MailManagerTestDouble mailManager = new MailManagerTestDouble();
		NotificationSetting notificationSetting = aNotificationSetting().includeFailed().sendBlankReport().build();
		
		List<Event> failedEvents = new FluentArrayList<Event>();
		List<EventScheduleCount> upcomingEventCounts = new FluentArrayList<EventScheduleCount>();
		
		EventScheduleCountGenerator sut = new EventScheduleCountGenerator(new SimpleDateFormat(), createSuccessfulUpcomingLoader(upcomingEventCounts),
				null, createSuccessfulFailedEventsLoader(notificationSetting, failedEvents), mailManager);
		sut.sendReport(notificationSetting, new StoppedClock());

		TemplateMailMessage message = (TemplateMailMessage) mailManager.message;
		
		assertNotNull(message);
	}
	
	@Test
	public void should_not_send_blank_report() throws Exception {
		MailManagerTestDouble mailManager = new MailManagerTestDouble();
		NotificationSetting notificationSetting = aNotificationSetting().includeFailed().doNotSendBlankReport().build();
		
		List<Event> failedEvents = new FluentArrayList<Event>();
		List<EventScheduleCount> upcomingEventCounts = new FluentArrayList<EventScheduleCount>();
		
		EventScheduleCountGenerator sut = new EventScheduleCountGenerator(new SimpleDateFormat(), createSuccessfulUpcomingLoader(upcomingEventCounts),
				null, createSuccessfulFailedEventsLoader(notificationSetting, failedEvents), mailManager);
		sut.sendReport(notificationSetting, new StoppedClock());

		TemplateMailMessage message = (TemplateMailMessage) mailManager.message;
		
		assertNull(message);
	}
	
	private FailedEventListLoader createSuccessfulFailedEventsLoader(NotificationSetting notificationSetting, List<Event> failedEvents) {
		FailedEventListLoader loader = createMock(FailedEventListLoader.class);
		expect(loader.setFrequency(notificationSetting.getFrequency())).andReturn(loader);
		expect(loader.setClock((Clock)anyObject())).andReturn(loader);
		expect(loader.load()).andReturn(failedEvents);
		replay(loader);
		return loader;
	}

	private OverdueEventScheduleCountListLoader createSuccessfulOverdueLoader(NotificationSetting notificationSetting, List<EventScheduleCount> overdueInspecitonCounts) {
		OverdueEventScheduleCountListLoader overdueLoader = createMock(OverdueEventScheduleCountListLoader.class);
		overdueLoader.setNotificationSetting(notificationSetting);
		expect(overdueLoader.setClock((Clock)anyObject())).andReturn(overdueLoader);
		expect(overdueLoader.load()).andReturn(overdueInspecitonCounts);
		replay(overdueLoader);
		return overdueLoader;
	}
	
	private UpcomingEventScheduleCountListLoader createSuccessfulUpcomingLoader(List<EventScheduleCount> eventScheduleCounts) {
		UpcomingEventScheduleCountListLoader upcomingLoader = createMock(UpcomingEventScheduleCountListLoader.class);
		upcomingLoader.setFromDate((Date)anyObject());
		upcomingLoader.setToDate((Date)anyObject());
		upcomingLoader.setNotificationSetting((NotificationSetting)anyObject());
		expect(upcomingLoader.load()).andReturn(eventScheduleCounts);
		replay(upcomingLoader);
		return upcomingLoader;
	}
}
