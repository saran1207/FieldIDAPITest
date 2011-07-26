package com.n4systems.fieldid.service.search.columns;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.n4systems.model.search.ColumnMappingConverter;
import com.n4systems.model.search.ColumnMappingGroupView;
import com.n4systems.model.search.ColumnMappingView;
import com.n4systems.model.columns.ActiveColumnMapping;
import com.n4systems.model.columns.ColumnLayout;
import com.n4systems.model.columns.ColumnMapping;
import com.n4systems.model.columns.ColumnMappingGroup;
import com.n4systems.model.columns.ReportType;
import com.n4systems.model.columns.loader.ColumnLayoutLoader;
import com.n4systems.model.columns.loader.ColumnMappingGroupLoader;
import com.n4systems.model.columns.loader.CustomColumnsLoader;
import com.n4systems.model.orgs.PrimaryOrg;
import com.n4systems.model.search.ReportConfiguration;
import com.n4systems.model.security.SecurityFilter;

public abstract class ColumnsService {

    protected abstract ReportType getReportType();

    public ReportConfiguration getReportConfiguration(SecurityFilter filter) {
        List<ColumnMappingGroupView> groupViews = new ArrayList<ColumnMappingGroupView>();

        PrimaryOrg primaryOrg = filter.getOwner().getPrimaryOrg();
        List<ColumnMappingGroup> loadedGroups = new ColumnMappingGroupLoader(primaryOrg).reportType(getReportType()).load();
        List<ColumnMappingGroup> groupsIncludingCustom = new ArrayList<ColumnMappingGroup>();
        groupsIncludingCustom.addAll(loadedGroups);
        if (includeCustom())
            groupsIncludingCustom.add(createCustomGroup(filter));

        ColumnLayout layout = new ColumnLayoutLoader(filter).reportType(getReportType()).load();

        for (ColumnMappingGroup mappingGroup : groupsIncludingCustom) {
            String groupKey = mappingGroup.getGroupKey();
            ColumnMappingGroupView groupView = new ColumnMappingGroupView(mappingGroup.getLabel(), mappingGroup.getLabel(), mappingGroup.getOrder(), groupKey);
            
            List<ColumnMapping> columnMappings = new ArrayList<ColumnMapping>(mappingGroup.getColumnMappings());
            Collections.sort(columnMappings);
			for (ColumnMapping column : columnMappings) {
                processColumn(layout, groupView, column);
            }
            groupViews.add(groupView);
        }

        return new ReportConfiguration(groupViews, convertColumn(layout.getSortColumn()), layout.getSortDirection());
    }

    private ColumnMappingGroup createCustomGroup(SecurityFilter filter) {
        List<ColumnMapping> customColumns = new CustomColumnsLoader(filter).reportType(getReportType()).load();
        ColumnMappingGroup fakeCustomGroup = new ColumnMappingGroup();
        fakeCustomGroup.setOrder(5000);
        fakeCustomGroup.setType(getReportType());
        fakeCustomGroup.setLabel("label.customattributes");
        fakeCustomGroup.getColumnMappings().addAll(customColumns);
        fakeCustomGroup.setGroupKey("custom_created_columns");

        for (ColumnMapping customColumn : customColumns) {
            customColumn.setGroup(fakeCustomGroup);
        }

        return fakeCustomGroup;
    }

    private void processColumn(ColumnLayout layout, ColumnMappingGroupView groupView, ColumnMapping mapping) {
        ColumnMappingView convertedView = convertColumn(mapping);
        ActiveColumnMapping activeConfig = layout.getActiveConfigurationFor(mapping);
        if (activeConfig != null) {
            convertedView.setOrder(activeConfig.getOrder());
            convertedView.setEnabled(true);
        } else {
            convertedView.setEnabled(false);
        }
        groupView.getMappings().add(convertedView);
    }

    private ColumnMappingView convertColumn(ColumnMapping systemMapping) {
        return new ColumnMappingConverter().convert(systemMapping);
    }

    protected boolean includeCustom() {
        return true;
    }

}
