package com.n4systems.ejb.impl;

import java.util.Date;

import javax.persistence.EntityManager;

import org.apache.log4j.Logger;

import com.n4systems.ejb.PersistenceManager;
import com.n4systems.exceptions.InvalidQueryException;
import com.n4systems.model.Inspection;
import com.n4systems.model.InspectionSchedule;
import com.n4systems.model.InspectionType;
import com.n4systems.model.Product;
import com.n4systems.model.api.Archivable.EntityState;
import com.n4systems.model.security.OpenSecurityFilter;
import com.n4systems.util.persistence.QueryBuilder;

public class EntityManagerLastInspectionDateFinder implements LastInspectionDateFinder {
	private static Logger logger = Logger.getLogger(EntityManagerLastInspectionDateFinder.class);
	
	
	private final PersistenceManager persistenceManager;
	private final EntityManager em;

	public EntityManagerLastInspectionDateFinder(PersistenceManager persistenceManager, EntityManager em) {
		super();
		this.persistenceManager = persistenceManager;
		this.em = em;
	}

	public Date findLastInspectionDate(Product product) {
		return findLastInspectionDate(product, null);
	}

	public Date findLastInspectionDate(Long scheduleId) {
		return findLastInspectionDate(persistenceManager.find(InspectionSchedule.class, scheduleId));
	}

	public Date findLastInspectionDate(InspectionSchedule schedule) {
		return findLastInspectionDate(schedule.getProduct(), schedule.getInspectionType());
	}

	public Date findLastInspectionDate(Product product, InspectionType inspectionType) {

		QueryBuilder<Date> qBuilder = new QueryBuilder<Date>(Inspection.class, new OpenSecurityFilter(), "i");

		qBuilder.setMaxSelect("date");
		qBuilder.addSimpleWhere("product.id", product.getId());
		qBuilder.addSimpleWhere("state", EntityState.ACTIVE);

		if (inspectionType != null) {
			qBuilder.addSimpleWhere("type.id", inspectionType.getId());
		}

		Date lastInspectionDate = null;
		try {
			lastInspectionDate = qBuilder.getSingleResult(em);
		} catch (InvalidQueryException e) {
			logger.error("Unable to find last inspection date", e);
		} catch (Exception e) {
			logger.error("Unable to find last inspection date", e);
		}

		return lastInspectionDate;
	}

}
