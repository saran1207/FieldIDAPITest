package com.n4systems.fieldid.reporting.helpers;

import java.util.ArrayList;
import java.util.List;

import com.n4systems.model.builders.BaseBuilder;
import com.n4systems.model.search.ColumnMappingGroupView;
import com.n4systems.model.search.ColumnMappingView;
import com.n4systems.model.search.ReportConfiguration;
import com.n4systems.util.persistence.search.SortDirection;

public class ReportConfiguationBuilder extends BaseBuilder<ReportConfiguration> {     

    private List<ColumnMappingGroupView> columnGroups;
    private ColumnMappingView sortColumn;
    private SortDirection sortDirection;
    

    public static ReportConfiguationBuilder aReportConfiguration() {
		return new ReportConfiguationBuilder(new ArrayList<ColumnMappingGroupView>(), ColumnMappingBuilder.aColumnMapping().build(), SortDirection.ASC );
	}

    public ReportConfiguationBuilder(){}

	private ReportConfiguationBuilder(List<ColumnMappingGroupView> columnGroups, ColumnMappingView sortColumn, SortDirection sortDirection) {
		this.columnGroups = columnGroups;
		this.sortColumn=sortColumn;
		this.sortDirection=sortDirection;
	}

    public ReportConfiguationBuilder withSortDirection(SortDirection sortDirection) {
        return new ReportConfiguationBuilder(columnGroups, sortColumn, sortDirection);
    }

    public ReportConfiguationBuilder withSortColumn(ColumnMappingView sortColumn) {
        return new ReportConfiguationBuilder(columnGroups, sortColumn, sortDirection);
    }

    public ReportConfiguationBuilder withColumnGroups(List<ColumnMappingGroupView> columnGroups) {
        return new ReportConfiguationBuilder(columnGroups, sortColumn, sortDirection);
    }

  
	@Override
	public ReportConfiguration createObject() {
		return new ReportConfiguration(columnGroups,sortColumn,sortDirection);
	}
		
}
