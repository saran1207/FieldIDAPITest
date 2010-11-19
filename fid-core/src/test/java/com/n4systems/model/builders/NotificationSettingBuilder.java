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
	private final Boolean includeFailed;
	private final Boolean sendBlankReport;
	
	public static NotificationSettingBuilder aNotificationSetting() {
		return new NotificationSettingBuilder(PrimaryOrgBuilder.aPrimaryOrg().build(), "name", true, false, false, true, UserBuilder.aUser().build());
	}
	
	private NotificationSettingBuilder(PrimaryOrg owner, String name, Boolean includeUpcoming, Boolean includeOverdue, Boolean includeFailed, Boolean sendBlankReport, User user) {
		this.owner = owner;
		this.name = name;
		this.upcomingReport = new UpcomingEventReport(RelativeTime.TODAY, RelativeTime.NEXT_WEEK, includeUpcoming);
		this.includeOverdue = includeOverdue;
		this.includeFailed = includeFailed;
		this.user = user;
		this.sendBlankReport = sendBlankReport;
	}
	
	public NotificationSettingBuilder includeOverdue() {
		return new NotificationSettingBuilder(owner, name, upcomingReport.isIncludeUpcoming(), true, includeFailed, sendBlankReport, user);
	}

	public NotificationSettingBuilder doNotIncludeOverdue() {
		return new NotificationSettingBuilder(owner, name, upcomingReport.isIncludeUpcoming(), false, includeFailed, sendBlankReport, user);
	}
	
	public NotificationSettingBuilder includeUpcoming() {
		return new NotificationSettingBuilder(owner, name, true, includeOverdue, includeFailed, sendBlankReport, user);
	}

	public NotificationSettingBuilder doNotIncludeUpcoming() {
		return new NotificationSettingBuilder(owner, name, false, includeOverdue, includeFailed, sendBlankReport, user);
	}
	
	public NotificationSettingBuilder includeFailed() {
		return new NotificationSettingBuilder(owner, name, upcomingReport.isIncludeUpcoming(), includeOverdue, true, sendBlankReport, user);
	}

	public NotificationSettingBuilder doNotIncludeFailed() {
		return new NotificationSettingBuilder(owner, name, upcomingReport.isIncludeUpcoming(), includeOverdue, false, sendBlankReport, user);
	}

	public NotificationSettingBuilder sendBlankReport() {
		return new NotificationSettingBuilder(owner, name, upcomingReport.isIncludeUpcoming(), includeOverdue, includeFailed, true, user);
	}

	public NotificationSettingBuilder doNotSendBlankReport() {
		return new NotificationSettingBuilder(owner, name, upcomingReport.isIncludeUpcoming(), includeOverdue, includeFailed, false, user);
	}
	
	@Override
	public NotificationSetting createObject() {
		NotificationSetting notificationSettings = new NotificationSetting();
		notificationSettings.setOwner(owner);
		notificationSettings.setName(name);
		notificationSettings.setIncludeOverdue(includeOverdue);
		notificationSettings.setUser(user);
		notificationSettings.setUpcomingReport(upcomingReport);
		notificationSettings.setIncludeFailed(includeFailed);
		notificationSettings.setSendBlankReport(sendBlankReport);
		return notificationSettings;
	}

}
