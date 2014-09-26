package com.n4systems.taskscheduling.task;

import com.n4systems.model.ExtendedFeature;
import com.n4systems.model.event.FailedEventListLoader;
import com.n4systems.model.event.SmartFailedEventListLoader;
import com.n4systems.model.eventschedulecount.OverdueEventScheduleCountListLoader;
import com.n4systems.model.eventschedulecount.UpcomingEventScheduleCountListLoader;
import com.n4systems.model.notificationsettings.NotificationSetting;
import com.n4systems.model.notificationsettings.NotificationSettingsByTimezoneListLoader;
import com.n4systems.model.orgs.PrimaryOrg;
import com.n4systems.model.security.OpenSecurityFilter;
import com.n4systems.model.security.SecurityFilter;
import com.n4systems.model.security.UserSecurityFilter;
import com.n4systems.model.utils.PlainDate;
import com.n4systems.notificationsetting.reports.EventScheduleCountGenerator;
import com.n4systems.services.TenantFinder;
import com.n4systems.taskscheduling.ScheduledTask;
import com.n4systems.util.*;
import com.n4systems.util.time.StoppedClock;
import com.n4systems.util.timezone.Country;
import com.n4systems.util.timezone.CountryList;
import com.n4systems.util.timezone.Region;
import org.apache.log4j.Logger;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

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

        Map<String, Date> midnightRegions = findRegionIdsAndDatesWhereItJustTurnedConfiguredHour();

        for (String regionId : midnightRegions.keySet()) {
            logger.info("Performing notifications for region: " + regionId);
            sendNotificationsForTimeZone(regionId, midnightRegions.get(regionId));
        }

		logger.info("Completed Event Schedule Notification Task");
    }

    private void sendNotificationsForTimeZone(String regionId, Date dateInTheTimeZone) {
        NotificationSettingsByTimezoneListLoader loader = new NotificationSettingsByTimezoneListLoader(new OpenSecurityFilter());
        loader.setTimeZone(regionId);

        StoppedClock clock = new StoppedClock(dateInTheTimeZone);

		for (NotificationSetting setting: loader.load()) {
			try {
				PrimaryOrg primaryOrg = TenantFinder.getInstance().findPrimaryOrg(setting.getTenant().getId());
                if(primaryOrg.getExtendedFeatures().contains(ExtendedFeature.EmailAlerts)) {
                    SecurityFilter settingUserFilter = new UserSecurityFilter(setting.getUser());

                    logger.info(LogUtils.prepare("Generating event schedule report for Tenant [$0], Name [$1], User [$2]", setting.getTenant(), setting.getName(), setting.getUser().getUserID()));

                    new EventScheduleCountGenerator(new SimpleDateFormat(primaryOrg.getDateFormat()), new UpcomingEventScheduleCountListLoader(settingUserFilter),
                            new OverdueEventScheduleCountListLoader(settingUserFilter), createFailedEventListLoader(setting, settingUserFilter),
                            ServiceLocator.getMailManager()).sendReport(setting, clock);
                }

			} catch(Exception e) {
				logger.error("Failed sending notification: " + setting.getId(), e);
			}
		}
    }

    private FailedEventListLoader createFailedEventListLoader(NotificationSetting setting, SecurityFilter filter) {
    	return setting.isSmartFailure() ? new SmartFailedEventListLoader(filter) : new FailedEventListLoader(filter);
	}

	private static Date getTheDateInTimeZone(String timeZoneId) {
        Date localizedDate = DateHelper.localizeDate(new Date(), TimeZone.getTimeZone(timeZoneId));
        return new PlainDate(localizedDate);
    }

    private static Map<String, Date> findRegionIdsAndDatesWhereItJustTurnedConfiguredHour() {
        Map<String, Date> currentTimezones = new HashMap<String, Date>();

        SortedSet<Country> countries = CountryList.getInstance().getCountries();
        Set<String> regionIds = new HashSet<String>();

        for (Country country : countries) {
            for (Region region : country.getRegions()) {
                regionIds.add(country.getFullName(region));
            }
        }

        for (String regionId : regionIds) {
            String timeZoneId = CountryList.getInstance().getRegionByFullId(regionId).getTimeZoneId();
            TimeZone timeZone = TimeZone.getTimeZone(timeZoneId);

            Calendar cal = new GregorianCalendar();
            cal.setTimeZone(timeZone);

            int configuredHour = ConfigContext.getCurrentContext().getInteger(ConfigEntry.HOUR_TO_RUN_EVENT_SCHED_NOTIFICATIONS);
            if (cal.get(Calendar.HOUR_OF_DAY) == configuredHour) {
                currentTimezones.put(regionId, getTheDateInTimeZone(timeZoneId));
            }
        }
        
        return currentTimezones;
    }

}
