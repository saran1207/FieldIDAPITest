package com.n4systems.model.orgs;

import javax.persistence.EntityManager;

import com.n4systems.persistence.savers.Saver;

public class OrgSaver extends Saver<BaseOrg> {

	@Override
	protected void save(EntityManager em, BaseOrg entity) {
		super.save(em, entity);
		
		// we need to re-save the org so that the security fields get set.
		entity.touch();
		super.update(em, entity);
	}

}
