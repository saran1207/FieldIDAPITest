package com.n4systems.fieldid.api.pub.mapping;

public class CompositeFieldMapper<F, T, V> implements Mapper<F, T> {
	private final GetterReference<F, V> getter;
	private final Mapper<V, T> subMapper;

	public CompositeFieldMapper(GetterReference<F, V> getter, Mapper<V, T> subMapper) {
		this.getter = getter;
		this.subMapper = subMapper;
	}

	@Override
	public void map(F from, T to) {
		V value = getter.get(from);
		if (value != null) {
			subMapper.map(value, to);
		}
	}
}
