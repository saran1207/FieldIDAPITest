package com.n4systems.persistence.loaders;

import com.n4systems.model.security.SecurityFilter;

import java.util.List;

abstract public class ListLoader<T> extends SecurityFilteredLoader<List<T>> {
	
	public ListLoader(SecurityFilter filter) {
		super(filter);
	}
	
	
	
}
