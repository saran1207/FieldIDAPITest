package com.n4systems.util.reflection;

import java.text.ParseException;

public class SimpleReflectionFilter implements ReflectionFilter<String> {

	private String path;
	private String value;
	
	/**
	 * Takes an expression in the form "field.path=String value"
	 * @param filterExpression
	 */
	public SimpleReflectionFilter(String filterExpression) throws ParseException {
		int splitIdx = filterExpression.indexOf('=');
		
		if (splitIdx == -1) {
			throw new ParseException("Filter expressions require a '='", filterExpression.length()); 
		}
		
		path = filterExpression.substring(0, splitIdx);
		value = filterExpression.substring(splitIdx + 1, filterExpression.length());
	}
	
	
	public String getPath() {
		return path;
	}

	public boolean isValid(String object) {
		return object.trim().toLowerCase().equals(value.trim().toLowerCase());
	}

}
