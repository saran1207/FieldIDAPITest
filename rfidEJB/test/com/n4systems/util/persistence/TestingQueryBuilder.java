package com.n4systems.util.persistence;

import com.n4systems.model.security.ManualSecurityFilter;

public class TestingQueryBuilder<E> extends QueryBuilder<E> {

	public TestingQueryBuilder(Class<?> tableClass) {
		super(tableClass, new ManualSecurityFilter(null, null, null));
	}

}
