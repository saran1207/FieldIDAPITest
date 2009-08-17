package com.n4systems.persistence;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.apache.log4j.Logger;

import com.n4systems.model.api.Saveable;
import com.n4systems.persistence.loaders.Loader;
import com.n4systems.persistence.savers.Saver;

public class PersistenceManager {
	private static final String PERSISTENCE_UNIT = "fieldid";
	private static Logger logger = Logger.getLogger(PersistenceManager.class);
	private static EntityManagerFactory entityManagerFactory;

	private static synchronized EntityManagerFactory getEntityManagerFactory() {
		if (entityManagerFactory == null) {
			logger.debug("Creating EntityManagerFactory");
			entityManagerFactory = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT);
		}
		return entityManagerFactory;
	}

	private static EntityManager getEntityManager() {
		logger.debug("Creating EntityManager");
		EntityManager createEntityManager = getEntityManagerFactory().createEntityManager();
		return createEntityManager;
	}

	public static void shutdown() {
		if (entityManagerFactory != null && entityManagerFactory.isOpen()) {
			logger.debug("Destroying EntityManagerFactory");
			entityManagerFactory.close();
			entityManagerFactory = null;
		} else {
			logger.warn("Close called on null or unopen EntityManagerFactory");
		}
	}

	public static Transaction startTransaction() {
		return new Transaction(getEntityManager());
	}
	
	public static <T> T executeLoader(Loader<T> loader) {
		T result = null;
		Transaction transaction = startTransaction();
		try {
			result = loader.load(transaction);
		} catch(RuntimeException e) {
			transaction.rollback();
			throw e;
		} finally {
			transaction.commit();
		}
		return result;
	}

	public static <T extends Saveable> void executeSaver(Saver<T> saver, T entity) {
		Transaction transaction = startTransaction();
		try {
			saver.save(transaction, entity);
		} catch(RuntimeException e) {
			transaction.rollback();
			throw e;
		} finally {
			transaction.commit();
		}
	}

	public static <T extends Saveable> T executeUpdater(Saver<T> saver, T entity) {
		T managedEntity = null;
		Transaction transaction = startTransaction();
		try {
			saver.update(transaction, entity);
		} catch(RuntimeException e) {
			transaction.rollback();
			throw e;
		} finally {
			transaction.commit();
		}
		return managedEntity;
	}

	public static <T extends Saveable> void executeDeleter(Saver<T> deleter, T entity) {
		Transaction transaction = startTransaction();
		try {
			deleter.remove(transaction, entity);
		} catch(RuntimeException e) {
			transaction.rollback();
			throw e;
		} finally {
			transaction.commit();
		}
	}

}
