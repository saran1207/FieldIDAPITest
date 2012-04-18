
package com.n4systems.fieldid.service.task;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import com.n4systems.fieldid.service.sendsearch.SendSearchService;
import com.n4systems.model.SendSavedItemSchedule;
import com.n4systems.model.search.SearchCriteria;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.jpa.AbstractEntityManagerFactoryBean;
import org.springframework.orm.jpa.EntityManagerFactoryUtils;
import org.springframework.orm.jpa.EntityManagerHolder;
import org.springframework.scheduling.annotation.Async;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.util.CollectionUtils;

import com.google.common.base.Preconditions;
import com.n4systems.fieldid.service.FieldIdService;
import com.n4systems.model.security.SecurityFilter;
import com.n4systems.services.SecurityContext;

public class AsyncService extends FieldIdService {
	private static final Logger logger = Logger.getLogger(AsyncService.class);
	
	@Autowired private AbstractEntityManagerFactoryBean entityManagerFactory;
    @Autowired private SendSearchService sendSearchService;

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

    public void sendSearchAsync(final SearchCriteria searchCriteria, final SendSavedItemSchedule schedule) {
        AsyncService.AsyncTask<Object> task = createTask(new Callable<Object>() {
            @Override
            public Object call() throws Exception {
                sendSearchService.sendSearch(searchCriteria, schedule);
                return null;
            }
        });
        run(task);
    }

	/**
	 * local class that does all the work. the idea is that it gets the current
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
