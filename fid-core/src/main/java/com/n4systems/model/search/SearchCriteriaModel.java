package com.n4systems.model.search;

import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.util.persistence.search.SortDirection;
import com.n4systems.util.selection.MultiIdSelection;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class SearchCriteriaModel implements Serializable {

    private BaseOrg owner;

    private List<ColumnMappingGroupView> columnGroups = new ArrayList<ColumnMappingGroupView>();

    private List<ColumnMappingGroupView> dynamicEventColumnGroups = new ArrayList<ColumnMappingGroupView>();
    private List<ColumnMappingGroupView> dynamicAssetColumnGroups = new ArrayList<ColumnMappingGroupView>();

    private Integer pageNumber = 0;

    private ColumnMappingView sortColumn;
    private SortDirection sortDirection;

    private boolean reportAlreadyRun;

    private MultiIdSelection selection = new MultiIdSelection();

    public List<ColumnMappingGroupView> getDynamicEventColumnGroups() {
        return dynamicEventColumnGroups;
    }

    public void setDynamicEventColumnGroups(List<ColumnMappingGroupView> dynamicEventColumnGroups) {
        this.dynamicEventColumnGroups = dynamicEventColumnGroups;
    }

    public List<ColumnMappingGroupView> getDynamicAssetColumnGroups() {
        return dynamicAssetColumnGroups;
    }

    public void setDynamicAssetColumnGroups(List<ColumnMappingGroupView> dynamicAssetColumnGroups) {
        this.dynamicAssetColumnGroups = dynamicAssetColumnGroups;
    }

    public Integer getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(Integer pageNumber) {
        this.pageNumber = pageNumber;
    }

    public List<ColumnMappingGroupView> getColumnGroups() {
        return columnGroups;
    }

    public void setColumnGroups(List<ColumnMappingGroupView> columnGroups) {
        this.columnGroups = columnGroups;
    }


    public List<ColumnMappingView> getSortedStaticAndDynamicColumns() {
        return getSortedStaticAndDynamicColumns(true);
    }

    public List<ColumnMappingView> getSortedStaticAndDynamicColumns(boolean enabledOnly) {
        List<ColumnMappingGroupView> allColumnGroups = new ArrayList<ColumnMappingGroupView>();
        allColumnGroups.addAll(getColumnGroups());
        allColumnGroups.addAll(getDynamicAssetColumnGroups());
        allColumnGroups.addAll(getDynamicEventColumnGroups());

        List<ColumnMappingView> enabledColumns = new ArrayList<ColumnMappingView>();

        for (ColumnMappingGroupView columnGroup : allColumnGroups) {
            for (ColumnMappingView column : columnGroup.getMappings()) {
                if (!enabledOnly || column.isEnabled()) {
                    enabledColumns.add(column);
                }
            }
        }
        Collections.sort(enabledColumns, new Comparator<ColumnMappingView>() {
            @Override
            public int compare(ColumnMappingView column1, ColumnMappingView column2) {
                return new Integer(column1.getOrder()).compareTo(column2.getOrder());
            }
        });

        return enabledColumns;
    }

    public MultiIdSelection getSelection() {
        return selection;
    }

    public void setSelection(MultiIdSelection selection) {
        this.selection = selection;
    }

    public ColumnMappingView getSortColumn() {
        return sortColumn;
    }

    public void setSortColumn(ColumnMappingView sortColumn) {
        this.sortColumn = sortColumn;
    }

    public SortDirection getSortDirection() {
        return sortDirection;
    }

    public void setSortDirection(SortDirection sortDirection) {
        this.sortDirection = sortDirection;
    }

    public boolean isReportAlreadyRun() {
        return reportAlreadyRun;
    }

    public void setReportAlreadyRun(boolean reportAlreadyRun) {
        this.reportAlreadyRun = reportAlreadyRun;
    }

    public BaseOrg getOwner() {
        return owner;
    }

    public void setOwner(BaseOrg owner) {
        this.owner = owner;
    }


}
