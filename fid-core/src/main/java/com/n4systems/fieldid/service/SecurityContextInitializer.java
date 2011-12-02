package com.n4systems.fieldid.service;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.n4systems.model.security.OpenSecurityFilter;
import com.n4systems.model.security.TenantOnlySecurityFilter;
import com.n4systems.model.security.UserSecurityFilter;
import com.n4systems.model.user.User;
import com.n4systems.services.SecurityContext;
import com.n4systems.util.persistence.QueryBuilder;
import com.n4systems.util.persistence.WhereClauseFactory;

@Service
@Scope("singleton")
public class SecurityContextInitializer implements ApplicationContextAware {
	
	private static ApplicationContext applicationContext;
	
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		SecurityContextInitializer.applicationContext = applicationContext;
	}

	public static void initSecurityContext(String userAuthKey) {
		QueryBuilder<User> builder = new QueryBuilder<User>(User.class, new OpenSecurityFilter());
		builder.addWhere(WhereClauseFactory.create("authKey", userAuthKey));
		
		PersistenceService persistenceService = getPersistenceService();
		User user = persistenceService.find(builder);
		
		if (user == null) {
			throw new SecurityException("No user for authKey [" + userAuthKey + "]");
		}
		
		SecurityContext securityContext = getSecurityContext();
		securityContext.setTenantSecurityFilter(new TenantOnlySecurityFilter(user.getTenant().getId()));
		securityContext.setUserSecurityFilter(new UserSecurityFilter(user));
	}
	
	public static void resetSecurityContext() {
		SecurityContext securityContext = getSecurityContext();
		securityContext.reset();
	}

	private static SecurityContext getSecurityContext() {
		SecurityContext securityContext = (SecurityContext) applicationContext.getBean(SecurityContext.class);
		return securityContext;
	}
	
	private static PersistenceService getPersistenceService() {
		PersistenceService persistenceService = (PersistenceService) applicationContext.getBean(PersistenceService.class);
		return persistenceService;
	}
}
