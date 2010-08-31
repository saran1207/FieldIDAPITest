package com.n4systems.util;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class MapUtils {
	
	/**
	 * Swaps the key and value for each map entry.  The map returned will be the
	 * same type as the one passed in as long as it has a default constructor.
	 * Any failure to instantiate will result in a return type of HashMap.
	 * @param map	A Map
	 * @return		A new Map with Keys and Values reversed
	 */
	@SuppressWarnings("unchecked")
	public static <K, V> Map<V, K> swapKeyVales(Map<K, V> map) {
		Map<V, K> to;
		try {
			to = map.getClass().newInstance();
		} catch (Exception e) {
			to = new HashMap<V, K>();
		}
		
		swapKeyVales(map, to);
		
		return to;
	}

	/**
	 * Reads entries in the from map, putting each into the to map where 
	 * key -> value become value -> key
	 * @param from
	 * @param to
	 */
	public static <K, V> void swapKeyVales(Map<K, V> from, Map<V, K> to) {
		for (Map.Entry<K, V> entry: from.entrySet()) {
			to.put(entry.getValue(), entry.getKey());
		}
	}
	
	/**
	 * Returns a map with it's keys and vales filled from the array elements.
	 * The map returned will of type LinkedHashMap to ensure the same iteration
	 * order as array.
	 * @param array	Array of values to fill map
	 * @return A LinkedHashMap with keys and values matching the entries of array
	 */
	public static <V> Map<V, V> fillMapKeysAndValues(V...array) {
		Map<V, V> map = new LinkedHashMap<V, V>(array.length);
		fillMapKeysAndValues(map, array);
		return map;
	}
	
	/**
	 * Fills the keys and values of this map with entries from this array
	 * @param map	A map with keys and values the same type as array
	 * @param array	Array of values to fill map
	 */
	public static <V> void fillMapKeysAndValues(Map<V, V> map, V...array) {
		for (V entry: array) {
			map.put(entry, entry);
		}
	}
	
	/**
	 * Maps and array of keys to an array of values.  Values without a corresponding
	 * key index will be ignored.  Keys without a corresponding value index will be
	 * mapped as null.  The map returned will of type LinkedHashMap to ensure the same iteration
	 * order as the keys array.
	 */
	public static <K, V, k extends K, v extends V> Map<K, V> combineArrays(k[] keys, v[] values) {
		Map<K, V> map = new LinkedHashMap<K, V>();
		
		combineArrays(map, keys, values);
		
		return map;
	}
	
	/**
	 * Maps and array of keys to an array of values.  Values without a corresponding
	 * key index will be ignored.  Keys without a corresponding value index will be
	 * mapped as null.
	 */
	public static <K, V, k extends K, v extends V> void combineArrays(Map<K, V> map, k[] keys, v[] values) {
		v value;
		for (int i = 0; i < keys.length; i++) {
			// if we have less values than keys, the remaining values will go in null
			value = (i < values.length) ? values[i] : null;
			
			map.put(keys[i], value);
		}
	}
}
