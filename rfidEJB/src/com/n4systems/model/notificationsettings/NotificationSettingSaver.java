package com.n4systems.model.notificationsettings;

import rfid.ejb.entity.UserBean;

import com.n4systems.persistence.savers.ModifiedBySaver;

public class NotificationSettingSaver extends ModifiedBySaver<NotificationSetting> {
	
	public NotificationSettingSaver() {
		super();
	}
	
	public NotificationSettingSaver(Long modifiedBy) {
		super(modifiedBy);
	}

	public NotificationSettingSaver(UserBean modifiedBy) {
		super(modifiedBy);
	}

}
