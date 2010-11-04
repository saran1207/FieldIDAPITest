package com.n4systems.taskscheduling.task;

import java.text.SimpleDateFormat;
import java.util.concurrent.TimeUnit;

import com.n4systems.model.eventschedulecount.OverdueEventScheduleCountListLoader;
import org.apache.log4j.Logger;

import com.n4systems.model.eventschedulecount.UpcomingEventScheduleCountListLoader;
import com.n4systems.model.notificationsettings.NotificationSetting;
import com.n4systems.model.orgs.PrimaryOrg;
import com.n4systems.model.security.SecurityFilter;
import com.n4systems.model.security.UserSecurityFilter;
import com.n4systems.notificationsetting.reports.EventScheduleCountGenerator;
import com.n4systems.persistence.loaders.AllEntityListLoader;
import com.n4systems.services.TenantCache;
import com.n4systems.taskscheduling.ScheduledTask;
import com.n4systems.util.LogUtils;
import com.n4systems.util.ServiceLocator;
import com.n4systems.util.time.Clock;
import com.n4systems.util.time.StoppedClock;

public class EventScheduleNotificationTask extends ScheduledTask {
	private static Logger logger = Logger.getLogger(EventScheduleNotificationTask.class);
	public static final String jobNamePrefix = "InspectScheduleNotify-";	
	public static final String tenantIdPrefix = "tenantId";
	public static final String reportStartKey = "report_start";
	public static final String reportLengthKey = "report_length";
	public static final String notifyAddrsKey = "notify_addrs";
	
	

	public EventScheduleNotificationTask() {
	    super(60 * 30, TimeUnit.SECONDS);
    }

	@Override
    protected void runTask() throws Exception {
		logger.info("Starting Event Schedule Notification Task");
		
		AllEntityListLoader<NotificationSetting> loader = new AllEntityListLoader<NotificationSetting>(NotificationSetting.class);
		
		Clock clock = new StoppedClock();
		
		for (NotificationSetting setting: loader.load()) {
			try {
				PrimaryOrg primaryOrg = TenantCache.getInstance().findPrimaryOrg(setting.getTenant().getId());
				SecurityFilter settingUserFilter = new UserSecurityFilter(setting.getUser());
				
				logger.info(LogUtils.prepare("Generating event schedule report for Tenant [$0], Name [$1], User [$2]",	setting.getTenant(), setting.getName(), setting.getUser().getUserID()));
				
				new EventScheduleCountGenerator(new SimpleDateFormat(primaryOrg.getDateFormat()), new UpcomingEventScheduleCountListLoader(settingUserFilter), new OverdueEventScheduleCountListLoader(settingUserFilter), ServiceLocator.getMailManager()).sendReport(setting, clock);
				
			} catch(Exception e) {
				logger.error("Failed sending notification: " + setting.getId(), e);
			}
		}
		
		logger.info("Completed Event Schedule Notification Task");
    }
	
	

	
	
	
}
