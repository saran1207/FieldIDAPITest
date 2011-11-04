package com.n4systems.handlers.creator.events.factory;

import javax.persistence.EntityManager;

import com.n4systems.ejb.PersistenceManager;
import com.n4systems.ejb.impl.CreateEventsMethodObject;
import com.n4systems.ejb.impl.EntityManagerLastEventDateFinder;
import com.n4systems.ejb.impl.EventSaver;
import com.n4systems.ejb.impl.EventScheduleManagerImpl;
import com.n4systems.ejb.impl.LastEventDateFinder;
import com.n4systems.ejb.impl.ManagerBackedCreateEventsMethodObject;
import com.n4systems.ejb.impl.ManagerBackedEventSaver;
import com.n4systems.ejb.impl.PersistenceManagerImpl;
import com.n4systems.ejb.legacy.impl.LegacyAssetManager;
import com.n4systems.handlers.creator.EventPersistenceFactory;
import com.n4systems.handlers.creator.EventsInAGroupCreator;
import com.n4systems.handlers.creator.WebServiceEventsCreator;
import com.n4systems.handlers.creator.events.EventCreator;
import com.n4systems.persistence.FieldIdTransactionManager;
import com.n4systems.persistence.Transaction;
import com.n4systems.persistence.TransactionManager;
import com.n4systems.security.AuditLogger;
import com.n4systems.security.CreateEventAuditHandler;
import com.n4systems.security.Log4JAuditLogger;
import com.n4systems.services.ManagerBackedNextEventScheduleService;
import com.n4systems.services.NextEventScheduleSerivce;


public class ProductionEventPersistenceFactory implements EventPersistenceFactory {

	public AuditLogger createCreateEventAuditLogger() {
		return new Log4JAuditLogger(new CreateEventAuditHandler());
	}

	public EventSaver createEventSaver(Transaction transaction) {
		EntityManager em = transaction.getEntityManager();
		PersistenceManager persistenceManager = new PersistenceManagerImpl(em);
		LastEventDateFinder lastEventDateFinder = new EntityManagerLastEventDateFinder(persistenceManager, em);
		
		return new ManagerBackedEventSaver(new LegacyAssetManager(em), persistenceManager, em, lastEventDateFinder);
	}

	public EventCreator createEventCreator() {
		return  new EventCreator(createTransactionManager(), this);
	}

	public FieldIdTransactionManager createTransactionManager() {
		return new FieldIdTransactionManager();
	}

	public CreateEventsMethodObject createCreateEventsMethodObject(Transaction transaction) {
		EntityManager em = transaction.getEntityManager();
		PersistenceManager persistenceManager = new PersistenceManagerImpl(em);
		
		return new ManagerBackedCreateEventsMethodObject(persistenceManager, createEventSaver(transaction));
	}
	
	public EventsInAGroupCreator createEventsInAGroupCreator() {
		return new WebServiceEventsCreator(createTransactionManager(), this);
	}
	
	public NextEventScheduleSerivce createNextEventScheduleService(Transaction transaction) {
		return new ManagerBackedNextEventScheduleService(new EventScheduleManagerImpl(transaction.getEntityManager()));
	}

    @Override
    public EventCreator createEventCreator(TransactionManager transactionManager) {
        return  new EventCreator(transactionManager, this);
    }
}
