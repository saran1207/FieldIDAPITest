package com.n4systems.fieldid.service.task;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

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
	
	@Autowired private SecurityContext securityContext;
	@Autowired private AbstractEntityManagerFactoryBean entityManagerFactory;

	private EntityManagerFactory getEntityManagerFactory() {
		return entityManagerFactory.getObject();
	}
		

	public <T> AsyncTask<T> createTask(Runnable runnable) {
		return new AsyncTask<T>(runnable);
	}			
	
	public class AsyncTask<T> implements Runnable { 
		SecurityFilter userFilter;
		SecurityFilter tenantFilter;
		Runnable runnable;
		
		public AsyncTask(Runnable runnable) { 
			this(runnable, securityContext.getTenantSecurityFilter(), securityContext.getUserSecurityFilter());
		}
		
		public AsyncTask(Runnable runnable, SecurityFilter tenantFilter, SecurityFilter userFilter) {			
			this.runnable = runnable;
			this.tenantFilter = tenantFilter;
			this.userFilter = userFilter;
		}
		
		private final EntityManager createEntityManager() throws IllegalStateException {
			EntityManagerFactory emf = getEntityManagerFactory();
			Preconditions.checkArgument(emf != null, "No EntityManagerFactory specified");
			// TODO DD : do i need properties when constructing this????
			Map<String,Object> properties = new HashMap<String,Object>();
			return (!CollectionUtils.isEmpty(properties) ? emf.createEntityManager(properties) : emf.createEntityManager());
		}		

		@Override
		public void run() {
			try { 				
				securityContext.setTenantSecurityFilter(tenantFilter);
				securityContext.setUserSecurityFilter(userFilter);
				EntityManager em = createEntityManager();
				TransactionSynchronizationManager.bindResource(getEntityManagerFactory(), new EntityManagerHolder(em));				
				runnable.run();
			} finally {
				EntityManagerHolder emHolder = (EntityManagerHolder)
				TransactionSynchronizationManager.unbindResource(getEntityManagerFactory());
				EntityManagerFactoryUtils.closeEntityManager(emHolder.getEntityManager());				
				securityContext.clear();
			}			
		}
	}
}
	
	

