package com.n4systems.persistence;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.apache.log4j.Logger;

import com.n4systems.persistence.deleters.Deleter;
import com.n4systems.persistence.loaders.Loader;
import com.n4systems.persistence.savers.Saver;

public class PersistenceManager {
	private static final String PERSISTENCE_UNIT = "fieldid";
	private static Logger logger = Logger.getLogger(PersistenceManager.class);
	private static EntityManagerFactory entityManagerFactory;

	private static synchronized EntityManagerFactory getEntityManagerFactory() {
		if (entityManagerFactory == null) {
			logger.info("Creating EntityManagerFactory");
			entityManagerFactory = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT);
		}
		return entityManagerFactory;
	}

	private static EntityManager getEntityManager() {
		logger.info("Creating EntityManager");
		EntityManager createEntityManager = getEntityManagerFactory().createEntityManager();
		return createEntityManager;
	}

	public static void shutdown() {
		if (entityManagerFactory != null && entityManagerFactory.isOpen()) {
			logger.info("Destroying EntityManagerFactory");
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
		} finally {
			transaction.commit();
		}
		return result;
	}

	public static void executeSaver(Saver<?> saver) {
		Transaction transaction = startTransaction();
		try {
			saver.save(transaction);
		} finally {
			transaction.commit();
		}
	}

	public static <T> T executeUpdater(Saver<T> saver) {
		T managedEntity = null;
		Transaction transaction = startTransaction();
		try {
			saver.update(transaction);
		} finally {
			transaction.commit();
		}
		return managedEntity;
	}

	public static void executeDeleter(Deleter deleter) {
		Transaction transaction = startTransaction();
		try {
			deleter.delete(transaction);
		} finally {
			transaction.commit();
		}
	}

}
