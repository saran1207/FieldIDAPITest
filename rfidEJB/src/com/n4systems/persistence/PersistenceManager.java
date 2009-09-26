package com.n4systems.persistence;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.apache.log4j.Logger;
import org.hibernate.LockMode;
import org.hibernate.Session;
import org.hibernate.exception.ConstraintViolationException;

import com.n4systems.exceptions.EntityStillReferencedException;
import com.n4systems.model.api.Saveable;
import com.n4systems.persistence.deleters.Deleter;
import com.n4systems.persistence.loaders.Loader;
import com.n4systems.persistence.savers.Saver;
import com.n4systems.util.ExceptionHelper;
import com.n4systems.util.persistence.QueryBuilder;

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
		return new FieldIdTransaction(getEntityManager());
	}
	
	public static void rollbackTransaction(Transaction transaction) {
		if (transaction != null) {
			transaction.rollback();
		}
	}
	
	public static void finishTransaction(Transaction transaction) {
		if (transaction != null) {
			transaction.commit();
		}
	}
	
	public static <T> T executeLoader(Loader<T> loader) {
		T result = null;
		Transaction transaction = startTransaction();
		try {
			result = loader.load(transaction);
		} catch(RuntimeException e) {
			rollbackTransaction(transaction);
			throw e;
		} finally {
			finishTransaction(transaction);
		}
		
		return result;
	}

	public static <T extends Saveable> void executeSaver(Saver<T> saver, T entity) {
		Transaction transaction = startTransaction();
		try {
			saver.save(transaction, entity);
		} catch(RuntimeException e) {
			rollbackTransaction(transaction);
			throw e;
		} finally {
			finishTransaction(transaction);
		}
	}

	public static <T extends Saveable> T executeUpdater(Saver<T> saver, T entity) {
		T managedEntity = null;
		Transaction transaction = startTransaction();
		try {
			managedEntity = saver.update(transaction, entity);
		} catch(RuntimeException e) {
			rollbackTransaction(transaction);
			throw e;
		} finally {
			finishTransaction(transaction);
		}
		return managedEntity;
	}

	public static <T extends Saveable> void executeDeleter(Deleter<T> deleter, T entity) throws EntityStillReferencedException {
		Transaction transaction = startTransaction();
		try {
			deleter.remove(transaction, entity);
			
			// flush the transaction early to force the RuntimeException
			transaction.getEntityManager().flush();
			
		} catch(RuntimeException e) {
			rollbackTransaction(transaction);
			
			if (ExceptionHelper.causeContains(e, ConstraintViolationException.class)) {
				throw new EntityStillReferencedException(entity.getClass(), entity.getIdentifier(), e);
			} else {
				throw e;
			}
		} finally {
			finishTransaction(transaction);
		}
	}

	public static <E> E find(QueryBuilder<E> builder) {
		E result = null;
		Transaction transaction = startTransaction();
		try {
			result = builder.getSingleResult(transaction.getEntityManager());
		} catch(RuntimeException e) {
			rollbackTransaction(transaction);
			throw e;
		} finally {
			finishTransaction(transaction);
		}

		return result;
	}
	
	public static <E> List<E> findAll(QueryBuilder<E> builder) {
		List<E> results = null;
		Transaction transaction = startTransaction();
		try {
			results = builder.getResultList(transaction.getEntityManager());
		} catch(RuntimeException e) {
			rollbackTransaction(transaction);
			throw e;
		} finally {
			finishTransaction(transaction);
		}
		return results;
	}
	
	public static Long findCount(QueryBuilder<?> builder) {
		Long count;
		Transaction transaction = startTransaction();
		try {
			count = builder.getCount(transaction.getEntityManager());
		} catch(RuntimeException e) {
			rollbackTransaction(transaction);
			throw e;
		} finally {
			finishTransaction(transaction);
		}
		return count;
	}
	
	public static boolean entityExists(QueryBuilder<?> builder) {
		boolean exists;
		Transaction transaction = startTransaction();
		try {
			exists = builder.entityExists(transaction.getEntityManager());
		} catch(RuntimeException e) {
			rollbackTransaction(transaction);
			throw e;
		} finally {
			finishTransaction(transaction);
		}
		return exists;
	}
	
	public static Session getHibernateSession(EntityManager em) {
		return (Session) em.getDelegate();
	}
	
	public static void reattach(EntityManager em, Object entity) {
		getHibernateSession(em).lock(entity, LockMode.NONE);
	}
	
}
