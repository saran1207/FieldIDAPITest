package com.n4systems.util;

import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;


public class LogUtils {
	// these are static for for speed
	private static final String[] ARG_SYMBOLS = {"\\$0", "\\$1", "\\$2", "\\$3", "\\$4", "\\$5", "\\$6", "\\$7", "\\$8", "\\$9", "\\$10", "\\$11", "\\$12", "\\$13", "\\$14", "\\$15", "\\$16", "\\$17", "\\$18", "\\$19"};	
	private static final String NULL_STRING = "null";
	
	public static String prepare(String message, Object...params) {
		if (params.length > ARG_SYMBOLS.length) {
			throw new RuntimeException("Too many log parameters.  Max " + params.length + " was " + params.length);
		}
		
		String prepMessage = message;
		// we need to work backwards otherwise $1 would replace $13
		for (int i = params.length - 1; i >= 0; i--) {
			if (params[i] != null) {
				prepMessage = prepMessage.replaceAll(ARG_SYMBOLS[i], params[i].toString());
			} else {
				prepMessage = prepMessage.replaceAll(ARG_SYMBOLS[i], NULL_STRING);
			}
		}
		
		return prepMessage;
	}
	
	
	public static List<String> getClasspathEntries() { 
		ClassLoader sysClassLoader = ClassLoader.getSystemClassLoader();
		URL[] urls = ((URLClassLoader)sysClassLoader).getURLs();
		List<String> result = new ArrayList<String>();
		for(int i=0; i< urls.length; i++) {
			result.add(urls[i].getFile());
		}
		return result;
	}
}
