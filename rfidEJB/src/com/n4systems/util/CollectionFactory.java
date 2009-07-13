package com.n4systems.util;

import antlr.collections.List;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

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
		return createCollection(collection.getClass(), collection.size());
	}
	
	/**
	 * 
	 * @param collectionClass
	 * @param capacity
	 * @return
	 */
	public static <E extends Collection<T>, T> Collection<T> createCollection(Class<E> collectionClass, Integer capacity) {
		Collection<T> collection;
		
		//more specifc implementations must come first (ie SortedSet before Set)
		if (List.class.isAssignableFrom(collectionClass)) {
			collection = new ArrayList<T>(capacity);
		} else if (SortedSet.class.isAssignableFrom(collectionClass)) {
			collection = new TreeSet<T>();
		} else if (Set.class.isAssignableFrom(collectionClass)) {
			collection = new HashSet<T>(capacity);
		} else if (Queue.class.isAssignableFrom(collectionClass)) {
			collection = new LinkedList<T>();	
		} else {
			// array list is default, but we should never reach this anyway
			collection = new ArrayList<T>(capacity);
		}
		
		return collection;
	}
	
	
}
