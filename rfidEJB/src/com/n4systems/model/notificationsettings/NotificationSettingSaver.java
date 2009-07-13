package com.n4systems.model.notificationsettings;

import com.n4systems.ejb.PersistenceManager;
import com.n4systems.persistence.savers.legacy.EntitySaver;
import com.n4systems.util.SecurityFilter;

public class NotificationSettingSaver extends EntitySaver<NotificationSetting> {

	public NotificationSettingSaver() {
	    super();
    }

	public NotificationSettingSaver(PersistenceManager pm) {
	    super(pm);
    }

	@Override
    protected void save(PersistenceManager pm, NotificationSetting entity) {
		pm.save(entity, getModifiedById());	
    }

	@Override
    protected NotificationSetting update(PersistenceManager pm, NotificationSetting entity) {
	    return pm.update(entity, getModifiedById());
    }

	@Override
	protected void remove(PersistenceManager pm, NotificationSetting entity) {
		// we need to load and remove the owners first
		NotificationSettingOwnerListLoader ownerLoader = new NotificationSettingOwnerListLoader(pm, new SecurityFilter(entity.getTenant().getId()));
		ownerLoader.setNotificationSettingId(entity.getId());
		
		for (NotificationSettingOwner owner: ownerLoader.load()) {
			pm.deleteAny(owner);
		}
		
		pm.delete(entity);
	}

}
