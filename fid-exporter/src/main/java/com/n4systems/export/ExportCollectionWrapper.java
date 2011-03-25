package com.n4systems.export;

import java.util.Collection;

public class ExportCollectionWrapper<T> {
	private final Collection<T> collection;

	public ExportCollectionWrapper(Collection<T> collection) {
		this.collection = collection;
	}
	
	public Collection<T> getCollection() {
		return collection;
	}
}
