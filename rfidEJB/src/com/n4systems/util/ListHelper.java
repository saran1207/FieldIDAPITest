package com.n4systems.util;

import com.n4systems.model.api.Listable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ListHelper {

	public static <T extends Listable<Long>> Map<Long, String> longListableToMap(List<T> list) {
		Map<Long, String> listMap = new LinkedHashMap<Long, String>();
		
		for(Listable<Long> listable: list) {
			listMap.put(listable.getId(), listable.getDisplayName());
		}
		
		return listMap;
	}
	
	public static <T extends Listable<String>> Map<String, String> stringListableToMap(T ... listables) {
		return stringListableToMap(Arrays.asList(listables));
	}
	
	public static <T extends Listable<String>> Map<String, String> stringListableToMap(List<T> list) {
		Map<String, String> listMap = new LinkedHashMap<String, String>();
		
		for(Listable<String> listable: list) {
			listMap.put(listable.getId(), listable.getDisplayName());
		}
		
		return listMap;
	}
	
	public static <T extends Listable<Long>> List<ListingPair> longListableToListingPair(List<T> list) {
		List<ListingPair> listingPair = new ArrayList<ListingPair>();
		
		for(Listable<Long> listable: list) {
			listingPair.add(new ListingPair(listable));
		}
		
		return listingPair;
	}
	
	public static <T extends Listable<Integer>> List<ListingPair> intListableToListingPair(List<T> list) {
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
