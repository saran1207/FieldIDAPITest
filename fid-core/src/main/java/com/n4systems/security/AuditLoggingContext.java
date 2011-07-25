package com.n4systems.security;

import java.io.Serializable;

import com.n4systems.model.user.User;
import com.n4systems.util.ServiceLocator;


public final class AuditLoggingContext implements Serializable {
	private static final long serialVersionUID = 1L;
	private static final ThreadLocal<AuditLoggingContext> context = new ThreadLocal<AuditLoggingContext>();
	
	private final User user;
	private final String sessionId;

	public static void initialize(Long userId, Long tenantId, String sessionId) throws SecurityException {
		User user = ServiceLocator.getPersistenceManager().find(User.class, userId, tenantId);
		
		if (user == null) {
			clear();
			throw new SecurityException("Initialize SecurityContext called with invalid User [" + userId + "] and Tenant [" + tenantId + "]");
		} else if(!user.isRegistered() || user.isArchived()) {
			clear();
			throw new SecurityException("Initialize SecurityContext called for inactive or deleted User [" + userId + "] and Tenant [" + tenantId + "]");
		}
			
		context.set(new AuditLoggingContext(user, sessionId));
	}

	public static AuditLoggingContext getContext() throws SecurityException {
		AuditLoggingContext currentContext = context.get();
		return currentContext;
	}

	public static void clear() {
		context.set(null);
	}

	private AuditLoggingContext(User user, String sessionId) {
		this.user = user;
		this.sessionId = sessionId;
	}

	public User getUser() {
    	return user;
    }

	public String getSessionId() {
    	return sessionId;
    }
}
