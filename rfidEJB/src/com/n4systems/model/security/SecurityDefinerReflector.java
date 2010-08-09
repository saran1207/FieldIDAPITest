package com.n4systems.model.security;

import java.lang.reflect.Method;

import org.apache.log4j.Logger;

import com.n4systems.model.api.UnsecuredEntity;

public class SecurityDefinerReflector {
	private final Logger logger = Logger.getLogger(SecurityDefinerReflector.class);
	
	private final Class<?> clazz;
	
	public SecurityDefinerReflector(Class<?> clazz) {
		this.clazz = clazz;
	}
	
	/**
	 * Invokes the public static createSecurityDefiner() method on clazz.
	 * @param clazz		An entity class to get a SecurityDefiner from
	 * @return			The SecurityDefiner as returned by createSecurityDefiner()
	 * @throws SecurityException	On any exception thrown while reflecting or invoking createSecurityDefiner() or if SecurityDefiner was null.
	 */
	public SecurityDefiner getDefiner() {
		SecurityDefiner definer = null;
		
		// UnsecuredEntities do not have a security definer
		if (UnsecuredEntity.class.isAssignableFrom(clazz)) {
			return null;
		}
		
		try {
			Method staticSecurityDefinerGetter = clazz.getMethod("createSecurityDefiner");
			
			// this is a static method with no arguments.  The invocation target is therefore null.
			definer = (SecurityDefiner)staticSecurityDefinerGetter.invoke(null);

		} catch (NoSuchMethodException e) {
			logger.warn(clazz.getName() + " should define the static public method createSecurityDefiner() or implement UnsecuredEntity", e);
		} catch (Exception e) {
			logger.warn("Could not invoke createSecurityDefiner() on " + clazz.getName(), e);
		}
		
		if (definer == null) {
			logger.warn("Returning null security definer from class [" + clazz.getName() + "]");
		}
		
		return definer;
	}
}
