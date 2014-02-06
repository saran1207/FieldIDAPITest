package com.n4systems.notificationsetting.reports;

import com.n4systems.mail.MailManager;
import com.n4systems.model.Event;
import com.n4systems.model.ThingEvent;
import com.n4systems.model.common.SimpleFrequency;
import com.n4systems.model.event.FailedEventListLoader;
import com.n4systems.model.eventschedulecount.EventScheduleCount;
import com.n4systems.model.eventschedulecount.OverdueEventScheduleCountListLoader;
import com.n4systems.model.eventschedulecount.UpcomingEventScheduleCountListLoader;
import com.n4systems.model.notificationsettings.NotificationSetting;
import com.n4systems.model.utils.PlainDate;
import com.n4systems.util.DateHelper;
import com.n4systems.util.LogUtils;
import com.n4systems.util.Range;
import com.n4systems.util.mail.TemplateMailMessage;
import com.n4systems.util.time.Clock;
import org.apache.commons.lang.time.DateUtils;
import org.apache.log4j.Logger;

import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.naming.NamingException;
import java.text.SimpleDateFormat;
import java.util.*;

public class EventScheduleCountGenerator {
	private static Logger logger = Logger.getLogger(EventScheduleCountGenerator.class);
	private SimpleDateFormat dateFormatter;
	private UpcomingEventScheduleCountListLoader upcomingLoader;
	private OverdueEventScheduleCountListLoader overdueLoader;
	private FailedEventListLoader failedLoader;
	private final MailManager mailManager;

	
	public EventScheduleCountGenerator(SimpleDateFormat dateFormatter, UpcomingEventScheduleCountListLoader upcomingLoader, 
			OverdueEventScheduleCountListLoader overdueLoader, FailedEventListLoader failedLoader, MailManager mailManager) {
		this.dateFormatter = dateFormatter;
		this.upcomingLoader = upcomingLoader;
		this.overdueLoader = overdueLoader;
		this.failedLoader = failedLoader;
		this.mailManager = mailManager;
	}

	public void sendReport(NotificationSetting setting, Clock clock) throws NamingException, NoSuchProviderException, MessagingException {
		if (shouldGenerateReport(setting, clock)) {
			return;
		}
		generateNotificationMessage(setting, clock);
	}

	private void generateNotificationMessage(NotificationSetting setting, Clock clock) throws NamingException, NoSuchProviderException, MessagingException {
		Range<Date> dateRange = setting.getUpcomingReport().getDateRange(clock);
		
		Date startDate = dateRange.getBeginning();
		Date endDate = dateRange.getEnding();
		
		SortedSet<EventScheduleCount> upcomingEvents = null;
		if (setting.getUpcomingReport().isIncludeUpcoming()) {
			upcomingEvents = getUpcomingEvents(setting, startDate, endDate);
		}
		
		SortedSet<EventScheduleCount> overdueEvents = null;
		if (setting.isIncludeOverdue()) {
			overdueEvents = getOverdueEvents(setting, clock);
		}
		
		List<ThingEvent> failedEvents = null;
		if (setting.isIncludeFailed()) {
			failedEvents = getFailedEvents(setting, clock);
		}

		if(shouldSendMessage(setting, upcomingEvents, overdueEvents, failedEvents)) {
			sendMessage(setting, startDate, endDate, upcomingEvents, overdueEvents, failedEvents);
		}
	}

	private boolean shouldSendMessage(NotificationSetting setting, SortedSet<EventScheduleCount> upcomingEvents, 
			SortedSet<EventScheduleCount> overdueEvents, List<ThingEvent> failedEvents) {
		if (setting.getSendBlankReport()) {
			return true;
		}
		
		return (upcomingEvents != null && !upcomingEvents.isEmpty()) || (overdueEvents != null && !overdueEvents.isEmpty()) || (failedEvents != null && !failedEvents.isEmpty());
	}

	private List<ThingEvent> getFailedEvents(NotificationSetting setting, Clock clock) {
		FailedEventListLoader loader = setupFailedLoader(setting, clock);
		
		return loader.load();
	}

	private FailedEventListLoader setupFailedLoader(NotificationSetting setting, Clock clock) {
		return failedLoader.setFrequency(setting.getFrequency()).setClock(clock).setNotificationSetting(setting);
	}

	private boolean shouldGenerateReport(NotificationSetting setting, Clock clock) {
		return !setting.getFrequency().isSameDay(clock.currentTime());
	}

	private SortedSet<EventScheduleCount> getOverdueEvents(NotificationSetting setting, Clock clock) {
		OverdueEventScheduleCountListLoader loader = setupOverdueLoader(setting, clock);

		return new TreeSet<EventScheduleCount>(loader.load());
	}

	private OverdueEventScheduleCountListLoader setupOverdueLoader(NotificationSetting setting, Clock clock) {
		overdueLoader.setNotificationSetting(setting);
		overdueLoader.setClock(clock);
		return overdueLoader;
	}

	private SortedSet<EventScheduleCount> getUpcomingEvents(NotificationSetting setting, Date start, Date end) {
		UpcomingEventScheduleCountListLoader loader = setupUpcomingLoader(setting, start, end);
		
		return new TreeSet<EventScheduleCount>(loader.load());
	}

	private UpcomingEventScheduleCountListLoader setupUpcomingLoader(NotificationSetting setting, Date start, Date end) {
		upcomingLoader.setNotificationSetting(setting);
		upcomingLoader.setFromDate(start);
		upcomingLoader.setToDate(end);
		return upcomingLoader;
	}

	private void sendMessage(NotificationSetting setting, Date start, Date end, SortedSet<EventScheduleCount> upcomingEventCounts, SortedSet<EventScheduleCount> overdueEvents, List<ThingEvent> failedEvents) throws MessagingException {
		String messageSubject = "Events Report: " + setting.getName();
		
		// no we need to build the message body with the html event report table
		TemplateMailMessage message = new TemplateMailMessage(messageSubject, "eventScheduleReport");
		
		message.getTemplateMap().put("dateFormatter", dateFormatter);
		message.getTemplateMap().put("setting", setting);
		message.getTemplateMap().put("upcomingEvents", upcomingEventCounts);
		message.getTemplateMap().put("overdueEvents", overdueEvents);
		message.getTemplateMap().put("overdueDate", new PlainDate());
		message.getTemplateMap().put("failedEvents", failedEvents);

        Date reportStartDate = getReportStartDate(setting.getFrequency(), new PlainDate());
		message.getTemplateMap().put("failedReportStart", reportStartDate != null ? reportStartDate : new PlainDate() );
		message.getTemplateMap().put("failedReportEnd", new PlainDate());
		message.getTemplateMap().put("upcomingReportStart", start);
		message.getTemplateMap().put("upcomingReportEnd", DateUtils.addDays(end, -1));
		message.setToAddresses(getAddressList(setting));
		
		logger.info(LogUtils.prepare("Sending Notification Message to [$0] recipients", setting.getAddresses().size()));
		
		
		mailManager.sendMessage(message);
	}


	private Date getReportStartDate(SimpleFrequency frequency, Date date) {
		Date startDate = null;
		if (frequency.equals(SimpleFrequency.DAILY)) {
			startDate = date;
		} else if (frequency.getGroupLabel().equals(SimpleFrequency.WEEKLY_SUNDAY.getGroupLabel())){
			startDate = DateHelper.increment(date, DateHelper.WEEK, -1);
		} else if (frequency.getGroupLabel().equals(SimpleFrequency.MONTHLY_LAST.getGroupLabel())){
			startDate = DateHelper.increment(date, DateHelper.MONTH, -1);
		} 
		return startDate;
	}

	private Set<String> getAddressList(NotificationSetting setting) {
		Set<String> addresses = new HashSet<String>();
		for (String address: setting.getAddresses()) {
			try {
				InternetAddress.parse(address, true);
				addresses.add(address);
			} catch(AddressException e) {
				logger.warn("Could not parse email address [" + address + "]", e);
			}
		}
		return addresses;
	}
}
