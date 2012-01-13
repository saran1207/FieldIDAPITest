package com.n4systems.util;

import com.google.common.base.Preconditions;

public class EnumUtils {
	

	public static <T extends Enum<T>> T valueOf(Class<T> clazz, String value) {
		Preconditions.checkNotNull(clazz, "must specify a non-null enumeration");
		if (value==null) { 
			return null;
		}
	    
        try {
	        return Enum.valueOf(clazz, value.trim().toUpperCase());
	    } catch(IllegalArgumentException ex) {
	        // can't find it? 
	    	return null;
	    }
	}
	
	public static <T extends Enum<T>> T nextLooped(T t) {
	    return next(t,true);
	}

	public static <T extends Enum<T>> T next(T t) {
	    return next(t,false);
	}
	
	public static <T extends Enum<T>> T previousLooped(T t) {
	    return previous(t,true);
	}

	public static <T extends Enum<T>> T previous(T t) {
	    return previous(t,false);
	}

	@SuppressWarnings("unchecked")
	private static <T extends Enum<T>> T next(T t, boolean wrap) {
	    int index = t.ordinal();
	    Enum<T>[] constants = t.getClass().getEnumConstants();
	    if (index==constants.length - 1) {
	        return wrap ? (T)constants[0] : null;
	    } else {
	        return (T)constants[index + 1];
	    }
	}

	@SuppressWarnings("unchecked")
	private static <T extends Enum<T>> T previous(T t, boolean wrap) {
	    int index = t.ordinal();
	    Enum<T>[] constants = t.getClass().getEnumConstants();
	    if (index==0) {
	        return wrap ? (T)constants[constants.length-1] : null;
	    } else {
	        return (T)constants[index - 1];
	    }
	}

}
