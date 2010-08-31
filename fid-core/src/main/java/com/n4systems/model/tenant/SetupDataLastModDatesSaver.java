package com.n4systems.model.tenant;

import javax.persistence.EntityManager;

import com.n4systems.persistence.savers.Saver;
import com.n4systems.services.SetupDataLastModUpdateService;

public class SetupDataLastModDatesSaver extends Saver<SetupDataLastModDates> {

	private boolean notifiyUpdateService = true;
	
	public SetupDataLastModDatesSaver() {}

	@Override
	protected void save(EntityManager em, SetupDataLastModDates entity) {
		em.persist(entity);
		checkAndNotifyUpdateService(entity);
	}

	@Override
	protected SetupDataLastModDates update(EntityManager em, SetupDataLastModDates entity) {
		SetupDataLastModDates updateEntity = em.merge(entity);
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
