package com.n4systems.model.search;

import com.n4systems.model.search.ColumnMappingGroupView;
import com.n4systems.model.search.ColumnMappingView;
import com.n4systems.util.persistence.search.SortDirection;

import java.util.List;

public class ReportConfiguration {

    private List<ColumnMappingGroupView> columnGroups;
    private ColumnMappingView sortColumn;
    private SortDirection sortDirection;

    public ReportConfiguration(List<ColumnMappingGroupView> columnGroups, ColumnMappingView sortColumn, SortDirection sortDirection) {
        this.columnGroups = columnGroups;
        this.sortColumn = sortColumn;
        this.sortDirection = sortDirection;
    }

    public List<ColumnMappingGroupView> getColumnGroups() {
        return columnGroups;
    }

    public ColumnMappingView getSortColumn() {
        return sortColumn;
    }

    public SortDirection getSortDirection() {
        return sortDirection;
    }

}
