package com.n4systems.ejb;

import com.n4systems.model.notificationsettings.NotificationSetting;
import com.n4systems.model.notificationsettings.NotificationSettingOwner;

import java.util.List;

import javax.ejb.Local;

@Local
public interface NotificationSettingManager {

	public void saveOrUpdate(NotificationSetting setting, List<NotificationSettingOwner> owners, Long modifiedBy);
	public void remove(NotificationSetting setting);
	
}
