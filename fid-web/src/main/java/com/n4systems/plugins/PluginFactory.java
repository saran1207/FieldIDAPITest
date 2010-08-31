package com.n4systems.plugins;


import org.apache.log4j.Logger;

import com.n4systems.plugins.integration.OrderResolver;

/**
 * Factory for creating implemented plugins.
 * 
 * Note: this should be located in EJB.  It's only here right now since the actual implementation of the plexus plugin
 * resides in the web project (due to dependency problems)
 */
public class PluginFactory {
	
	private static Logger logger = Logger.getLogger(PluginFactory.class);
	
	/**
	 * Creates an OrderResolver by Class name.
	 * @param resolverClassName		Full Class name of the implementing class. 
	 * @return						The instantated resolver or null on failure.
	 */
	public static OrderResolver createResolver(String resolverClassName) {
		OrderResolver resolver = null;
		
		try {
			Class<?> resolverClass = Class.forName(resolverClassName);
			
			resolver = (OrderResolver)resolverClass.newInstance();
		} catch (Throwable t) {
			logger.error("Unable to instantate Resolver plugin class [" + resolverClassName + "]", t);
		}
		
		return resolver;
	}
}
