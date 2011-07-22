package com.n4systems.fieldid.reporting.helpers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.SortedSet;
import java.util.TreeSet;

import com.n4systems.model.search.ColumnMappingGroupView;
import com.n4systems.model.search.ColumnMappingView;

public class AvailableReportColumns {

	private final StaticColumnProvider staticColumnProvider;
	private SortedSet<ColumnMappingGroupView> groups;
	private final DynamicColumnProvider dynamicColumnProvider;
	private ReportColumnFilter reportColumnFilter;

	public AvailableReportColumns(StaticColumnProvider staticColumnProvider, DynamicColumnProvider dynamicColumnProvider) {
		this.staticColumnProvider = staticColumnProvider;
		this.dynamicColumnProvider = dynamicColumnProvider;
		this.reportColumnFilter = new NoFilterReportColumnFilter();
	}

	public SortedSet<ColumnMappingGroupView> getMappingGroups() {
		return getGroups();
	}

	private SortedSet<ColumnMappingGroupView> getGroups() {
		if (groups == null) {
			initalizeGroups();
			filterGroups();
		}
		
		return groups;
	}

	private void filterGroups() {
		for (ColumnMappingGroupView columnMappingGroup : groups) {
			Collection<ColumnMappingView> mappingsToRemove = new ArrayList<ColumnMappingView>();
			for (ColumnMappingView columnMapping : columnMappingGroup.getMappings()) {
				if (!reportColumnFilter.available(columnMapping)) {
					mappingsToRemove.add(columnMapping);
				}
			}
			columnMappingGroup.getMappings().removeAll(mappingsToRemove);
		}
		
	}

	private void initalizeGroups() {
		groups = new TreeSet<ColumnMappingGroupView>();
		groups.addAll(staticColumnProvider.getMappings());
		groups.addAll(dynamicColumnProvider.getDynamicGroups());
	}

	public void setFilter(ReportColumnFilter reportColumnFilter) {
		this.reportColumnFilter = reportColumnFilter;
		
	}

}
