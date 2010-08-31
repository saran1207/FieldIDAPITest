package com.n4systems.util;

import java.lang.reflect.Method;

/**
 * Helper utility for logging
 */
public class LoggingHelper {

	// hide public constructor
	private LoggingHelper() {}
	
	public static String prepareMethodName(Method method) {
		final StringBuilder methodSig = new StringBuilder(method.getDeclaringClass().getName());
		methodSig.append('.');
		methodSig.append(method.getName());
		methodSig.append('(');

		Class<?>[] params = method.getParameterTypes();
		
		if(params.length > 0) {
			methodSig.append(params[0].getSimpleName());
			for(int i = 1; i < params.length; i++) {
				methodSig.append(", ");
				methodSig.append(params[i].getSimpleName());
			}
		}
		methodSig.append(')');
		
		return methodSig.toString();
	}
	
	
}
