package com.n4systems.fieldid.reporting.helpers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.SortedSet;
import java.util.TreeSet;

import com.n4systems.fieldid.viewhelpers.ColumnMapping;
import com.n4systems.fieldid.viewhelpers.ColumnMappingGroup;

public class AvailableReportColumns {

	private final StaticColumnProvider staticColumnProvider;
	private SortedSet<ColumnMappingGroup> groups;
	private final DynamicColumnProvider dynamicColumnProvider;
	private ReportColumnFilter reportColumnFilter;

	public AvailableReportColumns(StaticColumnProvider staticColumnProvider, DynamicColumnProvider dynamicColumnProvider) {
		this.staticColumnProvider = staticColumnProvider;
		this.dynamicColumnProvider = dynamicColumnProvider;
		this.reportColumnFilter = new NoFilterReportColumnFilter();
	}

	public SortedSet<ColumnMappingGroup> getMappingGroups() {
		return getGroups();
	}

	private SortedSet<ColumnMappingGroup> getGroups() {
		if (groups == null) {
			initalizeGroups();
			filterGroups();
		}
		
		return groups;
	}

	private void filterGroups() {
		for (ColumnMappingGroup columnMappingGroup : groups) {
			Collection<ColumnMapping> mappingsToRemove = new ArrayList<ColumnMapping>();
			for (ColumnMapping columnMapping : columnMappingGroup.getMappings()) {
				if (!reportColumnFilter.available(columnMapping)) {
					mappingsToRemove.add(columnMapping);
				}
			}
			columnMappingGroup.getMappings().removeAll(mappingsToRemove);
		}
		
	}

	private void initalizeGroups() {
		groups = new TreeSet<ColumnMappingGroup>();
		groups.addAll(staticColumnProvider.getMappings());
		groups.addAll(dynamicColumnProvider.getDynamicGroups());
	}

	public void setFilter(ReportColumnFilter reportColumnFilter) {
		this.reportColumnFilter = reportColumnFilter;
		
	}

}
