package com.n4systems.fieldid.reporting.helpers;

import java.util.SortedSet;

import com.n4systems.fieldid.viewhelpers.ColumnMappingGroupView;

public interface DynamicColumnProvider {

	public SortedSet<ColumnMappingGroupView> getDynamicGroups();

}
