
package com.n4systems.fieldid.service.task;

import com.google.common.base.Preconditions;
import com.n4systems.fieldid.service.FieldIdService;
import com.n4systems.model.security.SecurityFilter;
import com.n4systems.services.SecurityContext;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.jpa.AbstractEntityManagerFactoryBean;
import org.springframework.orm.jpa.EntityManagerFactoryUtils;
import org.springframework.orm.jpa.EntityManagerHolder;
import org.springframework.scheduling.annotation.Async;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.util.CollectionUtils;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;

/**
 * usage notes : this can be used to run code asynchronously in a few ways...
 * 1: delegate to an existing Spring service by creating a task.
 * 2: delegate to an existing legacy manager by creating a "legacyTask"
 * 3: just plunking code down here and putting an @Async tag on the public method.
 *
 * Third option is for when you ALWAYS want to run the method asynchronously.
 * The first two are more flexible and you don't want to restrict the usage of the method.
 *
 */
public class AsyncService extends FieldIdService {
	private static final Logger logger = Logger.getLogger(AsyncService.class);
	
	private @Autowired AbstractEntityManagerFactoryBean entityManagerFactory;

    @Async
	public <T> T run(AsyncTask<T> task) {
		return task.call();
	}
	
	public <X> LegacyAsyncTask<X> createLegacyTask(Callable<X> callable) {
		return new LegacyAsyncTask<X>(callable);
	}
	
	public <X> AsyncTask<X> createTask(Callable<X> callable) {
		return new AsyncTask<X>(callable);
	}

    /**
	 * local class that does all the task work. the idea is that it gets the current
	 * thread's context at construction time (recall, SecurityContext is thread
	 * scoped bean) and passes this information along when the thread executes in
	 * the call() method.
	 */

	public class LegacyAsyncTask<T> extends AsyncTask<T> {

		private LegacyAsyncTask(Callable<T> callable) {
			super(callable, securityContext);
		}

		private final EntityManager createEntityManager() throws IllegalStateException {
			EntityManagerFactory emf = getEntityManagerFactory();
			Preconditions.checkArgument(emf != null, "No EntityManagerFactory specified");
			Map<String, Object> properties = new HashMap<String, Object>();
			return (!CollectionUtils.isEmpty(properties) ? emf.createEntityManager(properties) : emf.createEntityManager());
		}

		private EntityManagerFactory getEntityManagerFactory() {
			return entityManagerFactory.getObject();
		}

		@Override
		public final T call() {
			try {
				EntityManager em = createEntityManager();
				TransactionSynchronizationManager.bindResource(getEntityManagerFactory(), new EntityManagerHolder(em));
				return super.call();
			} finally {
				EntityManagerHolder emHolder = (EntityManagerHolder) TransactionSynchronizationManager.unbindResource(getEntityManagerFactory());
				EntityManagerFactoryUtils.closeEntityManager(emHolder.getEntityManager());
			}
		}
	}

	public class AsyncTask<T> implements Callable<T> {
		private final Callable<T> callable;
		private final SecurityFilter tenantSecurityFilter;
		private final SecurityFilter userSecurityFilter;

		private AsyncTask(Callable<T> callable) {
			this(callable, securityContext);
		}

		private AsyncTask(Callable<T> callable, SecurityContext securityContext) {
			this.callable = callable;
			this.tenantSecurityFilter = securityContext.getTenantSecurityFilter();
			this.userSecurityFilter = securityContext.getUserSecurityFilter();
		}

		@Override
		public T call() {
			try {
				securityContext.setTenantSecurityFilter(tenantSecurityFilter);
				securityContext.setUserSecurityFilter(userSecurityFilter);
				return callable.call();
			} catch (Exception e) {
				logger.error("Asynchronous task failed", e);
				return null;
			} finally {
				securityContext.reset();
			}
		}
	}


}
