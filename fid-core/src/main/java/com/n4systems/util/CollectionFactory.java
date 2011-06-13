package com.n4systems.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import org.apache.commons.beanutils.BeanUtils;

import antlr.collections.List;

public class CollectionFactory {

	// all methods are static, hide constructor
	private CollectionFactory() {}
	
	/**
	 * 
	 * @param <E>
	 * @param collection
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <E extends Collection<T>, T> Collection<T> createCollection(E collection) {
		return (Collection<T>) createCollection(collection.getClass(), collection.size());
	}
	
	@SuppressWarnings("unchecked")
	public static <K,V> Map<K, V> createBeanMap(Collection<V> values, String propertyName) {
		Map<K,V> map = new HashMap<K,V>();
		for (V value:values) {
			K key;
			try {
				key = (K) BeanUtils.getProperty(value, propertyName);
				map.put(key, value);
			} catch (Exception e) {
				throw new UnsupportedOperationException("tried to create map based on undefined property '" + propertyName + "' for class '" + value.getClass());
			}
		}			
		return map;
	}

	@SuppressWarnings("unchecked")
	public static <T> Set<T> createBeanPropertySet(Collection<?> values, String propertyName) {
		Set<T> set = new HashSet<T>();
		for (Object value:values) {
			T propertyValue;
			try {
				propertyValue = (T) BeanUtils.getProperty(value, propertyName);
				set.add(propertyValue);
			} catch (Exception e) {
				throw new UnsupportedOperationException("unsuccesfully tried to create map based on property '" + propertyName + "' for class '" + value.getClass());
			}
		}			
		return set;
	}

	
	
	/**
	 * 
	 * @param collectionClass
	 * @param capacity
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static Collection<?> createCollection(Class<?> collectionClass, Integer capacity) {
		Collection<?> collection;
		
		//more specifc implementations must come first (ie SortedSet before Set)
		if (List.class.isAssignableFrom(collectionClass)) {
			collection = new ArrayList(capacity);
		} else if (SortedSet.class.isAssignableFrom(collectionClass)) {
			collection = new TreeSet();
		} else if (Set.class.isAssignableFrom(collectionClass)) {
			collection = new HashSet(capacity);
		} else if (Queue.class.isAssignableFrom(collectionClass)) {
			collection = new LinkedList();	
		} else {
			// array list is default, but we should never reach this anyway
			collection = new ArrayList(capacity);
		}
		
		return collection;
	}
}
