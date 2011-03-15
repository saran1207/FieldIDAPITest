package com.n4systems.model.columns;

import com.n4systems.model.parents.EntityWithTenant;
import com.n4systems.util.persistence.search.SortDirection;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name="column_layouts")
public class ColumnLayout extends EntityWithTenant {

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "columnLayout")
    private Collection<ActiveColumnMapping> columnMappingList = new ArrayList<ActiveColumnMapping>();

    @ManyToOne
    @JoinColumn(name="sort_column_id")
    private ColumnMapping sortColumn;

    @Column(name="sort_direction")
    @Enumerated(EnumType.STRING)
    private SortDirection sortDirection;

    @Column(name="report_type")
    @Enumerated(EnumType.STRING)
    private ReportType reportType;

    public Collection<ActiveColumnMapping> getColumnMappingList() {
        return columnMappingList;
    }

    public void setColumnMappingList(Collection<ActiveColumnMapping> columnMappingList) {
        this.columnMappingList = columnMappingList;
    }

    public ColumnMapping getSortColumn() {
        return sortColumn;
    }

    public void setSortColumn(ColumnMapping sortColumn) {
        this.sortColumn = sortColumn;
    }

    public SortDirection getSortDirection() {
        return sortDirection;
    }

    public void setSortDirection(SortDirection sortDirection) {
        this.sortDirection = sortDirection;
    }

    public ReportType getReportType() {
        return reportType;
    }

    public void setReportType(ReportType reportType) {
        this.reportType = reportType;
    }

    public ActiveColumnMapping getActiveConfigurationFor(ColumnMapping mapping) {
        for (ActiveColumnMapping activeMapping : columnMappingList) {
            if (activeMapping.getMapping().equals(mapping)) {
                return activeMapping;
            }
        }
        return null;
    }

    public List<ColumnMapping> getUsedColumnMappingList() {
        List<ColumnMapping> usedColumns = new ArrayList<ColumnMapping>();
        for (ActiveColumnMapping activeMapping : columnMappingList) {
            usedColumns.add(activeMapping.getMapping());
        }
        return usedColumns;
    }
}
