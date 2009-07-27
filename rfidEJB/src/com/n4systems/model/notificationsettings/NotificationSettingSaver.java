package com.n4systems.model.notificationsettings;

import javax.persistence.EntityManager;

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

	@Override
	protected void remove(EntityManager em, NotificationSetting entity) {
		// we need to load and remove the owners first
		NotificationSettingOwnerListLoader ownerLoader = new NotificationSettingOwnerListLoader();
		ownerLoader.setNotificationSettingId(entity.getId());
		
		for (NotificationSettingOwner owner: ownerLoader.load()) {
			em.remove(owner);
		}
		
		em.remove(entity);
	}

}
