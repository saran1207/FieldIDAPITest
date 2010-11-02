package com.n4systems.handlers.creator.inspections.factory;

import javax.persistence.EntityManager;

import com.n4systems.ejb.PersistenceManager;
import com.n4systems.ejb.impl.CreateInspectionsMethodObject;
import com.n4systems.ejb.impl.EntityManagerLastInspectionDateFinder;
import com.n4systems.ejb.impl.EventSaver;
import com.n4systems.ejb.impl.InspectionScheduleManagerImpl;
import com.n4systems.ejb.impl.LastEventDateFinder;
import com.n4systems.ejb.impl.ManagerBackedCreateInspectionsMethodObject;
import com.n4systems.ejb.impl.ManagerBackedEventSaver;
import com.n4systems.ejb.impl.PersistenceManagerImpl;
import com.n4systems.ejb.legacy.impl.LegacyAssetManager;
import com.n4systems.handlers.creator.InspectionPersistenceFactory;
import com.n4systems.handlers.creator.InspectionsInAGroupCreator;
import com.n4systems.handlers.creator.WebServiceInspectionsCreator;
import com.n4systems.handlers.creator.inspections.InspectionCreator;
import com.n4systems.persistence.FieldIdTransactionManager;
import com.n4systems.persistence.Transaction;
import com.n4systems.security.AuditLogger;
import com.n4systems.security.CreateInspectionAuditHandler;
import com.n4systems.security.Log4JAuditLogger;
import com.n4systems.services.ManagerBackedNextInspectionScheduleService;
import com.n4systems.services.NextInspectionScheduleSerivce;



public class ProductionInspectionPersistenceFactory implements InspectionPersistenceFactory {

	public AuditLogger createCreateInspectionAuditLogger() {
		return new Log4JAuditLogger(new CreateInspectionAuditHandler());
	}

	public EventSaver createInspectionSaver(Transaction transaction) {
		EntityManager em = transaction.getEntityManager();
		PersistenceManager persistenceManager = new PersistenceManagerImpl(em);
		LastEventDateFinder lastEventDateFinder = new EntityManagerLastInspectionDateFinder(persistenceManager, em);
		
		return new ManagerBackedEventSaver(new LegacyAssetManager(em), persistenceManager, em, lastEventDateFinder);
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
	
	public NextInspectionScheduleSerivce createNextInspectionScheduleService(Transaction transaction) {
		return new ManagerBackedNextInspectionScheduleService(new InspectionScheduleManagerImpl(transaction.getEntityManager()));
	}
		

	
}
