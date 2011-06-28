package com.n4systems.util;

import static org.junit.Assert.*;

import java.util.AbstractMap;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.junit.Test;

public class MapUtilsTest {

	@Test
	public void test_swap_keys_values() {
		Map<String, Integer> from = new HashMap<String, Integer>();
		Map<Integer, String> to = new HashMap<Integer, String>();
		
		from.put("one", 1);
		from.put("two", 2);
		from.put("three", 3);
		
		MapUtils.swapKeyValues(from, to);
		
		assertEquals(3, to.size());
		assertEquals(to.get(1), "one");
		assertEquals(to.get(2), "two");
		assertEquals(to.get(3), "three");
	}
	
	@Test
	public void swap_keys_values_create_correct_map_type() {
		Map<String, Integer> from = new TreeMap<String, Integer>();
		
		from.put("one", 1);
		from.put("two", 2);
		from.put("three", 3);
		
		Map<Integer, String> to = MapUtils.swapKeyVales(from);
		
		assertTrue(to instanceof TreeMap<?, ?>);
		assertEquals(3, to.size());
		assertEquals(to.get(1), "one");
		assertEquals(to.get(2), "two");
		assertEquals(to.get(3), "three");
	}
	
	@Test
	public void swap_keys_values_handles_empty() {
		Map<Integer, String> to = MapUtils.swapKeyVales(new TreeMap<String, Integer>());
		
		assertTrue(to instanceof TreeMap<?, ?>);
		assertTrue(to.isEmpty());
	}
	
	@Test
	public void swap_keys_values_defaults_to_hashmap_on_failure() {
		Map<Integer, String> to = MapUtils.swapKeyVales(new AbstractMap<String, Integer>() {

			@SuppressWarnings("unchecked")
			@Override
			public Set<java.util.Map.Entry<String, Integer>> entrySet() {
				return Collections.EMPTY_SET;
			}
			
		});
		
		assertTrue(to instanceof HashMap<?, ?>);
		assertTrue(to.isEmpty());
	}
	
	@Test
	public void test_fill_map_keys_and_values() {
		Map<Integer, Integer> map = new HashMap<Integer, Integer>();
		
		MapUtils.fillMapKeysAndValues(map, 1, 2, 3);
		
		assertEquals(3, map.size());
		assertEquals((Object)map.get(1), (Object)1);
		assertEquals((Object)map.get(2), (Object)2);
		assertEquals((Object)map.get(3), (Object)3);
	}

	@Test
	public void fill_map_keys_and_values_creates_linked_hash_map() {
		assertTrue(MapUtils.fillMapKeysAndValues(1, 2, 3) instanceof LinkedHashMap<?, ?>);
	}

	@Test
	public void fill_map_keys_and_values_iterates_in_order() {
		Map<Integer, Integer> map = MapUtils.fillMapKeysAndValues(1, 2, 3);

		Integer i = 1;
		for (Map.Entry<Integer, Integer> entry: map.entrySet()) {
			assertEquals(i, entry.getKey());
			assertEquals(i, entry.getValue());
			i++;
		}
	}
	
	@Test
	public void combine_arrays_maps_keys_to_values() {
		Map<String, Integer> map = new HashMap<String, Integer>();
		
		MapUtils.combineArrays(map, new String[] {"one", "two", "three"}, new Integer[] { 1, 2, 3 });

		assertEquals(3, map.size());
		assertEquals((Object)map.get("one"), 1);
		assertEquals((Object)map.get("two"), 2);
		assertEquals((Object)map.get("three"), 3);
	}
	
	@Test
	public void combine_arrays_creates_linked_hash_map() {
		assertTrue(MapUtils.combineArrays(new String[0], new String[0]) instanceof LinkedHashMap<?, ?>);
	}
	
	@Test
	public void combine_arrays_iterates_in_order() {
		Map<Integer, Integer> map = MapUtils.combineArrays(new Integer[] { 1, 2, 3 }, new Integer[] { 4, 5, 6 });

		Integer i = 1;
		for (Map.Entry<Integer, Integer> entry: map.entrySet()) {
			assertEquals(i, entry.getKey());
			assertEquals(new Integer(i + 3), entry.getValue());
			i++;
		}
	}
	
	@Test
	public void combine_arrays_ignores_extra_values() {
		Map<String, Integer> map = MapUtils.combineArrays(new String[] {"one", "two", "three"}, new Integer[] { 1, 2, 3, 4, 5, 6 });

		assertEquals(3, map.size());
		assertEquals((Object)map.get("one"), 1);
		assertEquals((Object)map.get("two"), 2);
		assertEquals((Object)map.get("three"), 3);
	}
	
	@Test
	public void combine_arrays_maps_missing_values_as_null() {
		Map<String, Integer> map = MapUtils.combineArrays(new String[] {"one", "two", "three"}, new Integer[] { 1, 2 });

		assertEquals(3, map.size());
		assertEquals((Object)map.get("one"), 1);
		assertEquals((Object)map.get("two"), 2);
		assertTrue(map.containsKey("three"));
		assertNull(map.get("three"));
	}
}
