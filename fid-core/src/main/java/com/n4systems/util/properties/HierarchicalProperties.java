package com.n4systems.util.properties;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.TreeSet;

public class HierarchicalProperties extends Properties {
	private static final long serialVersionUID = 1L;
	private static final char DELIM = '.';
	private final String parent;

	public HierarchicalProperties(String parent, Properties defaults) {
		super(defaults);
		this.parent = parent;
	}
	
	public HierarchicalProperties(String parent) {
		this(parent, null);
	}
	
	public HierarchicalProperties(Properties defaults) {
		this(null, defaults);
	}

	public HierarchicalProperties() {
		this(null, null);
	}
	
	public HierarchicalProperties getProperties(String path) {
		HierarchicalProperties subProperties = new HierarchicalProperties(path);

		String keyString;
		for(Object key: getMasterKeySet()) {
			keyString = key.toString();
			if(keyString.startsWith(path)) {
				// remove leading path + '.'
				try {
				String propertyPath = keyString.substring(path.length() + 1);
				subProperties.put(propertyPath, getProperty(keyString));
				}catch (Exception e) {
					throw new RuntimeException(e);
				}
			}
		}
		
		return subProperties;
	}

	public List<HierarchicalProperties> getPropertiesList(String path) {
		List<HierarchicalProperties> subProperties = new ArrayList<HierarchicalProperties>();
		
		HierarchicalProperties baseProperties = getProperties(path);
		
		// we'll store our indexes in a treeset with a custom comparator so that strings are sorted as strings and numbers sorted as numbers
		Set<String> indexes = new TreeSet<String>(new Comparator<String>() {
			public int compare(String str1, String str2) {
				try {
					// if both can be cast to longs, do this as a long comparison
					Long long1 = new Long(str1);
					Long long2 = new Long(str2);
					
					return long1.compareTo(long2);
				} catch(NumberFormatException e) {
					// otherwise we'll use the strings
					return str1.compareTo(str2);
				}
			}
		});
		
		//pull our list of indexes
		String keyString;
		for(Object key: baseProperties.getMasterKeySet()) {
			keyString = key.toString();
			try {
			String tempIdx = (keyString.indexOf(DELIM) != -1) ? keyString.substring(0, keyString.indexOf(DELIM)) : keyString;
			
			indexes.add(tempIdx);
			} catch (Exception e) {
					throw new RuntimeException(e);
			}
			
		}
		
		// get new properties for each index
		try {
		for(String index: indexes) {
			subProperties.add(baseProperties.getProperties(index));
		}
		
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return subProperties;
	}
	
	public Set<Object> getMasterKeySet() {
		Set<Object> keys = new HashSet<Object>(keySet());
		if (defaults != null) {
			keys.addAll(defaults.keySet());
		}
		return keys;
	}
	
	public String getParent() {
		return parent;
	}
	
	public String getString(String key) {
		return getProperty(key);
	}
	
	public Boolean getBoolean(String key) {
		return Boolean.valueOf(getProperty(key));
	}
	
	public Integer getInteger(String key) {
		return Integer.valueOf(getProperty(key));
	}
	
	public Long getLong(String key) {
		return Long.valueOf(getProperty(key));
	}
	
	public Float getFloat(String key) {
		return Float.valueOf(getProperty(key));
	}
	
	public Double getDouble(String key) {
		return Double.valueOf(getProperty(key));
	}
}
