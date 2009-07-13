package com.n4systems.model.notificationsettings;

import com.n4systems.ejb.PersistenceManager;
import com.n4systems.persistence.savers.legacy.EntitySaver;

public class NotificationSettingOwnerSaver extends EntitySaver<NotificationSettingOwner> {

	public NotificationSettingOwnerSaver() {
	    super();
    }

	public NotificationSettingOwnerSaver(PersistenceManager pm) {
	    super(pm);
    }

	@Override
	protected void save(PersistenceManager pm, NotificationSettingOwner entity) {
		pm.getEntityManager().persist(entity);
	}

	@Override
	protected NotificationSettingOwner update(PersistenceManager pm, NotificationSettingOwner entity) {
		return pm.getEntityManager().merge(entity);
	}

}
