package com.n4systems.fieldid.reporting.helpers;

import com.n4systems.fieldid.viewhelpers.ColumnMappingFactory;
import com.n4systems.model.Tenant;
import com.n4systems.model.search.ColumnMappingGroupView;

import java.util.SortedSet;

public class SharedColumnFactory implements StaticColumnProvider {

	private final Class<?> implementingClass;

	public SharedColumnFactory(Class<?> implementingClass, Tenant tenant) {
		this.implementingClass = implementingClass;
	}

	public SortedSet<ColumnMappingGroupView> getMappings() {
		return ColumnMappingFactory.getMappings(implementingClass);
	}

}
