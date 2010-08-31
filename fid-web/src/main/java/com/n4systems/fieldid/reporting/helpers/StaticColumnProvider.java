package com.n4systems.fieldid.reporting.helpers;

import java.util.SortedSet;

import com.n4systems.fieldid.viewhelpers.ColumnMappingGroup;

public interface StaticColumnProvider {

	public SortedSet<ColumnMappingGroup> getMappings();

}
