package com.n4systems.model.security;

import com.n4systems.util.CollectionFactory;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
import org.apache.commons.collections.MapUtils;
import org.apache.log4j.Logger;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Map;

public class MethodSecurityInterceptor<T> implements MethodInterceptor, Serializable {
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
		
		boolean accessAllowed = false;
        if (userSecurityLevel == SecurityLevel.LOCAL_ENDUSER) {
			accessAllowed = method.getAnnotation(DenyReadOnlyUsersAccess.class) == null;
		} else if (userSecurityLevel == SecurityLevel.SAFETY_NETWORK) {
			accessAllowed = method.getAnnotation(AllowSafetyNetworkAccess.class) != null;
		}
		
		if (!accessAllowed && logger.isTraceEnabled()) {
			String className = (target != null) ? target.getClass().getSimpleName() : "null";
			logger.trace(String.format("Access Denied: %s.%s @ %s", className, method.getName(), userSecurityLevel.name()));
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
		Class<?> returnType = method.getReturnType();
		
		// Strings default to an empty string
		if (returnType.equals(String.class)) {
			return DEFAULT_STRING;
		}
		
		// on collections, we return an empty collection
		if (Collection.class.isAssignableFrom(returnType)) {
			return CollectionFactory.createCollection(returnType, 0);
		}
		
		// maps return empty maps
		if (Map.class.isAssignableFrom(returnType)) {
			return MapUtils.EMPTY_MAP;
		}
		
		// everything else returns null
		return null;
	}
	
}