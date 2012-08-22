package com.n4systems.util;

import java.util.Collection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtils {
    
    private static final String copyFilePrefix = "Copy of ";
	private static final Pattern numericPattern = Pattern.compile("[0-9]+");
	
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
	
	public static String lowerCaseFirstLetter(String str) {
        int strLen;
        if (str == null || (strLen = str.length()) == 0) {
            return str;
        }
        return new StringBuffer(strLen)
            .append(Character.toLowerCase(str.charAt(0)))
            .append(str.substring(1))
            .toString();
	}

	public static boolean isWholeNumber(String value) {
		if (value==null) { 
			return false;
		}
		Matcher matcher = numericPattern.matcher(value.trim());
		return matcher.matches();
	}

    /**
     * translates file name to something appropriate for a new file (doesn't accomodate file extensions)
     *              Foo --> Copy of Foo
     *      Copy of Foo --> Copy of Foo (1)
     *  Copy of Foo (5) --> Copy of Foo (6)
     *          etc...
     */
    public static String getFileCopyName(String name) {
        Pattern copyFileSuffixPattern = Pattern.compile(".*( \\(([0-9]*)\\))");
        String suffixFormat = " (%d)";
        if (name.startsWith(copyFilePrefix)) {
            Matcher matcher = copyFileSuffixPattern.matcher(name);
            StringBuffer result = new StringBuffer(name.length());
            if (matcher.matches()) {
                // increment the n in  "Copy of Foo (n)" name.
                int i = Integer.parseInt(matcher.group(2)) + 1;
                return name.replace(matcher.group(1), String.format(suffixFormat,i));
            }
            // append first set of (n) to end.
            return name + String.format(suffixFormat,1);
        }
        // simply append prefix
        return copyFilePrefix + name;
    }

    public static <T> int compareAsString(T o1, T o2) {
        return o1==null || o1.toString()==null ? 1 :
                o2==null || o2.toString()==null ? -1 :
                        o1.toString().compareTo(o2.toString());
    }



}
