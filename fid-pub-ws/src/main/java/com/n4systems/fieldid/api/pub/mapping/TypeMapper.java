package com.n4systems.fieldid.api.pub.mapping;

import java.util.ArrayList;
import java.util.List;

public class TypeMapper<F, T> implements Mapper<F, T> {

	private final List<Mapper<F, T>> mappings;

	public TypeMapper(List<Mapper<F, T>> mappings) {
		this.mappings = mappings;
	}

	public TypeMapper(TypeMapper<F, T> typeMapper) {
		this();
		addMappings(typeMapper);
	}

	public TypeMapper() {
		this(new ArrayList<>());
	}

	public List<Mapper<F, T>> getMappings() {
		return mappings;
	}

	public TypeMapper<F, T> addMapping(Mapper<F, T> mapping) {
		mappings.add(mapping);
		return this;
	}

	public TypeMapper<F, T> addMappings(TypeMapper<F, T> typeMapper) {
		mappings.addAll(typeMapper.getMappings());
		return this;
	}

	@Override
	public void map(F from, T to) {
		for (Mapper<F, T> mapping : mappings) {
			mapping.map(from, to);
		}
	}

}
