package com.n4systems.model.tenant;

import com.n4systems.ejb.PersistenceManager;
import com.n4systems.persistence.savers.legacy.EntitySaver;
import com.n4systems.services.SetupDataLastModUpdateService;

public class SetupDataLastModDatesSaver extends EntitySaver<SetupDataLastModDates> {

	private boolean notifiyUpdateService = true;
	
	public SetupDataLastModDatesSaver() {}

	public SetupDataLastModDatesSaver(PersistenceManager pm) {
		super(pm);
	}

	@Override
	protected void save(PersistenceManager pm, SetupDataLastModDates entity) {
		pm.saveAny(entity);
		checkAndNotifyUpdateService(entity);
	}

	@Override
	protected SetupDataLastModDates update(PersistenceManager pm, SetupDataLastModDates entity) {
		SetupDataLastModDates updateEntity = pm.updateAny(entity);
		checkAndNotifyUpdateService(updateEntity);
		return updateEntity;
	}

	private void checkAndNotifyUpdateService(SetupDataLastModDates modDates) {
		if (notifiyUpdateService) {
			SetupDataLastModUpdateService.getInstance().updateModDates(modDates);
		}
	}
	
	/**
	 * Set this false if you DO NOT want to notify the {@link SetupDataLastModUpdateService} of
	 * a change after save or update.  notifiyUpdateService defaults to true.
	 */
	public void setNotifiyUpdateService(boolean notifiyUpdateService) {
		this.notifiyUpdateService = notifiyUpdateService;
	}
	
}
