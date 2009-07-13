package com.n4systems.util;

import java.util.Collection;

import com.n4systems.util.reflection.ReflectionException;
import com.n4systems.util.reflection.Reflector;

public class FuzzyResolver {
	private static String nonAlphaRegex = "[^a-z]";
	private static String nonAlNumRegex = "[^a-z0-9]";
	
	/**
	 * This method takes in a String and attempts to resolve it in a collection of strings
	 * using some very basic fuzzy rules (mung'ing if you will).  This is achieved by first
	 * attempting an exact match on the strings.  If this fails, then we try converting to 
	 * lower case and removing all non-alpha (or non-alpha-numeric depending on stripNumeric)
	 * 
	 * @param  target		The String to resolve against againstList
	 * @param  againstList 	The list of String to attempt resolution against 
	 * @param stripNumeric	When true removes all non-alpha characters including numbers
	 * 
	 * @return	The resolved String from againstList or null if resolution failed 
	 */
	public static String resolveString(String target, Collection<String> againstList, boolean stripNumeric) {
		String resolvedString = null;
		
		//first try an exact match
		if(againstList.contains(target)) {
			return target;
		}

		String mungStringRegex = (stripNumeric) ? nonAlphaRegex : nonAlNumRegex;
		
		//now try the fuzzy rules
		String mungTarget = mungString(target, mungStringRegex);
		for(String against: againstList) {
			if (mungString(against, mungStringRegex).equals(mungTarget)) {
				resolvedString = against;
				break;
			}
		}
		
		return resolvedString;
	}
	
	public static String mungString(String input) {
		return mungString(input, true);
	}
	
	public static String mungString(String input, boolean stripNumeric) {
		String mungStringRegex = (stripNumeric) ? nonAlphaRegex : nonAlNumRegex;
		return mungString(input, mungStringRegex);
	}
	
	public static String mungString(String input, String mungStringRegex) {
		return input.toLowerCase().replaceAll(mungStringRegex, "");
	}
	
	/**
	 * Attempts to resolve the target against a field within a collection of beans with stripNumeric set false.
	 * 
	 * @param target		The String to resolve against againstList's, againstField
	 * @param againstList 	A list of beans to attempt resolution on.
	 * @param againstField	The bean's field to attempt the match against.
	 * 
	 * @return	The resolved bean from againstList or null if resolution failed 
	 */
	public static <T> T resolve(String target, Collection<T> againstList, String againstField) throws ReflectionException {
		return resolve(target, againstList, againstField, false);
	}
	
	/**
	 * Attempts to resolve the target against a field within a collection of beans.
	 * 
	 * @param target		The String to resolve against againstList's, againstField
	 * @param againstList 	A list of beans to attempt resolution on.
	 * @param againstField	The bean's field to attempt the match against.
	 * @param stripNumeric	When true removes all non-alpha characters including numbers
	 * 
	 * @return	The resolved bean from againstList or null if resolution failed 
	 * @throws ReflectionException 
	 */
	public static <T> T resolve(String target, Collection<T> againstList, String againstField, boolean stripNumeric) throws ReflectionException {
		T resolved = null;

		String mungStringRegex = (stripNumeric) ? nonAlphaRegex : nonAlNumRegex;
		
		//now try the fuzzy rules
		String mungTarget = mungString(target, mungStringRegex);
		String fieldValue, mungValue;
		for(T against: againstList) {
			
			fieldValue = (String)Reflector.getPathValue(against, againstField);
			mungValue = mungString(fieldValue, mungStringRegex);
			
			if(fieldValue.equalsIgnoreCase(target) || mungValue.equals(mungTarget)) {
				resolved = against;
				break;
			}
		}
		
		return resolved;
	}
}
