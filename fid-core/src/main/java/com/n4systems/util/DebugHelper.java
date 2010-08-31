package com.n4systems.util;

import java.util.Collection;
import java.util.Map;

import org.apache.log4j.Logger;

public class DebugHelper {
	private static final char TAB = '\t';
	private static final char NEWLINE = '\n';
	private static final String OPEN_LIST = " [";
	private static final String CLOSE_LIST = "]";
	private static final String MAP_VALUE_SEP = " = ";
	private static final String NULL = "NULL";

	@SuppressWarnings("unchecked")
	public static void printMap(Map map, String message) {
		printMap(map, message, null);
	}
	
	@SuppressWarnings("unchecked")
	public static void printMap(Map map, String message, Logger logger) {
		StringBuilder output = new StringBuilder(message);
		output.append(OPEN_LIST);
		
		if (map == null) {
			output.append(NULL);
		} else {
			for (Object entry: map.entrySet()) {
				indentNewLine(output);
				output.append(decodeNull(((Map.Entry)entry).getKey()));
				output.append(MAP_VALUE_SEP);
				output.append(decodeNull(((Map.Entry)entry).getValue()));
			}
			
			// if the list was empty, the close should appear immediately after the open bracket
			if (!map.isEmpty()) {
				indentNewLine(output, 0);
			}
		}
		output.append(CLOSE_LIST);
			
		log(output.toString(), logger);
	}
	
	@SuppressWarnings("unchecked")
	public static void printCollection(Collection collection, String message) {
		printCollection(collection, message);
	}
	
	@SuppressWarnings("unchecked")
	public static void printCollection(Collection collection, String message, Logger logger) {
		StringBuilder output = new StringBuilder(message);
		output.append(OPEN_LIST);
		
		if (collection == null) {
			output.append(NULL);
		} else {
			for (Object element: collection) {
				indentNewLine(output);
				output.append(decodeNull(element));
			}
			
			// if the list was empty, the close should appear immediately after the open bracket
			if (!collection.isEmpty()) {
				indentNewLine(output, 0);
			}
		}
		output.append(CLOSE_LIST);
		
		log(output.toString(), logger);
	}
	
	private static void indentNewLine(StringBuilder str) {
		indentNewLine(str, 1);
	}
	
	private static void indentNewLine(StringBuilder str, int tabs) {
		str.append(NEWLINE);
		for (int i = 0; i < tabs; i++) {
			str.append(TAB);
		}
	}
	
	private static void log(String message, Logger logger) {
		if (logger == null) {
			System.err.println(message);
		} else {
			logger.debug(message);
		}
	}
	
	private static Object decodeNull(Object value) {
		return (value == null) ? NULL : value; 
	}
}
