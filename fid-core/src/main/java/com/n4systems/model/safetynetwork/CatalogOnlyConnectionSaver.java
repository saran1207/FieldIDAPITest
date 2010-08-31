package com.n4systems.model.safetynetwork;

import javax.persistence.EntityManager;

import com.n4systems.exceptions.InvalidArgumentException;
import com.n4systems.exceptions.NotImplementedException;
import com.n4systems.model.orgs.PrimaryOrg;
import com.n4systems.model.safetynetwork.TypedOrgConnection.ConnectionType;
import com.n4systems.persistence.savers.Saver;

public class CatalogOnlyConnectionSaver extends Saver<TypedOrgConnection> {
	private final Long houseAccountId;
	
	public CatalogOnlyConnectionSaver(Long houseAccountId) {
		super();
		this.houseAccountId = houseAccountId;
	}

	@Override
	protected void remove(EntityManager em, TypedOrgConnection entity) {
		guard(entity);
		super.remove(em, entity);
	}


	@Override
	protected void save(EntityManager em, TypedOrgConnection entity) {
		guard(entity);
		completeConnection(em, entity);
		super.save(em, entity);
	}
	
	

	private void completeConnection(EntityManager em, TypedOrgConnection entity) {
		if (entity.getConnectedOrg() == null) {
			PrimaryOrg findHouseAccount = findHouseAccount(em);
			entity.setConnectedOrg(findHouseAccount);
		}
	}

	private PrimaryOrg findHouseAccount(EntityManager em) {
		return em.find(PrimaryOrg.class, houseAccountId);
	}

	@Override
	protected TypedOrgConnection update(EntityManager em, TypedOrgConnection entity) {
		throw new NotImplementedException("you can not update this type of connection");
	}

	
	private void guard(TypedOrgConnection entity) {
		if (entity.getConnectionType() != ConnectionType.CATALOG_ONLY) {
			throw new InvalidArgumentException("you must only pass CATALOG ONLY typed connections.");
		}
		
		if (entity.getConnectedOrg() != null && entity.getConnectedOrg().getTenant().getId().equals(houseAccountId)) {
			throw new InvalidArgumentException("You can only connect to the house account with a Catalog only type");
		}
	}

}
