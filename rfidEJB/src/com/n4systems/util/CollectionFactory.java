package com.n4systems.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

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
