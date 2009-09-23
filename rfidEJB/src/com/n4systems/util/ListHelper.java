package com.n4systems.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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
}
