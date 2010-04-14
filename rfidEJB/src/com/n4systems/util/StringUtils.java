package com.n4systems.util;

import java.util.Collection;

public class StringUtils {

	public static <T> String concat(T[] array, String delim) {
		StringBuilder sb = new StringBuilder();
		
		if(array.length > 0) {
			sb.append(array[0].toString());
			
			for(int i = 1; i < array.length; i++) {
				sb.append(delim);
				sb.append(array[i]);
			}
		}
		
		return sb.toString();
	}
	
	@SuppressWarnings("unchecked")
	public static <T> String concat(Collection<T> list, String delim) {
		return concat((T[])list.toArray(), delim);
	}
	
	/**
	 * Indents a String with tabs.
	 * @param str	String to indent.
	 * @param tabs	Number of tabs to indent by
	 * @return		The indented String
	 */
	public static String indent(String str, int tabs) {
		if (str == null) {
			return null;
		}
		
		String tabStr = "";
		for (int i = 0; i < tabs; i++) {
			tabStr += "\t";
		}
		
		return tabStr + str.replaceAll("\\n", "\n" + tabStr);
	}
	
	/**
	 * @return A trimmed str if str {@link #isNotEmpty(String)}. null otherwise.
	 */
	public static String clean(String str) {
		return (isNotEmpty(str)) ? str.trim() : null;
	}
	
	/**
	 * @return If value is null returns an empty string otherwise returns value.toString()
	 */
	public static String stringOrEmpty(Object value) {
		return (value == null) ? new String() : value.toString();
	}
	
	/**
	 * @return true iff str is non-null and has non-zero length after trimming.  false otherwise.
	 */
	public static boolean isNotEmpty(String str) {
		return (str != null && str.trim().length() > 0);
	}
	
	/**
	 * @return false iff str is non-null and has non-zero length after trimming.  true otherwise.
	 */
	public static boolean isEmpty(String str) {
		return !isNotEmpty(str);
	}
	
	/**
	 * Replaces '.'s in a string path (such as an ognl path) with '_'s for use in places
	 * where '.'s have special meanings (eg for use a parameter names in a jpql query).  
	 * @param str	A path String
	 * @return		The converted String
	 */
	public static String pathToName(String str) {
		return str.replace('.', '_'); 
	}
	
	public static String stripWhitespace(String str) {
		return str.replaceAll("\\s", "");
	}
}
