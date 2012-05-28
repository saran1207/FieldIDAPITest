package com.n4systems.model.search;

import com.n4systems.model.AssetTypeGroup;
import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.model.parents.AbstractEntity;
import com.n4systems.util.persistence.search.SortDirection;
import com.n4systems.util.selection.MultiIdSelection;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@MappedSuperclass
public abstract class SearchCriteria extends AbstractEntity {

    @Transient
    private List<ColumnMappingGroupView> columnGroups = new ArrayList<ColumnMappingGroupView>();

    @Transient
    private List<ColumnMappingGroupView> dynamicEventColumnGroups = new ArrayList<ColumnMappingGroupView>();

    @Transient
    private List<ColumnMappingGroupView> dynamicAssetColumnGroups = new ArrayList<ColumnMappingGroupView>();

    @Transient
    private Integer pageNumber = 0;

    @Transient
    private ColumnMappingView sortColumn;

    @ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "ownerId")
    private BaseOrg owner;

    @Enumerated(EnumType.STRING)
    @Column(name="sortDirection")
    private SortDirection sortDirection;

    @Transient
    private boolean reportAlreadyRun;

    @Transient
    private MultiIdSelection selection = new MultiIdSelection();

    @Transient
    private Long savedReportId;

    @Transient
    private String savedReportName;

    @Column(name="sortColumnId")
    private Long sortColumnId;

    @ManyToOne
    @JoinColumn(name="assetTypeGroup")
    private AssetTypeGroup assetTypeGroup;

    public abstract List<String> getColumns();
    public abstract void setColumns(List<String> columns);

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
        setSortColumnId(sortColumn.getDbColumnId());
    }

    public SortDirection getSortDirection() {
        return sortDirection;
    }

    public void setSortDirection(SortDirection sortDirection) {
        this.sortDirection = sortDirection;
    }

    // This is used to determine whether the criteria panel will attempt to put in a default
    // report configuration according to available columns and those selected in the template configuration in setup.
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

    public Long getSortColumnId() {
        return sortColumnId;
    }

    public void setSortColumnId(Long sortColumnId) {
        this.sortColumnId = sortColumnId;
    }

    public Long getSavedReportId() {
        return savedReportId;
    }

    public void setSavedReportId(Long savedReportId) {
        this.savedReportId = savedReportId;
    }

    public String getSavedReportName() {
        return savedReportName;
    }

    public void  setSavedReportName(String savedReportName) {
        this.savedReportName = savedReportName;
    }

    public AssetTypeGroup getAssetTypeGroup() {
        return assetTypeGroup;
    }

    public void setAssetTypeGroup(AssetTypeGroup assetTypeGroup) {
        this.assetTypeGroup = assetTypeGroup;
    }

    @Transient
    public boolean requiresLeftOuterJoin() {
        return false;
    }

}
