package com.n4systems.taskscheduling.task;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.naming.NamingException;

import org.apache.log4j.Logger;

import com.n4systems.model.inspectionschedulecount.InspectionScheduleCount;
import com.n4systems.model.inspectionschedulecount.InspectionScheduleCountListLoader;
import com.n4systems.model.notificationsettings.NotificationSetting;
import com.n4systems.model.notificationsettings.NotificationSettingOwnerListLoader;
import com.n4systems.model.orgs.PrimaryOrg;
import com.n4systems.model.taskconfig.TaskConfig;
import com.n4systems.persistence.loaders.AllEntityListLoader;
import com.n4systems.services.TenantCache;
import com.n4systems.taskscheduling.ScheduledTask;
import com.n4systems.util.LogUtils;
import com.n4systems.util.SecurityFilter;
import com.n4systems.util.ServiceLocator;
import com.n4systems.util.mail.MailMessage;

public class InspectionScheduleNotificationTask extends ScheduledTask {
	private static Logger logger = Logger.getLogger(InspectionScheduleNotificationTask.class);
	public static final String jobNamePrefix = "InspectScheduleNotify-";	
	public static final String tenantIdPrefix = "tenantId";
	public static final String reportStartKey = "report_start";
	public static final String reportLengthKey = "report_length";
	public static final String notifyAddrsKey = "notify_addrs";
	
	private SimpleDateFormat dateFormatter;

	public InspectionScheduleNotificationTask() {
	    super(60 * 30, TimeUnit.SECONDS);
    }

	@Override
    protected void runTask(TaskConfig config) throws Exception {
		logger.info("Starting Inspection Schedule Notification Task");
		
		AllEntityListLoader<NotificationSetting> loader = new AllEntityListLoader<NotificationSetting>(NotificationSetting.class);
		
		Date now = new Date();
		Date startDate, endDate;
		for (NotificationSetting setting: loader.load()) {
			
			// need to check if we're actually supposed to notify today			
			if (!setting.getFrequency().isSameDay(now)) {
				continue;
			}
			
			PrimaryOrg primaryOrg = TenantCache.getInstance().findPrimaryOrg(setting.getTenant().getId());
			
			dateFormatter = new SimpleDateFormat(primaryOrg.getDateFormat());
			
			// resolve the relative times to hard dates, so they can be used in a query
			startDate = setting.getPeriodStart().getRelative(now);
			endDate = setting.getPeriodEnd().getRelative(startDate);
			
			generateNotificationMessage(setting, startDate, endDate);
		}
		
		logger.info("Completed Inspection Schedule Notification Task");
    }
	
	public void generateNotificationMessage(NotificationSetting setting, Date start, Date end) throws NamingException, NoSuchProviderException, MessagingException {
		logger.info(LogUtils.prepare("Generating inspection schedule report for Tenant [$0], Name [$1], User [$2], Start [$3], End [$4]", 
				setting.getTenant(), setting.getName(), setting.getUser().getUserID(), start, end));
		
		NotificationSettingOwnerListLoader customerLoader = new NotificationSettingOwnerListLoader();
		customerLoader.setNotificationSettingId(setting.getId());
		
		SecurityFilter settingUserFilter = new SecurityFilter(setting.getUser());		
		InspectionScheduleCountListLoader loader = new InspectionScheduleCountListLoader(settingUserFilter);
		
		loader.setNotification(setting);
		loader.setOwners(customerLoader.load());
		loader.setFromDate(start);
		loader.setToDate(end);
		
		List<InspectionScheduleCount> inspectionCounts = loader.load();
		
		String messageSubject = "Scheduled Inspection Report " + setting.getName() + " - " + dateFormatter.format(start) + " to " + dateFormatter.format(end);
		
		// no we need to build the message body with the html inspection report table
		StringBuilder messageBody = new StringBuilder(getTableCSS());
		messageBody.append("<center><h3>Scheduled Inspection Report: ").append(setting.getName()).append("</h3>");
		
		if(inspectionCounts.size() > 0) {
			
			messageBody
				.append("<table class=\"message\" cellpadding=2 cellspacing=2 border><tr>")
				.append("<th>Inspection Date</th>");
				
			if (setting.isUsingJobSite()) {
				messageBody.append("<th>Job Site</th>");
			} else {
				messageBody.append("<th>Customer</th><th>Division</th>");
			}
				
			messageBody.append("<th>Product Type</th><th>Event Type</th><th>Inspections Due</th></tr>");
		
			for(InspectionScheduleCount inspectionCount: inspectionCounts) {
				messageBody.append(formatMessageTableEntry(inspectionCount, setting.isUsingJobSite()));
			}
		
			messageBody.append("</table>");
		} else {
			// format a message for where there are no upcoming inspections
			messageBody.append("<h5>There are no scheduled inspections for this period</h5>");
		}
		
		messageBody.append("</center>");
		
		logger.trace("Notification Message Body: " + messageBody);
		
		MailMessage message = new MailMessage(messageSubject, messageBody.toString());
		
		// we need to make sure all our addresses are valid
		Set<String> addresses = new HashSet<String>();
		for (String address: setting.getAddresses()) {
			try {
				InternetAddress.parse(address, true);
				addresses.add(address);
			} catch(AddressException e) {
				logger.warn("Could not parse email address [" + address + "]", e);
			}
		}
		
		message.setToAddresses(addresses);
		
		logger.info(LogUtils.prepare("Sending Notification Message to [$0] recipients", setting.getAddresses().size()));
		ServiceLocator.getMailManager().sendMessage(message);
	}
	
	private String formatMessageTableEntry(InspectionScheduleCount isCount, boolean usingJobSite) {
		StringBuilder entry = new StringBuilder();
		
		entry
			.append("<tr><td>")
			.append(dateFormatter.format(isCount.getNextInspectionDate()))
			.append("</td><td>")
			.append(isCount.getOwnerName());
		
		if (!usingJobSite) {
			entry.append("</td><td>").append(isCount.getDivisionName());
		}
				
		entry
			.append("</td><td>")
			.append(isCount.getProductTypeName())
			.append("</td><td>")
			.append(isCount.getInspectionTypeName())
			.append("</td><td>")
			.append(isCount.getInspectionCount())
			.append("</td></tr>");
		
		return entry.toString();
	}
	
	private String getTableCSS() {
		String css = "<style type='text/css'>" +
		"table.message {" +
			"border-width: 1px 1px 1px 1px;" +
			"border-spacing: 1px;" +
			"border-style: none none none none;" +
			"border-color: gray gray gray gray;" +
			"border-collapse: separate;" +
			"background-color: rgb(255, 255, 240);" +
		"}" +
		"table.message th {" +
			"border-width: 1px 1px 1px 1px;" +
			"padding: 4px 4px 4px 4px;" +
			"border-style: dotted dotted dotted dotted;" +
			"border-color: blue blue blue blue;" +
			"background-color: white;" +
			"-moz-border-radius: 0px 0px 0px 0px;" +
		"}" +
		"table.message td {" +
			"border-width: 1px 1px 1px 1px;" +
			"padding: 4px 4px 4px 4px;" +
			"border-style: dotted dotted dotted dotted;" +
			"border-color: blue blue blue blue;" +
			"background-color: white;" +
			"-moz-border-radius: 0px 0px 0px 0px;" +
		"}" +
		"</style>";
		
		return css;
	}
}
