package com.n4systems.security;

import com.n4systems.model.user.User;
import com.n4systems.util.ServiceLocator;


import java.io.Serializable;

/**
 * A singleton security wrapper class.  Setup by web at the beginning of a request, and 
 * used by managers for security checks and auditing.
 */
public final class SecurityContext implements Serializable {
	private static final long serialVersionUID = 1L;
	private static final ThreadLocal<SecurityContext> context = new ThreadLocal<SecurityContext>();
	
	private final User user;
	private final String sessionId;
	
	/**
	 * Initializes the current SecurityContext.  MUST be called prior to {@link #getContext()}
	 * @param userId				Id of the User for this session
	 * @param tenantId				Id of the Tenant for this User
	 * @param sessionId				The current JSESSIONID
	 * @throws SecurityException	If a UserBean could not be found for the given userId
	 */
	public static void initialize(Long userId, Long tenantId, String sessionId) throws SecurityException {
		User user = ServiceLocator.getPersistenceManager().find(User.class, userId, tenantId);
		
		if (user == null) {
			clear();
			throw new SecurityException("Initialize SecurityContext called with invalid User [" + userId + "] and Tenant [" + tenantId + "]");
		} else if(!user.isActive() || user.isDeleted()) {
			clear();
			throw new SecurityException("Initialize SecurityContext called for inactive or deleted User [" + userId + "] and Tenant [" + tenantId + "]");
		}
			
		context.set(new SecurityContext(user, sessionId));
	}
	
	/**
	 * Returns the current SecurityContext, checking to see that it has been properly initialized first.
	 * @return						The current SecurityContext
	 * @throws SecurityException	If called before {@link #initialize(Long, String)}
	 */
	public static SecurityContext getContext() throws SecurityException {
		SecurityContext currentContext = context.get();
		
		/* 
		 * XXX - Unfortunately, since mobile does not send up the user with each request,
		 * we cannot throw an exception here.  This makes this class quite limiting as a 
		 * security context because we cannot ensure initialization.
		 *
		if (currentContext == null) {
			throw new SecurityException("Get SecurityContext called on uninitialized context");
		}
		*/
		
		return currentContext;
	}
	
	/**
	 * Nullifies the current SecurityContext.  Should be called before the end of each Request/Response cycle to minimize 
	 * the possibility of session hijacking.
	 */
	public static void clear() {
		context.set(null);
	}
	
	/** public constructor is hidden */
	private SecurityContext(User user, String sessionId) {
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
