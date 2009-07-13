package com.n4systems.util;

import java.util.HashMap;
import java.util.Map;

public class ReportMap<V> extends HashMap<String, V> {
	private static final long serialVersionUID = 1L;
	private static final String defaultString = "";
	
	private final String nullReplacement;

	public ReportMap() {
		nullReplacement = defaultString;
	}

	public ReportMap(String nullReplacement) {
		this.nullReplacement = nullReplacement;
	}
	
	public ReportMap(int initialCapacity) {
		super(initialCapacity);
		nullReplacement = defaultString;
	}
	
	public ReportMap(Map<? extends String, ? extends V> map) {
		super(map);
		decodeValues(this);
		nullReplacement = defaultString;
	}
	
	public String getNullReplacement() {
		return nullReplacement;
	}
	
	@Override
	public V put(String key, V value) {
		return super.put(key, decodeValue(value));
	}

	@Override
	public void putAll(Map<? extends String, ? extends V> map) {
		super.putAll(map);
		decodeValues(this);
	}
	
	public void putEmpty(String...keys) {
		for(String key: keys) {
			put(key, null);
		}
	}

	private void decodeValues(Map<? extends String, V> map) {
		for(Map.Entry<? extends String, V> entry: map.entrySet()) {
			decodeValue(entry.getValue());
		}
	}
	
	@SuppressWarnings("unchecked")
	private V decodeValue(V value) {
		return (value instanceof String) ? (V)decodeString((String)value) : value;
	}

	private String decodeString(String value) {
		return (value == null) ? nullReplacement : value;
	}
}
