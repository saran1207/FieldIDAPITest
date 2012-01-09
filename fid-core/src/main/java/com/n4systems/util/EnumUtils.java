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

}
