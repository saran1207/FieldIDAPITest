package com.n4systems.model.builders;


import com.n4systems.model.common.RelativeTime;
import com.n4systems.model.notificationsettings.NotificationSetting;
import com.n4systems.model.notificationsettings.UpcomingEventReport;
import com.n4systems.model.orgs.PrimaryOrg;
import com.n4systems.model.user.User;

public class NotificationSettingBuilder extends BaseBuilder<NotificationSetting> {

	private final PrimaryOrg owner;
	private final String name;
	private final Boolean includeOverdue;
	private final User user;
	private final UpcomingEventReport upcomingReport;
	
	public static NotificationSettingBuilder aNotificationSetting() {
		return new NotificationSettingBuilder(PrimaryOrgBuilder.aPrimaryOrg().build(), "name", true, false, UserBuilder.aUser().build());
	}
	
	private NotificationSettingBuilder(PrimaryOrg owner, String name, Boolean includeUpcoming, Boolean includeOverdue, User user) {
		this.owner = owner;
		this.name = name;
		this.upcomingReport = new UpcomingEventReport(RelativeTime.TODAY, RelativeTime.NEXT_WEEK, includeUpcoming);
		this.includeOverdue = includeOverdue;
		this.user = user;
	}
	
	public NotificationSettingBuilder includeOverdue() {
		return new NotificationSettingBuilder(owner, name, upcomingReport.isIncludeUpcoming(), true, user);
	}

	public NotificationSettingBuilder doNotIncludeOverdue() {
		return new NotificationSettingBuilder(owner, name, upcomingReport.isIncludeUpcoming(), false, user);
	}
	
	public NotificationSettingBuilder includeUpcoming() {
		return new NotificationSettingBuilder(owner, name, true, includeOverdue, user);
	}

	public NotificationSettingBuilder doNotIncludeUpcoming() {
		return new NotificationSettingBuilder(owner, name, false, includeOverdue, user);
	}
	
	@Override
	public NotificationSetting createObject() {
		NotificationSetting notificationSettings = new NotificationSetting();
		notificationSettings.setOwner(owner);
		notificationSettings.setName(name);
		notificationSettings.setIncludeOverdue(includeOverdue);
		notificationSettings.setUser(user);
		notificationSettings.setUpcomingReport(upcomingReport);
		return notificationSettings;
	}

}
