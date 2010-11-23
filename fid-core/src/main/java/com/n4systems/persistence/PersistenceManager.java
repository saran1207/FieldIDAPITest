package com.n4systems.persistence;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.apache.log4j.Logger;
import org.hibernate.FlushMode;
import org.hibernate.LockMode;
import org.hibernate.Session;
import org.hibernate.exception.ConstraintViolationException;
import org.hibernate.stat.Statistics;

import com.n4systems.exceptions.EntityStillReferencedException;
import com.n4systems.model.api.Saveable;
import com.n4systems.persistence.deleters.Deleter;
import com.n4systems.persistence.loaders.Loader;
import com.n4systems.persistence.savers.Saver;
import com.n4systems.util.ExceptionHelper;
import com.n4systems.util.persistence.QueryBuilder;

public class PersistenceManager {
    public static final String PRODUCTION_PERSISTENCE_UNIT = "fieldid";
	public static final String TESTING_PERSISTENCE_UNIT = "fieldid-test";
    public static String persistenceUnit = PRODUCTION_PERSISTENCE_UNIT;

	private static Logger logger = Logger.getLogger(PersistenceManager.class);
	private static EntityManagerFactory entityManagerFactory;

	private static synchronized EntityManagerFactory getEntityManagerFactory() {
		if (entityManagerFactory == null) {
			logger.debug("Creating EntityManagerFactory");
			entityManagerFactory = Persistence.createEntityManagerFactory(persistenceUnit);
		}
		return entityManagerFactory;
	}
	
	protected static void overrideEntityManagerFactory(EntityManagerFactory entityManagerFactory) {
		PersistenceManager.entityManagerFactory = entityManagerFactory;
	}

	protected static EntityManager getEntityManager() {
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
	
	public static <T> T executeLoader(final Loader<T> loader) {
		return new PersistenceManagerTransactor().execute(new UnitOfWork<T>() {
			public T run(Transaction transaction) {
				return loader.load(transaction);
			}
		});
	}

	public static <T extends Saveable> void executeSaver(final Saver<T> saver, final T entity) {
		new PersistenceManagerTransactor().execute(new UnitOfWork<Void>() {
			public Void run(Transaction transaction) {
				saver.save(transaction, entity);
				return null;
			}
		});
	}

	public static <T extends Saveable> T executeUpdater(final Saver<T> saver, final T entity) {
		return new PersistenceManagerTransactor().execute(new UnitOfWork<T>() {
			public T run(Transaction transaction) {
				return saver.update(transaction, entity);
			}
		});
	}

	@SuppressWarnings("unchecked")
	public static <T extends Saveable> void executeDeleter(Deleter<T> deleter, T entity) throws EntityStillReferencedException {
		Transaction transaction = startTransaction();
		try {
			T reloadedEntity = (T) transaction.getEntityManager().find(entity.getClass(), entity.getIdentifier());
			
			deleter.remove(transaction, reloadedEntity);
			
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
	
	public static void setSessionReadOnly(EntityManager em) {
		getHibernateSession(em).setFlushMode(FlushMode.MANUAL);
	}
	
	public static Statistics getHibernateStats() {
		EntityManager em = getEntityManager();
		
		Statistics stats = getHibernateSession(em).getSessionFactory().getStatistics();
		
		em.close();
		return stats;
	}
	
}
