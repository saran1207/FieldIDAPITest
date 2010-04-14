package com.n4systems.util;

import java.util.ArrayList;
import java.util.List;

public class ListingPairs {

	public static List<Long> convertToIdList(List<ListingPair> listingPairs) {
		if (listingPairs == null) {
			return new ArrayList<Long>();
		}
		return convertList(listingPairs);
	}

	private static List<Long> convertList(List<ListingPair> listingPairs) {
		ArrayList<Long> idList = new ArrayList<Long>(listingPairs.size());
		for (ListingPair listingPair : listingPairs) {
			idList.add(listingPair.getId());
		}
		return idList;
	}

}
