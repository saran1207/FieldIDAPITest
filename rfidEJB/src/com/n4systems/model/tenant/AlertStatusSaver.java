package com.n4systems.model.tenant;

import com.n4systems.ejb.PersistenceManager;
import com.n4systems.persistence.savers.legacy.EntitySaver;

public class AlertStatusSaver extends EntitySaver<AlertStatus> {

	public AlertStatusSaver() {
		super();
	}

	public AlertStatusSaver(PersistenceManager pm) {
		super(pm);
	}

	@Override
	protected void save(PersistenceManager pm, AlertStatus entity) {
		pm.saveAny(entity);
	}

	@Override
	protected AlertStatus update(PersistenceManager pm, AlertStatus entity) {
		return pm.updateAny(entity);
	}

}
