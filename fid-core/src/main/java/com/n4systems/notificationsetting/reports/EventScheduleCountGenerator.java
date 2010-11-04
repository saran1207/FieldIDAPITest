package com.n4systems.notificationsetting.reports;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.naming.NamingException;

import com.n4systems.model.eventschedulecount.OverdueEventScheduleCountListLoader;
import com.n4systems.model.eventschedulecount.UpcomingEventScheduleCountListLoader;
import org.apache.log4j.Logger;

import com.n4systems.mail.MailManager;
import com.n4systems.model.eventschedulecount.EventScheduleCount;
import com.n4systems.model.notificationsettings.NotificationSetting;
import com.n4systems.model.utils.PlainDate;
import com.n4systems.util.LogUtils;
import com.n4systems.util.Range;
import com.n4systems.util.mail.TemplateMailMessage;
import com.n4systems.util.time.Clock;

public class EventScheduleCountGenerator {
	private static Logger logger = Logger.getLogger(EventScheduleCountGenerator.class);
	private SimpleDateFormat dateFormatter;
	private UpcomingEventScheduleCountListLoader upcomingLoader;
	private OverdueEventScheduleCountListLoader overdueLoader;
	private final MailManager mailManager;

	
	public EventScheduleCountGenerator(SimpleDateFormat dateFormatter, UpcomingEventScheduleCountListLoader upcomingLoader, OverdueEventScheduleCountListLoader overdueLoader, MailManager mailManager) {
		this.dateFormatter = dateFormatter;
		this.upcomingLoader = upcomingLoader;
		this.overdueLoader = overdueLoader;
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

		sendMessage(setting, startDate, endDate, upcomingEvents, overdueEvents);
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

	private void sendMessage(NotificationSetting setting, Date start, Date end, SortedSet<EventScheduleCount> upcomingEventCounts, SortedSet<EventScheduleCount> overdueEvents) throws NoSuchProviderException, MessagingException {
		String messageSubject = createSubject(setting, start, end);
		
		// no we need to build the message body with the html event report table
		TemplateMailMessage message = new TemplateMailMessage(messageSubject, "eventScheduleReport");
		
		message.getTemplateMap().put("dateFormatter", dateFormatter);
		message.getTemplateMap().put("setting", setting);
		message.getTemplateMap().put("upcomingEvents", upcomingEventCounts);
		message.getTemplateMap().put("overdueEvents", overdueEvents);
		message.getTemplateMap().put("overdueDate", new PlainDate());
		
		message.setToAddresses(getAddressList(setting));
		
		logger.info(LogUtils.prepare("Sending Notification Message to [$0] recipients", setting.getAddresses().size()));
		
		
		mailManager.sendMessage(message);
	}


	private String createSubject(NotificationSetting setting, Date start, Date end) {
		String messageSubject = "Scheduled Events Report: " + setting.getName();
		
		if (setting.getUpcomingReport().isIncludeUpcoming()) {
			messageSubject += " - " + dateFormatter.format(start) + " to " + dateFormatter.format(end);
		}
		return messageSubject;
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
