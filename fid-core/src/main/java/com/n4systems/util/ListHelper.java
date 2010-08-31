package com.n4systems.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import com.n4systems.model.api.Listable;

public class ListHelper {

	public static <T extends Listable<Long>, C extends Collection<T>> Map<Long, String> longListableToMap(C list) {
		Map<Long, String> listMap = new LinkedHashMap<Long, String>();
		
		for(Listable<Long> listable: list) {
			listMap.put(listable.getId(), listable.getDisplayName());
		}
		
		return listMap;
	}
	
	public static <T extends Listable<String>> Map<String, String> stringListableToMap(T ... listables) {
		return stringListableToMap(Arrays.asList(listables));
	}
	
	public static <T extends Listable<String>, C extends Collection<T>> Map<String, String> stringListableToMap(C list) {
		Map<String, String> listMap = new LinkedHashMap<String, String>();
		
		for(Listable<String> listable: list) {
			listMap.put(listable.getId(), listable.getDisplayName());
		}
		
		return listMap;
	}
	
	public static <T extends Listable<Long>, C extends Collection<T>> List<ListingPair> longListableToListingPair(C list) {
		List<ListingPair> listingPair = new ArrayList<ListingPair>();
		
		for(Listable<Long> listable: list) {
			listingPair.add(new ListingPair(listable));
		}
		
		return listingPair;
	}
	
	public static <T extends Listable<Integer>, C extends Collection<T>> List<ListingPair> intListableToListingPair(C list) {
		List<ListingPair> listingPair = new ArrayList<ListingPair>();
		
		ListingPair lp;
		for(Listable<Integer> listable: list) {
			lp = new ListingPair();
			lp.setId(listable.getId().longValue());
			lp.setName(listable.getDisplayName());
			listingPair.add(lp);
		}
		
		return listingPair;
	}
	
	
	public static <K, T extends Collection<K>> Set<K> toSet(T collection) {
		return new HashSet<K>(collection);
	}
	
	public static <K, T extends Collection<K>> SortedSet<K> toSortedSet(T collection) {
		return new TreeSet<K>(collection);
	}
	
	public static <K, T extends Collection<K>> List<K> toList(T collection) {
		return new ArrayList<K>(collection);
	}
	
	public static <K, T extends Collection<K>> Queue<K> toQueue(T collection) {
		return new LinkedList<K>(collection);
	}
	
	public static <K, T extends Collection<K>> BlockingQueue<K> toBlockingQueue(T collection) {
		return new LinkedBlockingQueue<K>(collection);
	}
	
	public static <K> List<List<K>> splitList(List<K> list, int subListSize) {
		if (subListSize < 1) {
			throw new IllegalArgumentException("subListSize must be > 0");
		}
		
		List<List<K>> masterList = new ArrayList<List<K>>();
		
		for (int i = 0; i <= (list.size() - subListSize); i += subListSize) {
			masterList.add(list.subList(i, i + subListSize));
		}
		
		if ((list.size() % subListSize) != 0) {
			// add in anything remaining
			masterList.add(list.subList(list.size() - (list.size() % subListSize), list.size()));
		}
		
		return masterList;
	}
	
	public static <T extends Collection<M>, K extends Collection<M>, M> K copy(T src, K dst) {
		for (M o: src) {
			dst.add(o);
		}
		return dst;
	}
	
	public static boolean isNullOrEmpty(Collection<?> c) {
		return (c == null) ? true : c.isEmpty();
	}
}
