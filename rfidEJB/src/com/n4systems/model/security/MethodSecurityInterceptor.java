package com.n4systems.model.security;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import org.apache.log4j.Logger;

public class MethodSecurityInterceptor<T> implements MethodInterceptor {
	private static final Logger logger = Logger.getLogger("com.n4systems.securitylog");
	private static final String[] alwaysAllowed = {"hashCode", "getClass"};  // methods only need appear here if they would be caught by getterPrefixes
	private static final String[] getterPrefixes = {"get", "is", "has"};
	private static final String DEFAULT_STRING = "";
	
	private final T target;
	private final SecurityLevel userSecurityLevel;
	
	public MethodSecurityInterceptor(T target, SecurityLevel userSecurityLevel) {
		this.target = target;
		this.userSecurityLevel = userSecurityLevel;
	}
	
	public Object intercept(Object dummyTarget, Method method, Object[] args, MethodProxy proxy) throws Throwable {		
		// if it's not a secured method, or it's local, let it pass through
		if (!isSecuredMethod(method) || userSecurityLevel.isLocal()) {
			return passthrough(method, args);
		}
		
		NetworkAccessLevel accessAnnotation = method.getAnnotation(NetworkAccessLevel.class);
		
		boolean accessAllowed;
		if (userSecurityLevel.equals(SecurityLevel.LOCAL_ENDUSER)) {
			accessAllowed = handleCustomerUserSecurity(accessAnnotation);
		} else {
			accessAllowed = handleSafetyNetworkSecurity(accessAnnotation);
		}
		
		if (!accessAllowed && logger.isTraceEnabled()) {
			String className = (target != null) ? target.getClass().getSimpleName() : "null";
			logger.trace(String.format("Access Denied: %s.%s @ %s", className, method.getName(), userSecurityLevel.name()));
		}
		
		// if access is allowed, let is passthough, otherwise get the default
		return (accessAllowed) ? passthrough(method, args) : getDefaultValue(method); 	
	}
	
	private boolean handleCustomerUserSecurity(NetworkAccessLevel accessAnnotation) {
		// customers users work the opposite of the safety network, allowing access if the annotation was not present
		boolean accessAllowed = true;
		if (accessAnnotation != null && !accessAnnotation.allowCustomerUsers()) {
			accessAllowed = false;
		}
		
		return accessAllowed;
	}
	
	private boolean handleSafetyNetworkSecurity(NetworkAccessLevel accessAnnotation) {
		// default to DENIED if no annotation was present
		SecurityLevel methodSecurityLevel = (accessAnnotation != null) ? accessAnnotation.value() : SecurityLevel.DENIED;
		
		// check to see if this method allows for this users security level
		return methodSecurityLevel.allows(userSecurityLevel);
	}
	
	private Object passthrough(Method method, Object[] args) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
		return method.invoke(target, args);
	}

	private boolean isSecuredMethod(Method method) {
		String methodName = method.getName();
		for (String allowedMethod: alwaysAllowed) {
			if (methodName.equals(allowedMethod)) {
				return false;
			}
		}
		
		for (String prefix: getterPrefixes) {
			if (methodName.startsWith(prefix)) {
				return true;
			}
		}
		
		return false;
	}
	
	private Object getDefaultValue(Method method) {
		// Strings default to an empty string, everything else comes back null
		return (method.getReturnType().equals(String.class)) ? DEFAULT_STRING : null;
	}
	
}