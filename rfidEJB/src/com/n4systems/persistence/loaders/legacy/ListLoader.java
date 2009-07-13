package com.n4systems.persistence.loaders.legacy;

import java.util.List;

import com.n4systems.ejb.PersistenceManager;
import com.n4systems.util.SecurityFilter;

/**
 * @deprecated Use {@link FilteredListLoader} instead
 */
@Deprecated
abstract public class ListLoader<T> extends SecuredLoader<List<T>> {

	public ListLoader(PersistenceManager pm, SecurityFilter filter) {
		super(pm, filter);
	}

	public ListLoader(SecurityFilter filter) {
		super(filter);
	}

}
