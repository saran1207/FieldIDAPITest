package com.n4systems.ejb;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.interceptor.Interceptors;

import com.n4systems.ejb.interceptor.TimingInterceptor;
import com.n4systems.model.notificationsettings.NotificationSetting;
import com.n4systems.model.notificationsettings.NotificationSettingOwner;
import com.n4systems.model.notificationsettings.NotificationSettingOwnerListLoader;
import com.n4systems.model.notificationsettings.NotificationSettingOwnerSaver;
import com.n4systems.model.notificationsettings.NotificationSettingSaver;

//TODO: this does not need to be an ejb anymore
@Interceptors({TimingInterceptor.class})
@Stateless
public class NotificationSettingManagerImpl implements NotificationSettingManager {

	@EJB private PersistenceManager pm;
	
	public void saveOrUpdate(NotificationSetting setting, List<NotificationSettingOwner> owners, Long modifiedBy) {
		NotificationSettingSaver settingSaver = new NotificationSettingSaver(modifiedBy);
		NotificationSettingOwnerSaver ownerSaver = new NotificationSettingOwnerSaver();
		
		setting = settingSaver.saveOrUpdate(setting);

		for (NotificationSettingOwner owner: owners) {
			// now that we've saved the setting we need to ensure that it's set on our owner
			owner.setNotificationSetting(setting);
			ownerSaver.saveOrUpdate(owner);
		}
	}
	
	public void remove(NotificationSetting setting) {
		NotificationSettingOwnerListLoader ownerListLoader = new NotificationSettingOwnerListLoader();
		ownerListLoader.setNotificationSettingId(setting.getId());
		
		// need to load and remove the owners for it first
		for (NotificationSettingOwner owner: ownerListLoader.load()) {
			pm.getEntityManager().remove(owner);
		}
		
		pm.delete(setting);
	}

}
