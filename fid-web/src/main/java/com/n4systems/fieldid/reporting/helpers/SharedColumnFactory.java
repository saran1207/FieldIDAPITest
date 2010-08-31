package com.n4systems.fieldid.reporting.helpers;

import java.util.SortedSet;

import com.n4systems.fieldid.viewhelpers.ColumnMappingFactory;
import com.n4systems.fieldid.viewhelpers.ColumnMappingGroup;
import com.n4systems.model.Tenant;

public class SharedColumnFactory implements StaticColumnProvider {

	private final Class<?> implementingClass;
	private final Tenant tenant;

	public SharedColumnFactory(Class<?> implementingClass, Tenant tenant) {
		this.implementingClass = implementingClass;
		this.tenant = tenant;
	}

	public SortedSet<ColumnMappingGroup> getMappings() {
		return ColumnMappingFactory.getMappings(implementingClass, tenant);
	}

}
