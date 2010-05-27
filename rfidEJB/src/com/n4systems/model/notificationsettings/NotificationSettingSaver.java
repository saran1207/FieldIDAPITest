package com.n4systems.model.notificationsettings;


import com.n4systems.model.user.User;
import com.n4systems.persistence.savers.ModifiedBySaver;

public class NotificationSettingSaver extends ModifiedBySaver<NotificationSetting> {
	
	public NotificationSettingSaver() {
		super();
	}
	
	public NotificationSettingSaver(Long modifiedBy) {
		super(modifiedBy);
	}

	public NotificationSettingSaver(User modifiedBy) {
		super(modifiedBy);
	}

}
