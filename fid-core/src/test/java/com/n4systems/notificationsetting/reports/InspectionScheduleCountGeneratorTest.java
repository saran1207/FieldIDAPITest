package com.n4systems.notificationsetting.reports;

import static com.n4systems.model.builders.NotificationSettingBuilder.*;
import static org.easymock.EasyMock.*;
import static org.junit.Assert.*;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.junit.Test;

import com.n4systems.ejb.MailManagerTestDouble;
import com.n4systems.model.inspectionschedulecount.InspectionScheduleCount;
import com.n4systems.model.inspectionschedulecount.OverdueInspectionScheduleCountListLoader;
import com.n4systems.model.inspectionschedulecount.UpcomingInspectionScheduleCountListLoader;
import com.n4systems.model.notificationsettings.NotificationSetting;
import com.n4systems.test.helpers.FluentArrayList;
import com.n4systems.util.mail.TemplateMailMessage;
import com.n4systems.util.time.Clock;
import com.n4systems.util.time.StoppedClock;


public class InspectionScheduleCountGeneratorTest {

	
	@Test
	public void should_create_the_correct_subject_when_overdue_schedules_are_included() throws Exception {
		MailManagerTestDouble mailManager = new MailManagerTestDouble();
		InspectionScheduleCountGenerator sut = new InspectionScheduleCountGenerator(new SimpleDateFormat(), createSuccessfulUpcomingLoader(new ArrayList<InspectionScheduleCount>()), null, mailManager);
		
		
		NotificationSetting notificationSetting = aNotificationSetting().doNotIncludeOverdue().build();
		sut.sendReport(notificationSetting, new StoppedClock());
		assertTrue("incorrect subject got " + mailManager.message.getSubject(), mailManager.message.getSubject().startsWith("Scheduled Events Report: " + notificationSetting.getName() + " - "));
	}


	
	@Test
	public void should_not_send_null_to_mail_message_for_overdue_schedules() throws Exception {
		MailManagerTestDouble mailManager = new MailManagerTestDouble();
		InspectionScheduleCountGenerator sut = new InspectionScheduleCountGenerator(new SimpleDateFormat(), createSuccessfulUpcomingLoader(new ArrayList<InspectionScheduleCount>()), null, mailManager);
		
		NotificationSetting notificationSetting = aNotificationSetting().doNotIncludeOverdue().build();
		sut.sendReport(notificationSetting, new StoppedClock());
		
		TemplateMailMessage message = (TemplateMailMessage) mailManager.message;
		assertNull(message.getTemplateMap().get("overdueEvents"));
	}
	
	@Test
	public void should_you_load_the_overdue_schedules_when_the_notifications_set_to_include_overdue() throws Exception {
		MailManagerTestDouble mailManager = new MailManagerTestDouble();
		NotificationSetting notificationSetting = aNotificationSetting().includeOverdue().build();
		
		FluentArrayList<InspectionScheduleCount> overdueInspecitonCounts = new FluentArrayList<InspectionScheduleCount>();
		
		OverdueInspectionScheduleCountListLoader overdueLoader = createMock(OverdueInspectionScheduleCountListLoader.class);
		overdueLoader.setNotificationSetting(notificationSetting);
		expect(overdueLoader.setClock((Clock)anyObject())).andReturn(overdueLoader);
		expect(overdueLoader.load()).andReturn(overdueInspecitonCounts);
		replay(overdueLoader);
		
		
		InspectionScheduleCountGenerator sut = new InspectionScheduleCountGenerator(new SimpleDateFormat(), createSuccessfulUpcomingLoader(new ArrayList<InspectionScheduleCount>()), overdueLoader, mailManager);
		
		
		sut.sendReport(notificationSetting, new StoppedClock());
		
		verify(overdueLoader);
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void should_you_load_the_overdue_schedules_when_the_notifications_set_to_include_overdue_in_mail() throws Exception {
		MailManagerTestDouble mailManager = new MailManagerTestDouble();
		NotificationSetting notificationSetting = aNotificationSetting().includeOverdue().build();
		
		FluentArrayList<InspectionScheduleCount> overdueInspecitonCounts = new FluentArrayList<InspectionScheduleCount>(new InspectionScheduleCount(new Date(), notificationSetting.getOwner(), null, null, 1L));
		OverdueInspectionScheduleCountListLoader overdueLoader = createSuccessfulOverdueLoader(notificationSetting, overdueInspecitonCounts);
		
		
		InspectionScheduleCountGenerator sut = new InspectionScheduleCountGenerator(new SimpleDateFormat(), createSuccessfulUpcomingLoader(new ArrayList<InspectionScheduleCount>()), overdueLoader, mailManager);
		
		
		sut.sendReport(notificationSetting, new StoppedClock());
		
		TemplateMailMessage message = (TemplateMailMessage) mailManager.message;
		
		
		assertEquals(overdueInspecitonCounts, new FluentArrayList<InspectionScheduleCount>((Set)message.getTemplateMap().get("overdueEvents")));
	}
	
	
	

	@Test
	public void should_lookup_upcoming_schedules_when_upcoming_report_is_marked_as_included() throws Exception {
		MailManagerTestDouble mailManager = new MailManagerTestDouble();
		NotificationSetting notificationSetting = aNotificationSetting().includeUpcoming().build();
		
		UpcomingInspectionScheduleCountListLoader upcomingLoader = createMock(UpcomingInspectionScheduleCountListLoader.class);
		upcomingLoader.setFromDate((Date)anyObject());
		upcomingLoader.setToDate((Date)anyObject());
		upcomingLoader.setNotificationSetting((NotificationSetting)anyObject());
		expect(upcomingLoader.load()).andReturn(new ArrayList<InspectionScheduleCount>());
		replay(upcomingLoader);
		
		InspectionScheduleCountGenerator sut = new InspectionScheduleCountGenerator(new SimpleDateFormat(), upcomingLoader, 
					createSuccessfulOverdueLoader(notificationSetting, new ArrayList<InspectionScheduleCount>()), mailManager);
		
		
		Clock clock = new StoppedClock();
		sut.sendReport(notificationSetting, clock);
		
		verify(upcomingLoader);
	}
	
	

	@SuppressWarnings("unchecked")
	@Test
	public void should_send_upcoming_schedules_to_mail_when_upcoming_report_is_marked_as_included() throws Exception {
		MailManagerTestDouble mailManager = new MailManagerTestDouble();
		NotificationSetting notificationSetting = aNotificationSetting().includeUpcoming().build();
		
		List<InspectionScheduleCount> upcomingInspectionCounts = new FluentArrayList<InspectionScheduleCount>(new InspectionScheduleCount(new Date(), notificationSetting.getOwner(), null, null, 1L));
		InspectionScheduleCountGenerator sut = new InspectionScheduleCountGenerator(new SimpleDateFormat(), createSuccessfulUpcomingLoader(upcomingInspectionCounts), 
					createSuccessfulOverdueLoader(notificationSetting, upcomingInspectionCounts), mailManager);
		
		
		sut.sendReport(notificationSetting, new StoppedClock());
		
		TemplateMailMessage message = (TemplateMailMessage) mailManager.message;
		
		
		assertEquals(upcomingInspectionCounts, new ArrayList<InspectionScheduleCount>((Set)message.getTemplateMap().get("upcomingEvents")));
	}
	
	@Test
	public void should_not_send_upcoming_schedules_to_mail_when_upcoming_report_is_marked_as_included() throws Exception {
		MailManagerTestDouble mailManager = new MailManagerTestDouble();
		NotificationSetting notificationSetting = aNotificationSetting().doNotIncludeUpcoming().build();
		
		List<InspectionScheduleCount> upcomingInspectionCounts = new FluentArrayList<InspectionScheduleCount>(new InspectionScheduleCount(new Date(), notificationSetting.getOwner(), null, null, 1L));
		InspectionScheduleCountGenerator sut = new InspectionScheduleCountGenerator(new SimpleDateFormat(), createSuccessfulUpcomingLoader(upcomingInspectionCounts), 
					createSuccessfulOverdueLoader(notificationSetting, upcomingInspectionCounts), mailManager);
		
		
		sut.sendReport(notificationSetting, new StoppedClock());
		
		TemplateMailMessage message = (TemplateMailMessage) mailManager.message;
		
		assertNull(message.getTemplateMap().get("upcomingEvents"));
	}
	
	
	@Test
	public void should_not_include_the_date_range_in_the_subject_if_the_upcoming_report_is_not_included() throws Exception {
		MailManagerTestDouble mailManager = new MailManagerTestDouble();
		NotificationSetting notificationSetting = aNotificationSetting().doNotIncludeUpcoming().build();
		
		List<InspectionScheduleCount> upcomingInspectionCounts = new FluentArrayList<InspectionScheduleCount>(new InspectionScheduleCount(new Date(), notificationSetting.getOwner(), null, null, 1L));
		InspectionScheduleCountGenerator sut = new InspectionScheduleCountGenerator(new SimpleDateFormat(), createSuccessfulUpcomingLoader(upcomingInspectionCounts), 
					createSuccessfulOverdueLoader(notificationSetting, upcomingInspectionCounts), mailManager);
		
		
		sut.sendReport(notificationSetting, new StoppedClock());

		assertEquals("Scheduled Events Report: " + notificationSetting.getName(), mailManager.message.getSubject());
	}
	

	private OverdueInspectionScheduleCountListLoader createSuccessfulOverdueLoader(NotificationSetting notificationSetting, List<InspectionScheduleCount> overdueInspecitonCounts) {
		OverdueInspectionScheduleCountListLoader overdueLoader = createMock(OverdueInspectionScheduleCountListLoader.class);
		overdueLoader.setNotificationSetting(notificationSetting);
		expect(overdueLoader.setClock((Clock)anyObject())).andReturn(overdueLoader);
		expect(overdueLoader.load()).andReturn(overdueInspecitonCounts);
		replay(overdueLoader);
		return overdueLoader;
	}
	
	
	
	
	private UpcomingInspectionScheduleCountListLoader createSuccessfulUpcomingLoader(List<InspectionScheduleCount> inspectionScheduleCounts) {
		UpcomingInspectionScheduleCountListLoader upcomingLoader = createMock(UpcomingInspectionScheduleCountListLoader.class);
		upcomingLoader.setFromDate((Date)anyObject());
		upcomingLoader.setToDate((Date)anyObject());
		upcomingLoader.setNotificationSetting((NotificationSetting)anyObject());
		expect(upcomingLoader.load()).andReturn(inspectionScheduleCounts);
		replay(upcomingLoader);
		
		return upcomingLoader;
	}
}
