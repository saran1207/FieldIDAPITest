package com.n4systems.persistence.loaders;

import java.util.List;

import com.n4systems.model.security.SecurityFilter;

abstract public class ListLoader<T> extends SecurityFilteredLoader<List<T>> {
	
	public ListLoader(SecurityFilter filter) {
		super(filter);
	}
	
	
	
}
