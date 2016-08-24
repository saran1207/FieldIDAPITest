package com.n4systems.fieldid.reporting.helpers;

import com.n4systems.model.search.ColumnMappingGroupView;

import java.util.SortedSet;

public interface DynamicColumnProvider {

	public SortedSet<ColumnMappingGroupView> getDynamicGroups();

}
