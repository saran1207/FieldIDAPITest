package com.n4systems.util;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.google.common.collect.ImmutableList;


public class ListingPairsTest {

	
	@Test
	public void should_return_an_empty_list_of_longs_from_an_empty_list_pair() throws Exception {
		
		List<ListingPair> listingPairs = new ArrayList<ListingPair>();
		
		
		List<Long> actualIdList = ListingPairs.convertToIdList(listingPairs);
		
		assertThat(actualIdList.size(), equalTo(0));
	}
	
	
	@Test
	public void should_return_of_the_ids_in_the_same_order_as_the_given_listing_pair() throws Exception {
		
		List<ListingPair> listingPairs = ImmutableList.of(
				new ListingPair(1L, "name"),
				new ListingPair(3L, "name"),
				new ListingPair(6L, "name"));
		
		
		List<Long> actualIdList = ListingPairs.convertToIdList(listingPairs);
		
		assertThat(actualIdList, hasItems(1L, 3L, 6L));
	}
	
	

	@Test(expected=NullPointerException.class)
	public void should_throw_null_pointer_exception_if_null_is_handed_in() {
		ListingPairs.convertToIdList(null);
	}
}
