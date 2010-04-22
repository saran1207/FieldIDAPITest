package com.n4systems.handlers.creator.inspections.factory;

import javax.persistence.EntityManager;

import com.n4systems.ejb.PersistenceManager;
import com.n4systems.ejb.impl.ManagerBackedCreateInspectionsMethodObject;
import com.n4systems.ejb.impl.CreateInspectionsMethodObject;
import com.n4systems.ejb.impl.EntityManagerLastInspectionDateFinder;
import com.n4systems.ejb.impl.InspectionSaver;
import com.n4systems.ejb.impl.InspectionScheduleManagerImpl;
import com.n4systems.ejb.impl.LastInspectionDateFinder;
import com.n4systems.ejb.impl.ManagerBackedInspectionSaver;
import com.n4systems.ejb.impl.PersistenceManagerImpl;
import com.n4systems.ejb.legacy.impl.LegacyProductSerialManager;
import com.n4systems.handlers.creator.InspectionPersistenceFactory;
import com.n4systems.handlers.creator.InspectionsInAGroupCreator;
import com.n4systems.handlers.creator.WebServiceInspectionsCreator;
import com.n4systems.handlers.creator.inspections.InspectionCreator;
import com.n4systems.persistence.FieldIdTransactionManager;
import com.n4systems.persistence.Transaction;
import com.n4systems.security.AuditLogger;
import com.n4systems.security.CreateInspectionAuditHandler;
import com.n4systems.security.Log4JAuditLogger;



public class ProductionInspectionPersistenceFactory implements InspectionPersistenceFactory {

	public AuditLogger createCreateInspectionAuditLogger() {
		return new Log4JAuditLogger(new CreateInspectionAuditHandler());
	}

	public InspectionSaver createInspectionSaver(Transaction transaction) {
		EntityManager em = transaction.getEntityManager();
		PersistenceManager persistenceManager = new PersistenceManagerImpl(em);
		LastInspectionDateFinder lastInspectionDateFinder = new EntityManagerLastInspectionDateFinder(persistenceManager, em);
		
		return new ManagerBackedInspectionSaver(new LegacyProductSerialManager(em), new InspectionScheduleManagerImpl(em), persistenceManager, em, lastInspectionDateFinder);
	}

	public InspectionCreator createInspectionCreator() {
		return  new InspectionCreator(createTransactionManager(), this);
	}

	public FieldIdTransactionManager createTransactionManager() {
		return new FieldIdTransactionManager();
	}

	public CreateInspectionsMethodObject createCreateInspectionsMethodObject(Transaction transaction) {
		EntityManager em = transaction.getEntityManager();
		PersistenceManager persistenceManager = new PersistenceManagerImpl(em);
		
		return new ManagerBackedCreateInspectionsMethodObject(persistenceManager, createInspectionSaver(transaction));
	}
	
	public InspectionsInAGroupCreator createInspectionsInAGroupCreator() {
		return new WebServiceInspectionsCreator(createTransactionManager(), this);
	}
	

	
}
