package com.n4systems.ejb.legacy.impl;


import javax.persistence.EntityManager;
import javax.persistence.NoResultException;

import javax.persistence.Query;

import com.n4systems.ejb.legacy.UnitOfMeasureManager;
import com.n4systems.model.UnitOfMeasure;



public class UnitOfMeasureManagerImpl implements UnitOfMeasureManager {

	
	protected EntityManager em;
	
	
	public UnitOfMeasureManagerImpl() {
		super();
	}

	public UnitOfMeasureManagerImpl(EntityManager em) {
		super();
		this.em = em;
	}




	public UnitOfMeasure getUnitOfMeasureForInfoField(Long infoFieldId) {
		UnitOfMeasure unitMeasure = null;
		
		try {
			Query query = em.createQuery("select i.unitOfMeasure from InfoFieldBean i where i.uniqueID = :infoFieldId");
			query.setParameter("infoFieldId", infoFieldId);
	
			unitMeasure = (UnitOfMeasure)query.getSingleResult();
		} catch(NoResultException e) {}
		
		return unitMeasure;
	}

	
	
	
}
