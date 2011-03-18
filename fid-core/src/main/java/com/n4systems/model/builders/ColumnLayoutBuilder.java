package com.n4systems.model.builders;

import com.n4systems.model.Tenant;
import com.n4systems.model.columns.ActiveColumnMapping;
import com.n4systems.model.columns.ColumnLayout;
import com.n4systems.model.columns.ColumnMapping;
import com.n4systems.model.columns.ReportType;
import com.n4systems.util.persistence.search.SortDirection;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ColumnLayoutBuilder extends BaseEntityBuilder<ColumnLayout> {

    private ColumnMapping sortColumn;
    private SortDirection sortDirection;
    private List<ActiveColumnMapping> activeColumnMappings;
    private ReportType reportType;
    private Tenant tenant;

    public static ColumnLayoutBuilder aColumnLayout() {
        return new ColumnLayoutBuilder(null, null, Collections.<ActiveColumnMapping>emptyList(), null, null);
    }

    public ColumnLayoutBuilder(ColumnMapping sortColumn, SortDirection sortDirection, List<ActiveColumnMapping> activeColumnMappings, Tenant tenant, ReportType reportType) {
        super(null);
        this.sortColumn = sortColumn;
        this.sortDirection = sortDirection;
        this.activeColumnMappings = activeColumnMappings;
        this.tenant = tenant;
        this.reportType = reportType;
    }

    public ColumnLayoutBuilder sortColumn(ColumnMapping sortColumn) {
        return makeBuilder(new ColumnLayoutBuilder(sortColumn, sortDirection, activeColumnMappings, tenant, reportType));
    }

    public ColumnLayoutBuilder sortDirection(SortDirection sortDirection) {
        return makeBuilder(new ColumnLayoutBuilder(sortColumn, sortDirection, activeColumnMappings, tenant, reportType));
    }

    public ColumnLayoutBuilder tenant(Tenant tenant) {
        return makeBuilder(new ColumnLayoutBuilder(sortColumn, sortDirection, activeColumnMappings, tenant, reportType));
    }

    public ColumnLayoutBuilder reportType(ReportType reportType) {
        return makeBuilder(new ColumnLayoutBuilder(sortColumn, sortDirection, activeColumnMappings, tenant, reportType));
    }

    public ColumnLayoutBuilder activeColumnMappings(ActiveColumnMapping... mappings) {
        List<ActiveColumnMapping> activeColumnMappings = new ArrayList<ActiveColumnMapping>(mappings.length);
        activeColumnMappings.addAll(Arrays.asList(mappings));
        return makeBuilder(new ColumnLayoutBuilder(sortColumn, sortDirection, activeColumnMappings, tenant, reportType));
    }

    @Override
    public ColumnLayout createObject() {
        if (tenant == null) {
            throw new RuntimeException("You don't want to make a column layout with a null tenant. There can be only one -- it represents the default layout.");
        }

        ColumnLayout columnLayout = super.assignAbstractFields(new ColumnLayout());
        columnLayout.setSortColumn(sortColumn);
        columnLayout.setSortDirection(sortDirection);
        columnLayout.setColumnMappingList(activeColumnMappings);
        columnLayout.setTenant(tenant);
        columnLayout.setReportType(reportType);

        return columnLayout;
    }

}
