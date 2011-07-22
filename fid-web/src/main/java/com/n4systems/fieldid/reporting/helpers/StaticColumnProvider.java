package com.n4systems.fieldid.reporting.helpers;

import java.util.SortedSet;

import com.n4systems.model.search.ColumnMappingGroupView;

public interface StaticColumnProvider {

	public SortedSet<ColumnMappingGroupView> getMappings();

}
