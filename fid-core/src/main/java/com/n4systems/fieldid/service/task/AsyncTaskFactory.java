package com.n4systems.fieldid.service.task;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.orm.jpa.AbstractEntityManagerFactoryBean;
import org.springframework.orm.jpa.EntityManagerFactoryUtils;
import org.springframework.orm.jpa.EntityManagerHolder;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.util.CollectionUtils;

import com.google.common.base.Preconditions;
import com.n4systems.model.security.SecurityFilter;
import com.n4systems.services.SecurityContext;

@Scope("thread")
public class AsyncTaskFactory { 
	private static final Logger logger = Logger.getLogger(AsyncTaskFactory.class);
	
	@Autowired private SecurityContext securityContext;
	@Autowired private AbstractEntityManagerFactoryBean entityManagerFactory;

	public <X> AsyncTask<X> createTask(Callable<X> callable) {
		return new AsyncTask<X>(callable);
	}			

	
	/** local class that does all the work.
	 * the idea is that it gets the current thread's context at construction time (recall, SecurityContext is thread scoped bean)
	 * and passes this information along when the thread executes in the call() method.  
	 */

	public class AsyncTask<T> implements Callable<T> { 
		Callable<T> callable;
		private SecurityFilter tenantSecurityFilter;
		private SecurityFilter userSecurityFilter;
		
		private AsyncTask(Callable<T> callable) { 
			this(callable, securityContext);
		}
		
		private AsyncTask(Callable<T> callable, SecurityContext securityContext) {			
			this.callable = callable;
			// TODO DD : implement "clone" method for security context.
			this.tenantSecurityFilter = securityContext.getTenantSecurityFilter();
			this.userSecurityFilter = securityContext.getUserSecurityFilter();
		}
		
		private final EntityManager createEntityManager() throws IllegalStateException {
			EntityManagerFactory emf = getEntityManagerFactory();
			Preconditions.checkArgument(emf != null, "No EntityManagerFactory specified");
			// TODO DD : do i need properties when constructing this????
			Map<String,Object> properties = new HashMap<String,Object>();
			return (!CollectionUtils.isEmpty(properties) ? emf.createEntityManager(properties) : emf.createEntityManager());
		}		

		private EntityManagerFactory getEntityManagerFactory() {
			return entityManagerFactory.getObject();
		}

		
		// TODO DD : methinks i'd be better off doing this as an around advice aspect that
		//  1: stores & resets context when thread is executed.
		//  2: creates a new task to be run immediately when any method in the AsyncService is called.
		
		
		@Override
		public final T call() {
			try { 				
				securityContext.setTenantSecurityFilter(tenantSecurityFilter);
				securityContext.setUserSecurityFilter(userSecurityFilter);
				EntityManager em = createEntityManager();
				TransactionSynchronizationManager.bindResource(getEntityManagerFactory(), new EntityManagerHolder(em));				
				return callable.call();
			} catch (Exception e) {
				// TODO DD : what to do here.  need some sort of async exception handling???
				logger.error("asynchronous task failed " + e.getLocalizedMessage());
				return null;
			} finally {
				EntityManagerHolder emHolder = (EntityManagerHolder)
				TransactionSynchronizationManager.unbindResource(getEntityManagerFactory());
				EntityManagerFactoryUtils.closeEntityManager(emHolder.getEntityManager());				
				securityContext.reset();
			}			
		}
	}
}
	
	

