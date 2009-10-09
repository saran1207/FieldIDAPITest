package com.n4systems.model.security;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

public class MethodSecurityInterceptor<T> implements MethodInterceptor {
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
		// if it's not a secured method, let it pass through
		if (!isSecuredMethod(method)) {
			return passthrough(method, args);
		}
		
		// check to see if we have the access annotation, default to DENIED if not
		SecurityLevel methodSecurityLevel = SecurityLevel.DENIED;
		if (method.isAnnotationPresent(NetworkAccessLevel.class)) {
			methodSecurityLevel = method.getAnnotation(NetworkAccessLevel.class).value();
		}

		// check to see if this method allows for this users security level
		boolean accessAllowed = methodSecurityLevel.allows(userSecurityLevel);
		
		if (!accessAllowed) {
			System.out.println(String.format("Access Denined: %s @ %s", method.toGenericString(), userSecurityLevel.name()));
		}
		
		
		// if access is allowed, let is passthough, otherwise get the default
		return (accessAllowed) ? passthrough(method, args) : getDefaultValue(method); 	
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