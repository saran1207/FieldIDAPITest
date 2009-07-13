package com.n4systems.persistence.loaders;

import java.util.List;

import com.n4systems.util.SecurityFilter;

public abstract class FilteredListLoader<T> extends FilteredLoader<List<T>> {

	public FilteredListLoader(SecurityFilter filter) {
		super(filter);
	}

}
